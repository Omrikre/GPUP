package UI;
import Engine.*;

import java.util.Map;
import java.util.Scanner;

public class UserInOut extends Menu {
    private static boolean fileIsLoaded;
    private static final Graph graph = new Graph();
    private static Location type;

    public static void runProgram() {
        boolean runProgram = true;
        Scanner sc = new Scanner(System.in);
        int userChoice;


        System.out.println("\n -- Welcome To G.P.U.P -- ");

            while(runProgram) {
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
                }
                catch(Exception e) {
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

        int numOfTargets = graph.getAmountOfTargets();
        Map<Location, Integer> numOfTypes = graph.howManyTargetsInEachLocation();

        System.out.println("There is " + numOfTargets + " targets");
        System.out.println(numOfTypes.get(type.LEAF) + " leaves");
        System.out.println(numOfTypes.get(type.MIDDLE) + " middles");
        System.out.println(numOfTypes.get(type.ROOT) + " roots");
        System.out.println(numOfTypes.get(type.INDEPENDENT) + " independents");
    }
    private static void targetsInfo() {
        boolean targetInFile, stayInTargetInfo;
        Scanner sc = new Scanner(System.in);

        System.out.print(" enter target name: ");
        String targetName = sc.nextLine();
        targetInFile = graph.doesTargetExistByName(targetName);

        while (!targetInFile) {
            System.out.println(" -- The target you entered is not in the database --");

            stayInTargetInfo = keepTryingInput();
            if (!stayInTargetInfo) {
                return;
            } else {
                System.out.println(" enter target name: ");
                targetName = sc.nextLine();
                targetInFile = graph.doesTargetExistByName(targetName);
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
            if(targetInfo != null)
                System.out.println("info: " + targetInfo);

    }
    private static void PathBetweenTargets() {
        Scanner sc = new Scanner(System.in);
        String srcTargetName, destTargetName;
        boolean srcTargetExist, destTargetExist, stillTry;


        System.out.println("Enter the name of the first target: ");
        srcTargetName = sc.nextLine();
        System.out.println("Enter the name of the second target: ");
        destTargetName = sc.nextLine();
        srcTargetExist = graph.doesTargetExistByName(srcTargetName);
        destTargetExist = graph.doesTargetExistByName(destTargetName);
        System.out.println(" ");

        while(!srcTargetExist || !destTargetExist) {

            if(!srcTargetExist && !destTargetExist) {
                System.out.print(" -- The targets '" + srcTargetName + "' and '" + destTargetName + "' are NOT exist in the database --\n\n");
            }
            else {
                System.out.print(" -- The target '");
                if(!srcTargetExist)
                    System.out.print(srcTargetName);
                if(!destTargetExist)
                    System.out.print(destTargetName);
                System.out.print("' is NOT exist in the database --\n\n");
            }

            stillTry = keepTryingInput();
            if(!stillTry)
                return;

            System.out.println("Enter the name of the first target: ");
            srcTargetName = sc.nextLine();
            System.out.println("Enter the name of the second target: ");
            destTargetName = sc.nextLine();

            srcTargetExist = graph.doesTargetExistByName(srcTargetName);
            destTargetExist = graph.doesTargetExistByName(destTargetName);
        }

        //TODO fix the type
        graph.getPathBetweenTargets(srcTargetName, destTargetName, 1);
    }
//207
}


