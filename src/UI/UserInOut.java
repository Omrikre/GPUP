package UI;

import Engine.Engine;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.TargetDTO;
import Exceptions.FileException;
import Exceptions.FileNotLoadedException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.*;
import static java.lang.Thread.sleep;


public class UserInOut extends Menu {
    private static boolean fileIsLoaded;
    private static final Engine engine = new Engine();
    private static Location type;

    // run the UI
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
                        if (!fileIsLoaded)
                            throw new FileNotLoadedException();
                        generalInfo();
                        break;
                    case 3:
                        if (!fileIsLoaded)
                            throw new FileNotLoadedException();
                        targetsInfo();
                        break;
                    case 4:
                        if (!fileIsLoaded)
                            throw new FileNotLoadedException();
                        pathBetweenTargets();
                        break;
                    case 5:
                        if (!fileIsLoaded)
                            throw new FileNotLoadedException();
                        Simulation();
                        break;
                    case 0:
                        runProgram = false;
                        break;
                    default:
                        System.out.println("\n -- Please choose a number from the menu (0-5) --");
                        break;
                }
                //sc.nextLine();
            } catch (FileNotLoadedException e) {
                System.out.println(e.getMessage());
                //sc.nextLine();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(" -- Goodbye! -- \n");
    }

    // 1
    private static boolean fileLoad() throws FileException, JAXBException, FileNotFoundException {
        try {
            String filename, filePath;
            int userChoice = -1;
            Scanner sc = new Scanner(System.in);

            if (fileIsLoaded) {
                while (userChoice != 1 && userChoice != 2) {
                    sc.nextLine();
                    System.out.println(" there is a file that is loaded in the system");
                    System.out.println(" what would you like to do?");
                    System.out.println("1. run over the current file and load a new one");
                    System.out.println("2. stay with the current file and return to the main menu");
                    System.out.print(" your choice: ");
                    userChoice = sc.nextInt();
                }
            }

            System.out.print("\n enter file path: ");
            filePath = sc.nextLine();
            engine.loadFile(filePath);

            filename = "--TODO--";
            if (filePath.contains("/"))
                filename = filePath.substring(filePath.lastIndexOf("/")+1);
            if (filePath.contains("\\"))
                filename = filePath.substring(filePath.lastIndexOf("\\")+1);

            System.out.println(" -- The file '" + filename + "' has been loaded --");
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    // 2
    private static void generalInfo() {

        int numOfTargets = engine.getAmountOfTargets();
        Map<Location, Integer> numOfTypes = engine.howManyTargetsInEachLocation();

        System.out.println("There is " + numOfTargets + " targets");
        System.out.println(numOfTypes.get(type.LEAF) + " leaves");
        System.out.println(numOfTypes.get(type.MIDDLE) + " middles");
        System.out.println(numOfTypes.get(type.ROOT) + " roots");
        System.out.println(numOfTypes.get(type.INDEPENDENT) + " independents");
    }
    // 3
    private static void targetsInfo() {
        boolean targetInFile;
        Scanner sc = new Scanner(System.in);

        System.out.print(" enter target name: ");
        String targetName = sc.nextLine();
        targetInFile = engine.isTargetInGraphByName(targetName);

        while (!targetInFile) {
            System.out.println(" -- The target you entered is not in the database --");
            if (!keepTryingInput()) {
                return;
            } else {
                System.out.println(" enter target name: ");
                targetName = sc.nextLine();
                targetInFile = engine.isTargetInGraphByName(targetName);
            }
        }
        TargetDTO dto = engine.getTargetDataTransferObjectByName(targetName);
        //print name and type
        System.out.println("name: " + targetName);
        System.out.println("type: " + dto.getTargetLocation().toString());
        //print dependent targets
        if(dto.getTargetDependsOn().isEmpty())
            System.out.println("depends: no depends");
        else
            System.out.println("depends: " + dto.getTargetDependsOn().toString());
        //print required targets
        if(dto.getTargetRequiredFor().isEmpty())
            System.out.println("required: no required");
        else
            System.out.println("required: " + dto.getTargetRequiredFor().toString());
        //print more info about the target
        String targetInfo = dto.getTargetInfo();
        if (targetInfo != null)
            System.out.println("info: " + targetInfo);

    }
    // 4
    private static void pathBetweenTargets() {
        Scanner sc = new Scanner(System.in);
        String srcTargetName, destTargetName;
        boolean srcTargetExist, destTargetExist, stillTry;
        int res = 0;
        Set<List<String>> lst;

        System.out.print("Enter the name of the first target: ");
        srcTargetName = sc.nextLine();
        System.out.print("Enter the name of the second target: ");
        destTargetName = sc.nextLine();
        srcTargetExist = engine.isTargetInGraphByName(srcTargetName);
        destTargetExist = engine.isTargetInGraphByName(destTargetName);
        System.out.println(" ");

        while (!srcTargetExist || !destTargetExist) {

            if (!srcTargetExist && !destTargetExist) {
                System.out.print("\n -- The targets '" + srcTargetName + "' and '" + destTargetName + "' are NOT exist in the database --\n\n");
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

            System.out.print("Enter the name of the first target: ");
            srcTargetName = sc.nextLine();
            System.out.print("Enter the name of the second target: ");
            destTargetName = sc.nextLine();

            srcTargetExist = engine.isTargetInGraphByName(srcTargetName);
            destTargetExist = engine.isTargetInGraphByName(destTargetName);
        }

        // get BOND from user
        while (res !=1 && res !=2) {
        System.out.println("\n in which bond between the targets to search for?");
        System.out.println(" 1. the target " + srcTargetName + " depends on the target " + destTargetName);
        System.out.println(" 2. the target " + srcTargetName + " required for the target " + destTargetName);
        System.out.print(" your choice: ");
        res = sc.nextInt();
        }

        if (res == 1)
            lst = engine.getPathBetweenTargets(srcTargetName, destTargetName, Bond.DEPENDS_ON);
        else
            lst = engine.getPathBetweenTargets(srcTargetName, destTargetName, Bond.REQUIRED_FOR);

        if(lst.isEmpty()) {
            System.out.print(" the target " + srcTargetName + " has no ");
            if(res == 1)
                System.out.print(Bond.DEPENDS_ON.toString());
            else
                System.out.print(Bond.REQUIRED_FOR.toString());
            System.out.println(" path to the target " + destTargetName);
            return;
        }
        boolean firstTarget = true;
        int lineCount = 0;
        for (List<String> line : lst) {
            lineCount++;
            System.out.print(lineCount + ") ");
            for (String targetName : line) {
                if(firstTarget) {
                    System.out.print(targetName);
                    firstTarget = false;
                }
                else {
                    if(res == 1)
                        System.out.print(" >> " + targetName);
                    else
                        System.out.print(" << " + targetName);
                }
            }
            firstTarget = true;
            System.out.println(" ");
        }
        System.out.println(" ");
    }
    // 5
    private static void Simulation() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        int runTime;
        float probabilityForSuccess, probabilityForSuccessWarnings;
        boolean randomRunTime = false;

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

        runSimulation(runTime, randomRunTime, probabilityForSuccess, probabilityForSuccessWarnings);
       }
    private static void runSimulation(int runTime, boolean randomRunTime, float success, float successWithWarnings) throws InterruptedException {
        Set<String> simTargets;
        int realRunTime = runTime;
        Random rand = new Random();
        Time runningTime = null;
        String targetInfo;
        TargetDTO dto;
        State targetState;

        System.out.println("\n\n ---------------------- ");
        System.out.println(" -- START SIMULATION -- ");
        System.out.println(" ---------------------- \n");
        simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        while(simTargets != null) {
            for (String s : simTargets) {
                if (randomRunTime)
                    realRunTime = rand.nextInt(runTime);
                System.out.println(" target name: " + s);
                dto = engine.getTargetDataTransferObjectByName(s);
                targetInfo = dto.getTargetInfo();
                if (targetInfo != null)
                    System.out.println(" target info: " + targetInfo);
                else
                    System.out.println(" no additional info for this target");
                goToSleep(realRunTime);

                runningTime = new Time(realRunTime);
                targetState = changTargetState(s,success,successWithWarnings, runningTime);
                System.out.println(" running result: " + targetState.toString() + " \n");
                printTheTargetsChanges(targetState, s);
            }
            simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        }
        System.out.println("\n\n -------------------- ");
        System.out.println(" -- END SIMULATION -- ");
        System.out.println(" -------------------- \n");

    }
    private static int randomRunTime(int runTime) {
        int MenuChoice = -1;
        printRandomRunTimeMenu(runTime);
        Scanner sc = new Scanner(System.in);
        MenuChoice = sc.nextInt();

        while (!(MenuChoice == 1 || MenuChoice == 2 || MenuChoice == 0)) {
            System.out.println("\n -- enter num by the menu (1/2/0) --");
            printRandomRunTimeMenu(runTime);
            sc.nextLine();
            MenuChoice = sc.nextInt();
        }
        return MenuChoice;
    }
    private static void printRandomRunTimeMenu(int runTime) {
        System.out.println("\n what would you prefer? ");
        System.out.println(" 1. fixed processing time - " + runTime + " ms per target");
        System.out.println(" 2. random processing time - up to " + runTime + " ms");
        System.out.println(" 0. cancel and return to the main menu");
        System.out.print(" enter your choice: ");
    }
    private static float getProbabilityToSuccess() {
        // get from user the probability to success

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
    private static float getProbabilityToSuccessWarnings() {
        // get probability it's a success with warnings
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
    private static void goToSleep(int sleepTime) throws InterruptedException {
        // sleep machine
        try {
        System.out.println(" goes to sleep for " + sleepTime + " ms");
        System.out.println(" -- layla tov --");
        sleep(sleepTime);
        System.out.println(" -- boker tov --");
         }
        catch (InterruptedException e) {        }
    }
    private static State changTargetState(String targetName ,double success ,double successWithWarnings, Time runTime) {
        Random rand = new Random();
        double magicNumber = rand.nextDouble();
        if(success >= magicNumber) {
            magicNumber = rand.nextDouble();
            if (successWithWarnings >= magicNumber) {
                engine.setFinishedState(targetName, State.FINISHED_WARNINGS, runTime);
                return State.FINISHED_WARNINGS;
            }
            else {
                engine.setFinishedState(targetName, State.FINISHED_SUCCESS, runTime);
                return State.FINISHED_SUCCESS;
            }
        }
        else {
            engine.setFinishedState(targetName, State.FINISHED_FAILURE, runTime);
            return State.FINISHED_FAILURE;
        }
    }
    private static void printTheTargetsChanges(State state, String targetName) {
        Set<String> targetChanges;
        boolean firstPrint = true;
        boolean mainTargetSucceed = (state == State.FINISHED_SUCCESS || state == State.FINISHED_WARNINGS);

        if(mainTargetSucceed)
            targetChanges = engine.getRunnableTargetsNamesFromFinishedTarget(targetName);
        else
            targetChanges = engine.getSkippedTargetsNamesFromFailedTarget(targetName);

        if (!targetChanges.isEmpty()) {
            if (mainTargetSucceed)
                System.out.println(" the following targets become 'waiting' because the target " + targetName + " succeed");
            else
                System.out.println(" the following targets become 'skipped' because the target " + targetName + " failed");

            for (String target : targetChanges) {
                if(firstPrint) {
                    System.out.print(" >> " + target);
                    firstPrint = false;
                }
                else
                    System.out.print(", " + target);
            }
        }
        else
            System.out.println(" no changes were made to other targets");
        System.out.println("\n");
    }
}


