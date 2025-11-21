package main;

import config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class main {

    public static int getValidNumber(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int num = sc.nextInt();
                sc.nextLine();
                return num;
            } else {
                System.out.println("Invalid input! Numbers only.");
                System.out.print("Enter again: ");
                sc.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();
        char love = 0;

        int choice;

        do {
            System.out.println("===== MAIN MENU =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            choice = getValidNumber(sc);

            switch (choice) {
                case 1:
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();

                    String qryUser = "SELECT * FROM tbl_user WHERE u_email = ?";
                    List<Map<String, Object>> userList = db.fetchRecords(qryUser, email);

                    if (userList.isEmpty()) {
                        System.out.println("INVALID CREDENTIALS");
                        break;
                    }
                    Map<String, Object> acc = userList.get(0);
                    String type = acc.get("u_type").toString();
                    String status = acc.get("u_status").toString();
                    String realPass = acc.get("u_pass").toString();
                    if (type.equalsIgnoreCase("Staff")) {
                        if (!pass.equals(realPass)) {
                            System.out.println("INVALID CREDENTIALS");
                            break;
                        }
                    }
                    else if (type.equalsIgnoreCase("Admin")) {
                        String hashedInput = config.hashPassword(pass);
                        if (!hashedInput.equals(realPass)) {
                            System.out.println("INVALID CREDENTIALS");
                            break;
                        }
                    }
                    if (status.equalsIgnoreCase("Pending")) {
                        System.out.println("Account is Pending. Contact Admin!");
                        break;
                    }
                    System.out.println("LOGIN SUCCESS!");

                    if (type.equalsIgnoreCase("Admin")) {
                        Admin admin = new Admin();
                        admin.Admin();
                    } else if (type.equalsIgnoreCase("Staff")) {
                        Staff staff = new Staff();
                        staff.Staff();
                    }
                    break;

                case 2:
                    System.out.print("Enter user name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter user email: ");
                    String newEmail = sc.nextLine();

                    while (true) {
                        String checkEmail = "SELECT * FROM tbl_user WHERE u_email = ?";
                        List<Map<String, Object>> emailExists = db.fetchRecords(checkEmail, newEmail);
                        if (emailExists.isEmpty()) break;
                        System.out.print("Email already exists. Enter another email: ");
                        newEmail = sc.nextLine();
                    }
                    System.out.print("Enter user Type (1 - Admin / 2 - Staff): ");
                    int typeChoice = getValidNumber(sc);
                    while (typeChoice < 1 || typeChoice > 2) {
                        System.out.print("Invalid choice. Choose 1 or 2 only: ");
                        typeChoice = getValidNumber(sc);
                    }
                    String userType = (typeChoice == 1) ? "Admin" : "Staff";

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    String finalPassword;

                    if (userType.equals("Admin")) {
                        finalPassword = config.hashPassword(password);
                    }
                    else {
                        finalPassword = password;
                    }

                    String userStatus = userType.equals("Admin") ? "Approved" : "Pending";

                    String insertSQL = "INSERT INTO tbl_user(u_name, u_email, u_type, u_status, u_pass) VALUES (?, ?, ?, ?, ?)";
                    db.addRecord(insertSQL, name, newEmail, userType, userStatus, finalPassword);

                    if (userType.equals("Admin")) {
                        System.out.println("Admin account created successfully!");
                    } else {
                        System.out.println("Staff account registered successfully! Waiting for Admin approval.");
                    }
                    break;

                case 3:
                    System.out.println("Thank you! Sana Pasado.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
            System.out.print("Return to Main Menu? (Y/N): ");
            love = sc.next().charAt(0);
            sc.nextLine();
        } while (love == 'Y' || love == 'y');
        sc.close();
    }
}
