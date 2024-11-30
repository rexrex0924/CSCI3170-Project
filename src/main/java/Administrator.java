import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
        }
    }

    //Option 1
    public static void create() {
        try {
            Application app = new Application();
            Connection conn = DriverManager.getConnection(app.DBMSLink, app.username, app.password);
            Statement stmt = conn.createStatement();
            String sql1 = "Create TABLE Category(\n" +
                    "    ID integer NOT NULL PRIMARY KEY CHECK ( ID>=1 AND ID<=9),\n" +
                    "    Name VARCHAR(20) NOT NULL\n" +
                    ");";
            String sql2 = "CREATE TABLE Manufacturer(\n" +
                    "    ID integer NOT NULL PRIMARY KEY CHECK ( ID>=1 AND ID<=99),\n" +
                    "    Name varchar(20) not null,\n" +
                    "    Address varchar(50) not null,\n" +
                    "    PhoneNumber integer not null check ( PhoneNumber>=10000000 and  PhoneNumber<=99999999 )\n" +
                    ");";
            String sql3 = "Create table Part(\n" +
                    "    ID integer not null primary key check ( id>=1 and id<=999 ),\n" +
                    "    Name varchar(20) not null,\n" +
                    "    Price integer not null check ( Price>=1 and Price<=99999 ),\n" +
                    "    ManufacturerID integer not null references Manufacturer(ID),\n" +
                    "    CategoryID integer not null references Category(ID),\n" +
                    "    Warranty integer not null check ( Warranty>=10 and Warranty<=99),\n" +
                    "    Available_Quantity integer not null check ( Available_Quantity>=1 and Available_Quantity<=99)\n" +
                    ");";
            String sql4 = "CREATE table Salesperson(\n" +
                    "    ID integer not null primary key check ( id>=1 and id<=99),\n" +
                    "    Name varchar(20) not null,\n" +
                    "    Address varchar(50) not null,\n" +
                    "    PhoneNumber integer not null check (PhoneNumber>=10000000 and  PhoneNumber<=99999999),\n" +
                    "    Experience integer not null check (Experience>=1 and Experience<=9)\n" +
                    ");";
            String sql5 = "create table Transaction(\n" +
                    "    ID integer not null primary key check ( ID>=1 and ID<=9999),\n" +
                    "    PartID integer not null references Part(ID),\n" +
                    "    SalespersonID integer not null references Salesperson(ID),\n" +
                    "    Date date not null\n" +
                    ");";
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.executeUpdate(sql5);
            conn.close();
        }
        catch(Exception e) {
            System.err.println("Error: " + e);
            return;
        }
        System.out.println("Table created successfully!");
    }

    //Option 2
    public static void delete(){
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Are you sure? (Yes or No)");
            if(in.nextLine().equals("No")) {
                return;
            }
            Application app = new Application();
            Connection conn = DriverManager.getConnection(app.DBMSLink, app.username, app.password);
            Statement stmt = conn.createStatement();

            String sql1 = "TRUNCATE TABLE Transaction cascade;";
            String sql2 = "TRUNCATE TABLE Salesperson cascade;";
            String sql3 = "TRUNCATE TABLE Part cascade;";
            String sql4 = "TRUNCATE TABLE Manufacturer cascade;";
            String sql5 = "TRUNCATE TABLE Category cascade;";

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.executeUpdate(sql5);

            sql1 = "DROP TABLE Transaction;";
            sql2 = "DROP TABLE Salesperson;";
            sql3 = "DROP TABLE Part;";
            sql4 = "DROP TABLE Manufacturer;";
            sql5 = "DROP TABLE Category;";

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.executeUpdate(sql5);
            conn.close();
        }
        catch(Exception e) {
            System.err.println("Error: " + e);
            return;
        }
        System.out.println("Table deleted successfully!");
    }

    //Option 3
    public static void load(){
        String path = "xxx.txt";
        Scanner in = new Scanner(System.in);
        System.out.print("Type in the source data path: ");
        path = path.replace("xxx", "src/main/java/" + in.nextLine());

        try{
            Application app = new Application();
            Connection conn = DriverManager.getConnection(app.DBMSLink, app.username, app.password);
            Statement stmt = conn.createStatement();
            String sql;
            in = new Scanner(new File(path));

            if(path.equals("src/main/java/category.txt")){
                String a, b;
                String[] line;
                while(in.hasNext()){
                    line = in.nextLine().split("\t");
                    a = line[0];
                    b = line[1];
                    sql = String.format("INSERT INTO category (id, name) VALUES ('%s', '%s');", a, b);
                    stmt.executeUpdate(sql);
                }
            }

            else if(path.equals("src/main/java/manufacturer.txt")){
                String a, b, c, d;
                String[] line;
                while(in.hasNext()){
                    line = in.nextLine().split("\t");
                    a = line[0];
                    b = line[1];
                    c = line[2];
                    d = line[3];
                    sql = String.format("INSERT INTO manufacturer (id, name, address, phonenumber) VALUES ('%s', '%s', '%s', '%s');", a, b, c, d);
                    stmt.executeUpdate(sql);
                }
            }

            else if(path.equals("src/main/java/part.txt")){
                String a, b, c, d, e, f, g;
                String[] line;
                while(in.hasNext()){
                    line = in.nextLine().split("\t");
                    a = line[0];
                    b = line[1];
                    c = line[2];
                    d = line[3];
                    e = line[4];
                    f = line[5];
                    g = line[6];
                    sql = String.format("INSERT INTO part (id, name, price, manufacturerid, categoryid, warranty, available_quantity)" +
                            " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s');", a, b, c, d, e ,f, g);
                    stmt.executeUpdate(sql);
                }
            }

            else if(path.equals("src/main/java/salesperson.txt")){
                String a, b, c, d, e;
                String[] line;
                while(in.hasNext()){
                    line = in.nextLine().split("\t");
                    a = line[0];
                    b = line[1];
                    c = line[2];
                    d = line[3];
                    e = line[4];
                    sql = String.format("INSERT INTO salesperson (id, name, address, phonenumber, experience)" +
                            " VALUES ('%s', '%s', '%s', '%s', '%s');", a, b, c, d, e);
                    stmt.executeUpdate(sql);
                }
            }

            else if(path.equals("src/main/java/transaction.txt")){
                String a, b, c, d;
                String[] line;
                while(in.hasNext()){
                    line = in.nextLine().split("\t");
                    a = line[0];
                    b = line[1];
                    c = line[2];
                    d = line[3];
                    sql = String.format("INSERT INTO transaction (id, partid, salespersonid, date)" +
                            " VALUES ('%s', '%s', '%s', '%s');", a, b, c, d);
                    stmt.executeUpdate(sql);
                }
            }

            else{
                System.out.println("This is not a valid path!");
                return;
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
            String sql = "select * from xxx;";

            System.out.print("Which table would you like to show: ");
            sql = sql.replace("xxx", in.nextLine());
            ResultSet rs = stmt.executeQuery(sql);

            if(sql.equals("select * from category;")){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    System.out.printf("| %d | %s |", id, name);
                    System.out.println();
                }
            }
            else if(sql.equals("select * from manufacturer;")){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    Integer phonenumber = rs.getInt("phonenumber");
                    System.out.printf("| %d | %s | %s | %d", id, name, address, phonenumber);
                    System.out.println();
                }
            }

            else if(sql.equals("select * from part;")){
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

            else if(sql.equals("select * from salesperson;")){
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

            else if(sql.equals("select * from transaction;")){
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

    //Helper function
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
