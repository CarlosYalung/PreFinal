package main;

import config.config;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Admin {

    static Scanner sc = new Scanner(System.in);
    static config db = new config();

    public static int getValidNumber() {
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

    public static int getExistingID(String query) {
        while (true) {
            int id = getValidNumber();
            List<Map<String, Object>> result = db.fetchRecords(query, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("ID not found. Try again.");
                System.out.print("Enter again: ");
            }
        }
    }

    public static void customerAndService() {
    
        String custQuery = "SELECT * FROM tbl_PetCare";
        String[] custHeaders = {"ID", "Name", "Contact", "Email"};
        String[] custCols = {"id", "name", "contact", "email"};
        db.viewRecords(custQuery, custHeaders, custCols);

        String petQuery = "SELECT * FROM Pet";
        String[] petHeaders = {"ID", "Owner ID", "Name", "Breed", "Age", "Service"};
        String[] petCols = {"id", "owner_id", "name", "breed", "age", "service"};
        db.viewRecords(petQuery, petHeaders, petCols);

        String apptQuery = "SELECT * FROM Appointment";
        String[] apptHeaders = {"ID", "Customer ID", "Pet ID", "Service", "Date", "Notes"};
        String[] apptCols = {"id", "customer_id", "pet_id", "service", "appointment_date", "notes"};
        db.viewRecords(apptQuery, apptHeaders, apptCols);
    }

    public static void viewUsers() {
        String usersQuery = "SELECT * FROM tbl_user";
        String[] usersHeaders = {"ID", "Name", "Email", "Type", "Status"};
        String[] usersCols = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        db.viewRecords(usersQuery, usersHeaders, usersCols);

        String apptQuery = "SELECT * FROM Appointment";
        String[] apptHeaders = {"ID", "Customer ID", "Pet ID", "Service", "Date", "Notes"};
        String[] apptCols = {"id", "customer_id", "pet_id", "service", "appointment_date", "notes"};
        db.viewRecords(apptQuery, apptHeaders, apptCols);
    }

    public void Admin() {
        char again;

        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View Users");
            System.out.println("2. Approve Account");
            System.out.println("3. Delete Customer");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter choice: ");

            int choice = getValidNumber();

            switch (choice) {

                case 1: 
                    System.out.println("\n=== USER LIST ===");
                    viewUsers();
                    System.out.println("\n=== CUSTOMER, PET, AND APPOINTMENT DATA ===");
                    customerAndService();
                    break;

                case 2: 
                    viewUsers();
                    System.out.print("\nEnter User ID to Approve: ");
                    int approveId = getExistingID("SELECT * FROM tbl_user WHERE u_id = ?");
                    String sqlApprove = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sqlApprove, "Approved", approveId);
                    System.out.println(" User Approved Successfully!");
                    break;

                case 3:
                    customerAndService();
                    System.out.println("\n--- DELETE CUSTOMER ---");
                    System.out.print("Enter Customer ID to Delete: ");
                    int deleteID = getExistingID("SELECT * FROM tbl_PetCare WHERE id = ?");
                    String sqlDelete = "DELETE FROM tbl_PetCare WHERE id = ?";
                    db.deleteRecord(sqlDelete, deleteID);
                    System.out.println(" Customer Deleted Successfully!");
                    break;

                case 4: 
                    System.out.println(" Returning to Main Menu...");
                    return;

                default:
                    System.out.println(" Invalid option. Please try again.");
            }

            System.out.print("\nDo you want to continue in ADMIN DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println(" Exiting Admin Dashboard... Goodbye!");
    }
}
