package UI;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.Scanner;

public class UserInOut extends Menu {
    private static boolean fileIsLoaded;


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

        if(true) {
            System.out.println(" -- The file " + filename + " has been loaded --");
            return true;
        }
        else {
            System.out.println(" -- The file has NOT been loaded --");
            // TODO add exception toString print
            return false;
        }
    }
    private static void generalInfo() {
        //get number of the targets and the types
        int numOfTargets = 1000;
        int numOfLeaf = 1000;
        int numOfMiddle = 1000;
        int numOfRoot = 1000;
        int numOfIndependents = 1000;

        System.out.println("There is " + numOfTargets + " targets");
        System.out.println(numOfLeaf + " Leafs");
        System.out.println(numOfMiddle + " Middles");
        System.out.println(numOfRoot + " Roots");
        System.out.println(numOfIndependents + " Independents");
    }
    private static void targetsInfo() {
        boolean targetInFile = true;
        boolean stayInTargetInfo = false;
        String targetName = "";
        String targetType = "";
        //get line and check if in the file

        //check if target in file -
        while (!targetInFile)
            System.out.println(" -- The target you entered is not in the database --");
        System.out.println(" do you want to try again? (y - yes | n - back to menu): ");
        //scanner and exceptions
        if (!stayInTargetInfo) {
            return;
        } else {
            System.out.println(" enter target name: ");
            //check again - if correct - targetInFile = true
        }
            System.out.println("name: " + targetName);
            System.out.println("type: " + targetType);
            //print dependent targets
            //print required targets
            //print more info about the target

    }

}


