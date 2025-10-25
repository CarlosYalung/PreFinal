package main;

import config.config;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);
    config db = new config();

    private void customerAndService() {
    
        String custQuery = "SELECT * FROM tbl_PetCare";
        String[] custHeaders = {"ID", "Name", "Contact", "Email"};
        String[] custCols = {"id", "name", "contact", "email"};
        db.viewRecords(custQuery, custHeaders, custCols);

        String petQuery = "SELECT * FROM Pet";
        String[] petHeaders = {"ID", "Owner ID", "Name", "Breed", "Age", "Service"};
        String[] petCols = {"id", "owner_id", "name", "breed", "age", "service"};
        db.viewRecords(petQuery, petHeaders, petCols);

        String apptQuery = "SELECT * FROM Appointment";
        String[] apptHeaders = {"ID", "Customer ID", "Pet ID", "Service ID", "Date", "Notes"};
        String[] apptCols = {"id", "customer_id", "pet_id", "service_id", "appointment_date", "notes"};
        db.viewRecords(apptQuery, apptHeaders, apptCols);
    }

    private void viewUsers() {
        String usersQuery = "SELECT * FROM tbl_user";
        String[] usersHeaders = {"ID", "Name", "Email", "Type", "Status"};
        String[] usersCols = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        db.viewRecords(usersQuery, usersHeaders, usersCols);
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
            int choice = sc.nextInt();
            sc.nextLine(); 

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
                    int approveId = sc.nextInt();
                    sc.nextLine();

                    String sqlApprove = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sqlApprove, "Approved", approveId);
                    System.out.println(" User Approved Successfully!");
                    break;

                case 3:
                    customerAndService();
                    System.out.println("\n--- DELETE CUSTOMER ---");
                    System.out.print("Enter Customer ID to Delete: ");
                    int deleteID = sc.nextInt();
                    sc.nextLine();

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
