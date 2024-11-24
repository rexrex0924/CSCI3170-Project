import java.util.Scanner;

public class Administrator extends Application{

    public void run() {
        Scanner in = new Scanner(System.in);
        int choice = 0;
        while(choice != 5) {
            System.out.print("-----Operations for administrator menu-----\n" +
                    "What kinds of operation would you like to perform?\n" +
                    "1. Create all tables\n" +
                    "2. Delete all tables\n" +
                    "3. Load from datafile\n" +
                    "4. Show content of a table\n" +
                    "5. Return to the main menu\n" +
                    "Enter Your Choice: ");
            choice = in.nextInt();
            System.out.println();
        }
    }

    //Option 1
    public static void create(){

    }

    //Option 2
    public static void delete(){

    }

    //Option 3
    public static void load(){

    }

    //Option 4
    public static void display(){

    }


}
