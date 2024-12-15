import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Manager extends Application {

    public void run() {
        int choice = 0;
        Scanner in = new Scanner(System.in);
        while (choice != 5) {
            try {
                System.out.println();
                System.out.print("-----Operations for manager menu-----\n" +
                        "What kinds of operation would you like to perform?\n" +
                        "1. List all salespersons\n" +
                        "2. Count the no. of sales record of each salesperson under a specific range on years of experience\n" +
                        "3. Show the total sales value of each manufacturer\n" +
                        "4. Show the N most popular part\n" +
                        "5. Return to the main menu\n" +
                        "Enter your choice: ");
                choice = in.nextInt();
                switch (choice) {
                    case 1:
                        list();
                        break;
                    case 2:
                        countNoOfSales();
                        break;
                    case 3:
                        showTotal();
                        break;
                    case 4:
                        showNMostPopular();
                        break;
                    case 5:
                        System.out.println("Returning to main menu.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                in.next();
            }
        }
    }

    //Option 1
    public void list() {
        try (Connection conn = DriverManager.getConnection(DBMSLink, username, password)) {
            Statement stmt = conn.createStatement();
            int choice = 0;
            String order = "";
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.print("Choose ordering:\n" +
                        "1. By ascending order\n" +
                        "2. By descending order\n" +
                        "Choose the list ordering: ");

                if (in.hasNextInt()) {
                    choice = in.nextInt();
                    if (choice == 1) {
                        order = "ASC";
                        break;
                    } else if (choice == 2) {
                        order = "DESC";
                        break;
                    } else {
                        System.out.print("Please enter 1 or 2.\n");
                    }
                } else {
                    System.out.print("Invalid input. Please enter 1 or 2.\n");
                    in.next(); 
                }
            }
            ResultSet rs = stmt.executeQuery("SELECT ID, Name, PhoneNumber, Experience FROM Salesperson ORDER BY Experience " + order);
            System.out.println("List of Salespersons:");
            System.out.println("| ID | Name | Mobile Phone | Years of Experience |");
            while (rs.next()) {
                System.out.println("| " + rs.getInt("ID") + " | " + rs.getString("Name") + " | " +
                        + rs.getInt("PhoneNumber") + " | " + rs.getInt("Experience") + " | " );
            }
            System.out.println("End of Query");
        } catch (SQLException e) {
            System.err.println("Error listing salespersons: " + e.getMessage());
        }
    }


    //Option 2
    public void countNoOfSales() {
        try (Connection conn = DriverManager.getConnection(DBMSLink, username, password)){
            Scanner in = new Scanner(System.in);
            int minExp, maxExp;
            while(true) {
                System.out.print("Type in the lower bound for years of experience: ");
                if(in.hasNextInt()) {
                    if ((minExp = in.nextInt()) < 0){
                        System.out.println("Please enter a non negative integer");
                        continue;
                    }
                    break;
                }
                else{
                    System.out.println("Please enter an integer.");
                    in.next();
                }
            }
            while(true) {
                System.out.print("Type in the upper bound for years of experience: ");
                if(in.hasNextInt()) {
                    if ((maxExp = in.nextInt()) < 0){
                        System.out.println("Please enter a non negative integer");
                        continue;
                    }
                    break;
                }
                else{
                    System.out.println("Please enter an integer.");
                    in.next();
                }
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT s.ID, s.Name, Experience, COUNT(t.ID) AS NUM_OF_TRAN " +
                            "FROM Salesperson s LEFT JOIN Transaction t ON s.ID = t.SalespersonID " +
                            "WHERE s.Experience BETWEEN ? AND ? " +
                            "GROUP BY s.ID, s.Name, Experience " +
                            "ORDER BY s.ID DESC");
            stmt.setInt(1, minExp);
            stmt.setInt(2, maxExp);

            ResultSet rs = stmt.executeQuery();
            System.out.println("Transaction Record:");
            System.out.println("| ID | Name | Years of Experience | Number of Transaction | ");
            while (rs.next()) {
                System.out.println("| " + rs.getInt("ID") + " | " + rs.getString("Name") +
                        " | " + rs.getInt("Experience") + " | " + rs.getInt("NUM_OF_TRAN"));
            }
            System.out.println("End of Query");
        }
        catch (SQLException e) {
            System.err.println("Error counting sales records: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numbers for experience range.");
        }
    }

    //Option 3
    public void showTotal() {
        try (Connection conn = DriverManager.getConnection(DBMSLink, username, password)){
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT ManufacturerID, Name, SUM(Price) AS Total FROM part P, transaction T " +
                             "WHERE P.ID = T.PartID " +
                             "GROUP BY ManufacturerID, Name " +
                             "ORDER BY TOTAL DESC;");
             System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
             while (rs.next()) {
                System.out.println("| " + rs.getInt("ManufacturerID") + " | " +
                        rs.getString("Name") + " | " + rs.getInt("Total") + " | " );
             }
             System.out.println("End of Query");
        } catch (SQLException e) {
            System.err.println("Error showing total sales value: " + e.getMessage());
        }
    }


    //Option 4
    public void showNMostPopular() {
        try (Connection conn = DriverManager.getConnection(DBMSLink, username, password)){
            Scanner in = new Scanner(System.in);
            int n;
            while(true) {
                System.out.print("Type in the number of parts: ");
                if (in.hasNextInt()) {
                    if ((n = in.nextInt()) <= 0) {
                        System.out.println("N should be a positive integer.");
                        continue;
                    }
                    break;
                }
                else{
                    System.out.println("Please enter an integer.");
                    in.next();
                }
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT PartID, Name, COUNT(*) AS Count FROM part P, transaction T " +
                            "WHERE P.ID = T.PartID " +
                            "GROUP BY PartID, Name " +
                            "ORDER BY COUNT DESC " +
                            "LIMIT ?;");

            stmt.setInt(1, n);
            ResultSet rs = stmt.executeQuery();
            System.out.println("| Part ID | Part Name | No. of Transaction |");
            while (rs.next()) {
                System.out.println("| " + rs.getInt("PartID") + " | " + rs.getString("Name") + " | " + rs.getInt("Count") + " |");
            }
            System.out.println("End Of Query");
        } catch (SQLException e) {
            System.err.println("Error showing N most popular parts: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number for N.");
        }
    }
}
