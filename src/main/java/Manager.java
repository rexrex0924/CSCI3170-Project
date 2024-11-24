import java.util.Scanner;

public class Manager extends Application{

    public void run() {
        int choice = 0;
        Scanner in = new Scanner(System.in);
        while(choice != 5) {
            System.out.print("-----Operations for manager menu-----\n" +
                    "What kinds of operation would you like to perform?\n" +
                    "1. List all salespersons\n" +
                    "2. Count the no. of sales record of each salesperson under a specific range on years of experience\n" +
                    "3. Show the total sales value of each manufacturer\n" +
                    "4. Show the N most popular part\n" +
                    "5. Return to the main menu\n" +
                    "Enter your choice: ");
            choice = in.nextInt();
            System.out.println();
        }
    }

    //Option 1
    public static void list(){

    }

    //Option 2
    public static void countNoOfSales(){

    }

    //Option 3
    public static void showTotal(){

    }

    //Option 4
    public static void showNMostPopular(){

    }

}
