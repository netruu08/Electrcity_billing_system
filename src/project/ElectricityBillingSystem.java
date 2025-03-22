package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class ElectricityBillingSystem {


	    // PostgreSQL database credentials
	    private static final String URL = "jdbc:postgresql://localhost:5432/electricdb"; // Adjust as per your setup
	    private static final String USER = "postgres";  // Replace with your PostgreSQL username
	    private static final String PASSWORD = "123"; // Replace with your PostgreSQL password

	    // Method to connect to PostgreSQL database
	    public static Connection connect() {
	        try {
	            return DriverManager.getConnection(URL, USER, PASSWORD);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // Method to add a new customer to the database
	    public static void addCustomer(String name, String address, String meterNumber) {
	        String query = "INSERT INTO customer (name, address, meter_number) VALUES (?, ?, ?)";
	        try (Connection connection = connect(); 
	        PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, name);
	            statement.setString(2, address);
	            statement.setString(3, meterNumber);
	            statement.executeUpdate();
	            System.out.println("Customer added successfully.");
	        } 
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Method to calculate the bill based on units consumed
	    public static double calculateBill(int unitsConsumed) {
	        double billAmount = 0;
	        if (unitsConsumed <= 100) {
	            billAmount = unitsConsumed * 5;
	        } else if (unitsConsumed <= 300) {
	            billAmount = (100 * 5) + ((unitsConsumed - 100) * 6);
	        } else {
	            billAmount = (100 * 5) + (200 * 6) + ((unitsConsumed - 300) * 7);
	        }
	        return billAmount;
	    }

	    // Method to add billing details to the database
	    public static void addBilling(int customerId, int unitsConsumed, double billAmount) {
	        String query = "INSERT INTO billing (customer_id, units_consumed, amount) VALUES (?, ?, ?)";
	        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, customerId);
	            statement.setInt(2, unitsConsumed);
	            statement.setDouble(3, billAmount);
	            statement.executeUpdate();
	            System.out.println("Billing details added successfully.");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Method to display the billing history for a specific customer
	    public static void displayBillingHistory(int customerId) {
	        String query = "SELECT * FROM billing WHERE customer_id = ? ORDER BY billing_date DESC";
	        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, customerId);
	            ResultSet resultSet = statement.executeQuery();
	            while (resultSet.next()) {
	                int billingId = resultSet.getInt("billing_id");
	                int unitsConsumed = resultSet.getInt("units_consumed");
	                double amount = resultSet.getDouble("amount");
	                Timestamp billingDate = resultSet.getTimestamp("billing_date");

	                System.out.println("Billing ID: " + billingId);
	                System.out.println("Units Consumed: " + unitsConsumed);
	                System.out.println("Amount: ₹" + amount);
	                System.out.println("Billing Date: " + billingDate);
	                System.out.println("-------------------------");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	 // Method to update customer details
	    public static void updateCustomer(int customerId, String newName, String newAddress, String newMeterNumber) {
	        String query = "UPDATE customer SET name = ?, address = ?, meter_number = ? WHERE customer_id = ?";
	        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, newName);
	            statement.setString(2, newAddress);
	            statement.setString(3, newMeterNumber);
	            statement.setInt(4, customerId);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Customer details updated successfully.");
	            } else {
	                System.out.println("No customer found with ID " + customerId);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	 // Method to update billing details
	    public static void updateBilling(int billingId, int newUnitsConsumed, double newAmount) {
	        String query = "UPDATE billing SET units_consumed = ?, amount = ? WHERE billing_id = ?";
	        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, newUnitsConsumed);
	            statement.setDouble(2, newAmount);
	            statement.setInt(3, billingId);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Billing record updated successfully.");
	            } else {
	                System.out.println("No billing record found with ID " + billingId);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	   
	    
	 // Method to delete a billing record from the database
	    public static void deleteBilling(int billingId) {
	        String query = "DELETE FROM billing WHERE billing_id = ?";
	        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, billingId);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Billing record deleted successfully.");
	            } else {
	                System.out.println("No billing record found with ID " + billingId);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    // Main method to interact with the user
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        while (true) {
	            System.out.println("Electricity Billing System");
	            System.out.println("1. Add Customer");
	            System.out.println("2. Add Billing");
	            System.out.println("3. Display Billing History");
	            System.out.println("4. Update Customer");
	            System.out.println("5. Update Billing");
	            System.out.println("6. Delete Billing");
	            System.out.println("7. Exit");
	            System.out.print("Enter your choice: ");
	            int choice = scanner.nextInt();

	            if (choice == 1) {
	                // Add a new customer
	                scanner.nextLine(); // Consume newline
	                System.out.print("Enter Customer Name: ");
	                String name = scanner.nextLine();
	                System.out.print("Enter Customer Address: ");
	                String address = scanner.nextLine();
	                System.out.print("Enter Meter Number: ");
	                String meterNumber = scanner.nextLine();
	                addCustomer(name, address, meterNumber);
	            } else if (choice == 2) {
	                // Add billing information
	                System.out.print("Enter Customer ID: ");
	                int customerId = scanner.nextInt();
	                System.out.print("Enter Units Consumed: ");
	                int unitsConsumed = scanner.nextInt();
	                double billAmount = calculateBill(unitsConsumed);
	                addBilling(customerId, unitsConsumed, billAmount);
	            } else if (choice == 3) {
	                // Display billing history for a customer
	                System.out.print("Enter Customer ID to view billing history: ");
	                int customerId = scanner.nextInt();
	                displayBillingHistory(customerId);
	            } else if (choice == 4) {
	                // Update customer details
	                System.out.print("Enter Customer ID to update: ");
	                int customerId = scanner.nextInt();
	                scanner.nextLine(); // Consume newline
	                System.out.print("Enter new Customer Name: ");
	                String newName = scanner.nextLine();
	                System.out.print("Enter new Customer Address: ");
	                String newAddress = scanner.nextLine();
	                System.out.print("Enter new Meter Number: ");
	                String newMeterNumber = scanner.nextLine();
	                updateCustomer(customerId, newName, newAddress, newMeterNumber);
	            } else if (choice == 5) {
	                // Update billing details
	                System.out.print("Enter Billing ID to update: ");
	                int billingId = scanner.nextInt();
	                System.out.print("Enter new Units Consumed: ");
	                int newUnitsConsumed = scanner.nextInt();
	                double newAmount = calculateBill(newUnitsConsumed);
	                updateBilling(billingId, newUnitsConsumed, newAmount);
	            }  else if (choice == 6) {
	                // Delete a billing record
	                System.out.print("Enter Billing ID to delete: ");
	                int billingId = scanner.nextInt();
	                deleteBilling(billingId);
	            } else if (choice == 7) {
	                // Exit the program
	                System.out.println("Exiting the system...");
	                break;
	            } else {
	                System.out.println("Invalid choice. Please try again.");
	            }
	        }

	        scanner.close();
	    }
	}
