package UI;
import Engine.*;

import java.util.Map;
import java.util.Scanner;

public class UserInOut extends Menu {
    private static boolean fileIsLoaded;
    private static boolean
    private static final Engine engine = new Engine();
    private static Location type;

    //private static final Graph graph = new Graph();




    public static void runProgram() {
        boolean runProgram = true;
        Scanner sc = new Scanner(System.in);
        int userChoice;


        System.out.println("\n -- Welcome To G.P.U.P -- ");

        while (runProgram) {
            try {
                System.out.print("\n");
                printMenu();
                userChoice = sc.nextInt();
                switch (userChoice) {
                    case 1:
                        fileIsLoaded = fileLoad();
                        break;
                    case 2:
                        if (!fileIsLoaded) {
                            System.out.println(" -- Please load file first -- ");
                            break;
                        }
                        generalInfo();
                        break;
                    case 3:
                        if (!fileIsLoaded) {
                            System.out.println(" -- Please load file first -- ");
                            break;
                        }
                        targetsInfo();
                        break;
                    case 4:
                        if (!fileIsLoaded) {
                            System.out.println(" -- Please load file first -- ");
                            break;
                        }
                        PathBetweenTargets();
                        break;
                    case 5:
                        break;
                    case 0:
                        runProgram = false;
                        break;
                    default:
                        System.out.println("\n -- Please choose a number from the menu (0-5) --");
                        break;
                }
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(e.toString());
                sc.nextLine();
            }
        }
        System.out.println(" -- Goodbye! -- \n");
    }

    private static boolean fileLoad() {
        // TODO throws - bad file load
        // TODO call file load func

        String filename = "**TEST**";

        System.out.println(" -- The file " + filename + " has been loaded --");
        return true;
    }

    private static void generalInfo() {

        int numOfTargets = engine.getAmountOfTargets();
        Map<Location, Integer> numOfTypes = engine.howManyTargetsInEachLocation();

        System.out.println("There is " + numOfTargets + " targets");
        System.out.println(numOfTypes.get(type.LEAF) + " leaves");
        System.out.println(numOfTypes.get(type.MIDDLE) + " middles");
        System.out.println(numOfTypes.get(type.ROOT) + " roots");
        System.out.println(numOfTypes.get(type.INDEPENDENT) + " independents");
    }

    private static void targetsInfo() {
        boolean targetInFile, stayInTargetInfo;
        Scanner sc = new Scanner(System.in);
        TargetDTO target = new TargetDTO();

        System.out.print(" enter target name: ");
        String targetName = sc.nextLine();
        //  targetInFile =
        //         engine.getTargetDataTransferObjectByName(targetName);

        while (!targetInFile) {
            System.out.println(" -- The target you entered is not in the database --");

            stayInTargetInfo = keepTryingInput();
            if (!stayInTargetInfo) {
                return;
            } else {
                System.out.println(" enter target name: ");
                targetName = sc.nextLine();
                targetInFile = graph.isTargetInGraphByName(targetName);
            }
        }
        //print name and type
        System.out.println("name: " + targetName);
        System.out.println("type: " + graph.getLocationByName(targetName).toString());
        //print dependent targets
        System.out.println("depends: " + graph.getDependsOnByName(targetName).toString());
        //print required targets
        System.out.println("required: " + graph.getRequiredForByName(targetName).toString());
        //print more info about the target
        String targetInfo = graph.getInfoByName(targetName);
        if (targetInfo != null)
            System.out.println("info: " + targetInfo);

    }

