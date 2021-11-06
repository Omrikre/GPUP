package UI;

import java.util.Scanner;

import static java.sql.DriverManager.println;

public class Menu {

    public static void printMenu() {
        System.out.println(" Please choose from the following options:");
        System.out.println(" 1. Load file");
        System.out.println(" 2. Show general info");
        System.out.println(" 3. Show target's info");
        System.out.println(" 4. Find route between targets");
        System.out.println(" 5. Run simulation");
        System.out.println(" 0. Exit\n");
        System.out.print(" Your choice: ");
    }

    public static boolean keepTryingInput() {
        Scanner sc = new Scanner(System.in);
        String keepTryingString;

        System.out.print(" do you want to try again? (y - yes | n - back to menu): ");
        while(true) {
            keepTryingString = sc.nextLine();
            switch (keepTryingString) {
                case "n":
                case "N":
                    return false;
                case "y":
                case "Y":
                    return true;
                default:
                    System.out.println(" -- enter y to try again OR n to go back to the menu -- ");
                    System.out.print(" y / n : ");
                    break;
            }
        }
    }
}

