import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Administrator extends Application{

    public void run() {
        Scanner in = new Scanner(System.in);
        int choice;
        try {
            print();
            choice = in.nextInt();
            while (choice != 5) {
                if (choice < 1 || choice > 5) {
                    System.out.println("Please enter a number between 1 and 5!");
                } else if (choice == 1) {
                    create();
                } else if (choice == 2) {
                    delete();
                } else if (choice == 3) {
                    load();
                } else {
                    display();
                }
                print();
                choice = in.nextInt();
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Please enter a number!\n");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Option 1: Create tables
    public void create() {
        try (Connection conn = DriverManager.getConnection(DBMSLink, username, password);
             Statement stmt = conn.createStatement()) {
            // Combined SQL statements for creating tables
            stmt.executeUpdate("CREATE TABLE Category (ID INT PRIMARY KEY CHECK (ID BETWEEN 1 AND 9), Name VARCHAR(20) NOT NULL)");
            stmt.executeUpdate("CREATE TABLE Manufacturer (ID INT PRIMARY KEY CHECK (ID BETWEEN 1 AND 99), Name VARCHAR(20) NOT NULL, Address VARCHAR(50) NOT NULL, PhoneNumber INT NOT NULL CHECK (PhoneNumber BETWEEN 10000000 AND 99999999))");
            stmt.executeUpdate("CREATE TABLE Part (ID INT PRIMARY KEY CHECK (ID BETWEEN 1 AND 999), Name VARCHAR(20) NOT NULL, Price INT NOT NULL CHECK (Price BETWEEN 1 AND 99999), ManufacturerID INT REFERENCES Manufacturer(ID), CategoryID INT REFERENCES Category(ID), Warranty INT NOT NULL CHECK (Warranty BETWEEN 10 AND 99), Available_Quantity INT NOT NULL CHECK (Available_Quantity BETWEEN 1 AND 99))");
            stmt.executeUpdate("CREATE TABLE Salesperson (ID INT PRIMARY KEY CHECK (ID BETWEEN 1 AND 99), Name VARCHAR(20) NOT NULL, Address VARCHAR(50) NOT NULL, PhoneNumber INT NOT NULL CHECK (PhoneNumber BETWEEN 10000000 AND 99999999), Experience INT NOT NULL CHECK (Experience BETWEEN 1 AND 9))");
            stmt.executeUpdate("CREATE TABLE Transaction (ID INT PRIMARY KEY CHECK (ID BETWEEN 1 AND 9999), PartID INT REFERENCES Part(ID), SalespersonID INT REFERENCES Salesperson(ID), \"date\" DATE NOT NULL)");
            System.out.println("Tables created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    // Option 2: Delete tables
    public void delete() {
        Scanner in = new Scanner(System.in);
        System.out.println("Are you sure? (Yes or No)");
        if (in.nextLine().equals("No")) {
            return;
        }

        try{
            Connection conn = DriverManager.getConnection(DBMSLink, username, password);
            Statement stmt = conn.createStatement();
            String databaseProductName = conn.getMetaData().getDatabaseProductName(); // Get database name

            String cascadeSyntax = "";
            if (databaseProductName.equalsIgnoreCase("Oracle"))
                cascadeSyntax = "CASCADE CONSTRAINTS";
             else if (databaseProductName.equalsIgnoreCase("PostgreSQL"))
                cascadeSyntax = "CASCADE";

            stmt.executeUpdate("DROP TABLE Transaction " + cascadeSyntax);
            stmt.executeUpdate("DROP TABLE Salesperson " + cascadeSyntax);
            stmt.executeUpdate("DROP TABLE Part " + cascadeSyntax);
            stmt.executeUpdate("DROP TABLE Manufacturer " + cascadeSyntax);
            stmt.executeUpdate("DROP TABLE Category " + cascadeSyntax);
            System.out.println("Tables deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }

    //Option 3
    public static void load(){
        String path = "xxx.txt";
        Scanner in = new Scanner(System.in);
        System.out.print("Type in the source data path: ");
        path = path.replace("xxx", "src/main/java/sample_data/" + in.nextLine());

        try{
            Application app = new Application();
            Connection conn = DriverManager.getConnection(app.DBMSLink, app.username, app.password);
            Statement stmt = conn.createStatement();
            String sql;
            in = new Scanner(new File(path));

            switch (path) {
                case "src/main/java/sample_data/category.txt" -> {
                    String a, b;
                    String[] line;
                    while (in.hasNext()) {
                        line = in.nextLine().split("\t");
                        a = line[0];
                        b = line[1];
                        sql = String.format("INSERT INTO category (id, name) VALUES ('%s', '%s')", a, b);
                        stmt.executeUpdate(sql);
                    }
                }
                case "src/main/java/sample_data/manufacturer.txt" -> {
                    String a, b, c, d;
                    String[] line;
                    while (in.hasNext()) {
                        line = in.nextLine().split("\t");
                        a = line[0];
                        b = line[1];
                        c = line[2];
                        d = line[3];
                        sql = String.format("INSERT INTO manufacturer (id, name, address, phonenumber) VALUES ('%s', '%s', '%s', '%s')", a, b, c, d);
                        stmt.executeUpdate(sql);
                    }
                }
                case "src/main/java/sample_data/part.txt" -> {
                    String a, b, c, d, e, f, g;
                    String[] line;
                    while (in.hasNext()) {
                        line = in.nextLine().split("\t");
                        a = line[0];
                        b = line[1];
                        c = line[2];
                        d = line[3];
                        e = line[4];
                        f = line[5];
                        g = line[6];
                        sql = String.format("INSERT INTO part (id, name, price, manufacturerid, categoryid, warranty, available_quantity)" +
                                " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", a, b, c, d, e, f, g);
                        stmt.executeUpdate(sql);
                    }
                }
                case "src/main/java/sample_data/salesperson.txt" -> {
                    String a, b, c, d, e;
                    String[] line;
                    while (in.hasNext()) {
                        line = in.nextLine().split("\t");
                        a = line[0];
                        b = line[1];
                        c = line[2];
                        d = line[3];
                        e = line[4];
                        sql = String.format("INSERT INTO salesperson (id, name, address, phonenumber, experience)" +
                                " VALUES ('%s', '%s', '%s', '%s', '%s')", a, b, c, d, e);
                        stmt.executeUpdate(sql);
                    }
                }
                case "src/main/java/sample_data/transaction.txt" -> {
                    sql = "INSERT INTO transaction (ID, PARTID, SALESPERSONID, \"date\") VALUES (?, ?, ?, TO_DATE(?, 'DD/MM/YYYY'))";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        while (in.hasNextLine()) {
                            String[] line = in.nextLine().split("\t");
                            pstmt.setInt(1, Integer.parseInt(line[0]));
                            pstmt.setInt(2, Integer.parseInt(line[1]));
                            pstmt.setInt(3, Integer.parseInt(line[2]));
                            pstmt.setString(4, line[3]);
                            pstmt.executeUpdate();
                        }
                    }
                }
                default -> {
                    System.out.println("This is not a valid path!");
                    return;
                }
            }
        }

        catch (Exception e){
            System.err.println("Error: " + e);
            return;
        }

        System.out.println("File loaded successfully!");
    }

    //Option 4
    public static void display(){
        try{
            Application app = new Application();
            Connection conn = DriverManager.getConnection(app.DBMSLink, app.username, app.password);
            Statement stmt = conn.createStatement();
            Scanner in = new Scanner(System.in);
            String sql = "select * from xxx";

            System.out.print("Which table would you like to show: ");
            sql = sql.replace("xxx", in.nextLine());
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);

            if(sql.equals("select * from category")){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    System.out.printf("| %d | %s |", id, name);
                    System.out.println();
                }
            }
            else if(sql.equals("select * from manufacturer")){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    Integer phonenumber = rs.getInt("phonenumber");
                    System.out.printf("| %d | %s | %s | %d", id, name, address, phonenumber);
                    System.out.println();
                }
            }

            else if(sql.equals("select * from part")){
                while(rs.next()){
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    Integer price = rs.getInt("price");
                    Integer manufacturerid = rs.getInt("manufacturerid");
                    Integer categoryid = rs.getInt("categoryid");
                    Integer warranty = rs.getInt("warranty");
                    Integer available_quantity = rs.getInt("available_quantity");
                    System.out.printf("| %d | %s | %d | %d | %d | %d | %d",
                            id, name, price, manufacturerid, categoryid, warranty, available_quantity);
                    System.out.println();
                }
            }

            else if(sql.equals("select * from salesperson")){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    Integer phonenumber = rs.getInt("phonenumber");
                    Integer experience = rs.getInt("experience");
                    System.out.printf("| %d | %s | %s | %d | %d", id, name, address, phonenumber, experience);
                    System.out.println();
                }
            }

            else if(sql.equals("select * from transaction")){
                while(rs.next()){
                    Integer id = rs.getInt("id");
                    Integer partid = rs.getInt("partid");
                    Integer salespersonid = rs.getInt("salespersonid");
                    Date date = rs.getDate("date");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = formatter.format(date);
                    System.out.printf("| %d | %d | %d | %s", id, partid, salespersonid, dateString);
                    System.out.println();
                }
            }
        }
        catch(Exception e){
            System.err.println("Error: " + e);
        }
    }

    //Helper functions
    private static void print() {
        System.out.print("\n-----Operations for administrator menu-----\n" +
                "What kinds of operation would you like to perform?\n" +
                "1. Create all tables\n" +
                "2. Delete all tables\n" +
                "3. Load from datafile\n" +
                "4. Show content of a table\n" +
                "5. Return to the main menu\n" +
                "Enter Your Choice: ");
    }

}