    private static void pathBetweenTargets() {
        Scanner sc = new Scanner(System.in);
        String srcTargetName, destTargetName;
        boolean srcTargetExist, destTargetExist, stillTry;


        System.out.println("Enter the name of the first target: ");
        srcTargetName = sc.nextLine();
        System.out.println("Enter the name of the second target: ");
        destTargetName = sc.nextLine();
        srcTargetExist = graph.isTargetInGraphByName(srcTargetName);
        destTargetExist = graph.isTargetInGraphByName(destTargetName);
        System.out.println(" ");

        while (!srcTargetExist || !destTargetExist) {

            if (!srcTargetExist && !destTargetExist) {
                System.out.print(" -- The targets '" + srcTargetName + "' and '" + destTargetName + "' are NOT exist in the database --\n\n");
            } else {
                System.out.print(" -- The target '");
                if (!srcTargetExist)
                    System.out.print(srcTargetName);
                if (!destTargetExist)
                    System.out.print(destTargetName);
                System.out.print("' is NOT exist in the database --\n\n");
            }

            stillTry = keepTryingInput();
            if (!stillTry)
                return;

            System.out.println("Enter the name of the first target: ");
            srcTargetName = sc.nextLine();
            System.out.println("Enter the name of the second target: ");
            destTargetName = sc.nextLine();

            srcTargetExist = graph.isTargetInGraphByName(srcTargetName);
            destTargetExist = graph.isTargetInGraphByName(destTargetName);
        }

        //TODO fix the type
        graph.getPathBetweenTargets(srcTargetName, destTargetName, 1);
    }



    //TODO finish simul, print the res, create log
    private static void runSimulation() {
        Scanner sc = new Scanner(System.in);
        int runTime, tempMenuChoice = -1;
        float probabilityForSuccess, probabilityForSuccessWarnings;
        boolean randomRunTime;

        // get simulation time per target
        System.out.print(" enter the simulation time per target (in ms): ");
        runTime = sc.nextInt();

        switch(randomRunTime(runTime)) {
            case 9:
                return;
            case 1:
                randomRunTime = false;
            case 2:
                randomRunTime = true;
        }
        probabilityForSuccess = getProbabilityToSuccess();
        probabilityForSuccessWarnings = getProbabilityToSuccessWarnings();

        /*
        * מידעים שיש להדפיס במהלך ריצת task על כל target:
•	מידע על הזמן שה task הולך "לישון"
•	שורת מידע לפני "השינה".
•	שורת מידע אחרי "השינה"
        */

       }

    // get how to run the simulation (fixed or random processing time)
    private static int randomRunTime(int runTime) {
        int MenuChoice = -1;
        Scanner sc = new Scanner(System.in);

        while (!(MenuChoice == 1 || MenuChoice == 2 || MenuChoice == 0)) {
            sc.nextLine();
            System.out.println("\n what would you prefer? ");
            System.out.println(" 1. fixed processing time - " + runTime + " ms per target");
            System.out.println(" 2. random processing time - up to " + runTime + " ms");
            System.out.println(" 0. cancel and return to the main menu");
            System.out.print(" enter your choice: ");
            MenuChoice = sc.nextInt();
        }
        return MenuChoice;
    }
    // get from user the probability to success
    private static float getProbabilityToSuccess() {
        float res = -1;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n in what probability to success do you want the simulation to run? ");
        System.out.println(" 1   --> all the targets will success");
        System.out.println(" 0.5 --> half will success and half will fail");
        System.out.println(" 0   --> all will fail");
        System.out.print(" enter any number between 0 to 1: ");
        res = sc.nextInt();
        while (res < 0 && res > 1) {
            if (res == 9)
                return 9;
            System.out.println("\n if you prefer to return to the main menu enter '9'");
            System.out.println(" the probability to success has to be a number between 0 to 1");
            System.out.print(" enter your choice: ");
        }
        return res;

    }
    // get probability it's a success with warnings
    private static float getProbabilityToSuccessWarnings() {
        float res = -1;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n if the task succeeded, what the probability it's a success with warnings? ");
        System.out.println(" 1   --> it's a success with warnings");
        System.out.println(" 0   --> it's a success without warnings");
        System.out.print(" enter any number between 0 to 1: ");
        res = sc.nextInt();
        while (res < 0 && res > 1) {
            if (res == 9)
                return 9;
            System.out.println("\n if you prefer to return to the main menu enter '9'");
            System.out.println(" the probability to success with warnings has to be a number between 0 to 1");
            System.out.print(" enter your choice: ");
        }
        return res;
    }
}


