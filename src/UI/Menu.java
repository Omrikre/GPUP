package UI;

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

    public static void printTargetList() {

        System.out.println(" Please choose from the following options:");
        System.out.println(" 1. Load file");
        System.out.print(" Your choice: ");
    }

}

