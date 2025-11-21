package main;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Staff {

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

    public static int getExistingCustomerID() {
        while (true) {
            int id = getValidNumber();
            String check = "SELECT * FROM tbl_PetCare WHERE id = ?";
            List<Map<String, Object>> result = db.fetchRecords(check, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("Customer ID not found. Try again.");
                System.out.print("Enter Customer ID: ");
            }
        }
    }

    public static int getExistingPetID() {
        while (true) {
            int id = getValidNumber();
            String check = "SELECT * FROM Pet WHERE id = ?";
            List<Map<String, Object>> result = db.fetchRecords(check, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("Pet ID not found. Try again.");
                System.out.print("Enter Pet ID: ");
            }
        }
    }

    public static void customerAndService() {
        
        String custQuery = "SELECT * FROM tbl_PetCare";
        String[] custHeaders = {"ID", "Name", "Contact", "Email"};
        String[] custCols = {"id", "name", "contact", "email"};
        db.viewRecords(custQuery, custHeaders, custCols);

        String petQuery = "SELECT * FROM Pet";
        String[] petHeaders = {"ID", "Owner ID", "Name", "Breed", "Age", "Service", "Price"};
        String[] petCols = {"id", "owner_id", "name", "breed", "age", "service", "price"};
        db.viewRecords(petQuery, petHeaders, petCols);

        String apptQuery = "SELECT * FROM Appointment";
        String[] apptHeaders = {"ID", "Customer ID", "Pet ID", "Service", "Date", "Notes"};
        String[] apptCols = {"id", "customer_id", "pet_id", "service", "appointment_date", "notes"};
        db.viewRecords(apptQuery, apptHeaders, apptCols);
    }

    
    public void Staff() {
        char again;

        do {
            System.out.println("\n===== STAFF DASHBOARD =====");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Pet and Service");
            System.out.println("3. Appointment");
            System.out.println("4. View Customer, Pet, and Services");
            System.out.println("5. Update Customer");
            System.out.println("6. Update Pet and Service");
            System.out.println("7. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int resp = getValidNumber();

            switch (resp) {

                case 1: 
                    System.out.print("Enter customer name: ");
                    String customerName = sc.nextLine();
                    System.out.print("Enter contact: ");
                    String customerContact = sc.nextLine();
                    System.out.print("Enter email: ");
                    String customerEmail = sc.nextLine();

                    String customerSql = "INSERT INTO tbl_PetCare(name, contact, email) VALUES(?, ?, ?)";
                    db.addRecord(customerSql, customerName, customerContact, customerEmail);
                    System.out.println(" Customer added successfully!");
                    break;

                case 2: 
                    System.out.println("\n=== ADD PET AND SERVICE ===");

                    db.viewRecords(
                        "SELECT id, name, contact, email FROM tbl_PetCare ORDER BY id ASC",
                        new String[]{"ID", "Name", "Contact", "Email"},
                        new String[]{"id", "name", "contact", "email"}
                    );

                    System.out.print("\nEnter Customer ID (Pet Owner): ");
                    int customerId = getExistingCustomerID();

                    System.out.print("Enter Pet Name: ");
                    String petName = sc.nextLine();
                    System.out.print("Enter Breed: ");
                    String breed = sc.nextLine();
                    System.out.print("Enter Age: ");
                    String petAge = sc.nextLine();

                    System.out.println("Available Services: Grooming | Vaccination | Checkup");
                    System.out.print("Enter Service Type: ");
                    String serviceType = sc.nextLine();

                    System.out.print("Enter Service Price: ");
                    int price = getValidNumber();

                    String petSql = "INSERT INTO Pet(name, owner_id, breed, age, service, price) VALUES(?, ?, ?, ?, ?, ?)";
                    db.addRecord(petSql, petName, customerId, breed, petAge, serviceType, price);

                    List<Map<String, Object>> cust = db.fetchRecords("SELECT name FROM tbl_PetCare WHERE id = ?", customerId);
                    String ownerName = cust.get(0).get("name").toString();

                    System.out.println("\n--- PET SERVICE RECEIPT ---");
                    System.out.println("Customer: " + ownerName);
                    System.out.println("Pet: " + petName);
                    System.out.println("Service: " + serviceType);
                    System.out.println("Price: ₱" + price);
                    System.out.println("---------------------------");
                    break;

                case 3: 
                    customerAndService();
                    System.out.println("\n=== APPOINTMENT SERVICES ===");
                    System.out.print("Enter Customer ID: ");
                    int custId = getExistingCustomerID();

                    System.out.print("Enter Pet ID: ");
                    int petId = getExistingPetID();

                    System.out.print("Enter Service (Grooming | Vaccination | Checkup): ");
                    String service = sc.nextLine();

                    System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
                    String apptDate = sc.nextLine();

                    System.out.print("Enter Notes (optional): ");
                    String notes = sc.nextLine();

                    String apptSql = "INSERT INTO Appointment (customer_id, pet_id, service, appointment_date, notes) VALUES (?, ?, ?, ?, ?)";
                    db.addRecord(apptSql, custId, petId, service, apptDate, notes);

                    System.out.println(" Appointment created successfully!");
                    break;

                case 4: 
                    System.out.println("\n=== VIEW CUSTOMER, PET, AND SERVICES ===");
                    customerAndService();
                    break;

                case 5: 
                    customerAndService();
                    System.out.println("\n--- UPDATE CUSTOMER ---");
                    System.out.print("Enter Customer ID to Update: ");
                    int custUpdateId = getExistingCustomerID();

                    System.out.print("Enter New Customer Name: ");
                    String newCustName = sc.nextLine();
                    System.out.print("Enter New Contact: ");
                    String newCustContact = sc.nextLine();
                    System.out.print("Enter New Email: ");
                    String newCustEmail = sc.nextLine();

                    String sqlUpdate = "UPDATE tbl_PetCare SET name = ?, contact = ?, email = ? WHERE id = ?";
                    db.updateRecord(sqlUpdate, newCustName, newCustContact, newCustEmail, custUpdateId);
                    System.out.println(" Customer Updated Successfully!");
                    break;

                case 6: 
                    customerAndService();
                    System.out.println("\n--- UPDATE PET AND SERVICE ---");
                    System.out.print("Enter Pet ID to Update: ");
                    int petUpdateId = getExistingPetID();

                    System.out.print("Enter New Pet Name: ");
                    String newPetName = sc.nextLine();
                    System.out.print("Enter New Breed: ");
                    String newBreed = sc.nextLine();
                    System.out.print("Enter New Age: ");
                    String newAge = sc.nextLine();
                    System.out.print("Enter New Service Type: ");
                    String newServiceType = sc.nextLine();
                    System.out.print("Enter New Service Price: ");
                    int newPrice = getValidNumber();

                    String sqlPet = "UPDATE Pet SET name = ?, breed = ?, age = ?, service = ?, price = ? WHERE id = ?";
                    db.updateRecord(sqlPet, newPetName, newBreed, newAge, newServiceType, newPrice, petUpdateId);
                    System.out.println(" Pet Updated Successfully!");
                    break;

                case 7: 
                    System.out.println(" Exiting Staff Dashboard... Returning to Main Menu!");
                    return;

                default:
                    System.out.println(" Invalid option. Please try again.");
            }

            System.out.print("\nDo you want to continue in STAFF DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println(" Exiting Staff Dashboard... Goodbye!");
    }
}
