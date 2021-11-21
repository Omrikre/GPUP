package UI;
import java.util.Scanner;

public class Menu {

    public static void printMenu() {
        System.out.println(" Please choose from the following options:");
        System.out.println(" 1. Load graph from an XML file");
        System.out.println(" 2. Show graph's general info");
        System.out.println(" 3. Show target's info");
        System.out.println(" 4. Find route between two targets");
        System.out.println(" 5. Run simulation");
        System.out.println(" 6. Find if a target is part of a cycle");
        System.out.println(" 0. Exit\n");
        System.out.print(" Your choice: ");
    }
    public static boolean keepTryingInput() {
        Scanner sc = new Scanner(System.in);
        String keepTryingString;

        System.out.print("\n do you want to try again? (y - yes | n - back to menu): ");
        while(true) {
            keepTryingString = sc.nextLine();
            switch (keepTryingString) {
                case "n":
                case "N":
                    System.out.println(" ");
                    return false;
                case "y":
                case "Y":
                    System.out.println(" ");
                    return true;
                default:
                    System.out.println("\n -- enter y to try again OR n to go back to the menu -- ");
                    System.out.print("    y / n : ");
                    break;
            }
        }
    }
    public static void printHeader() {
        System.out.println("\n\n" +
                "__           __  _                            _______    \n" +
                "\\ \\    _    / / | |                          |__   __|   \n" +
                " \\ \\  / \\  / /__| | ___ ___  _ __ ___   ___     | | ___  \n" +
                "  \\ \\/ _ \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\    | |/ _ \\ \n" +
                "   \\  / \\  /  __/ | (_| (_) | | | | | |  __/    | | (_) |\n" +
                "    \\/   \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|    |_|\\___/ \n");
        System.out.println("" +
                "             _____   _____  _    _  _____  \n" +
                "            / ____| |  __ \\| |  | ||  __ \\ \n" +
                "           | |  __  | |__) | |  | || |__) |\n" +
                "           | | |_ | |  ___/| |  | ||  ___/ \n" +
                "           | |__| |_| |   _| |__| || |     \n" +
                "            \\_____(_)_|  (_)\\____(_)_|     \n" +
                "                                   \n");

    }

}

