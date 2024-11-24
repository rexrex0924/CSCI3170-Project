import java.util.Scanner;

public class Salesperson extends Application{

    public void run() {
        Scanner in = new Scanner(System.in);
        int choice = 0;
        while(choice != 3) {
            System.out.print("-----Operations for salesperson menu-----\n" +
                    "What kinds of operation would you like to perform?\n" +
                    "1. Search for parts\n" +
                    "2. Sell a part\n" +
                    "3. Return to the main menu\n" +
                    "Enter your choice: ");
            choice = in.nextInt();
            System.out.println();
        }
    }

    //Option 1
    public static void search(){

    }

    //Option 2
    public static void sell(){

    }

}
