package UI;

import Engine.Engine;
import Engine.Enums.Bond;
import Engine.Enums.Location;

import Engine.Enums.State;
import Engine.DTO.TargetDTO;
import Exceptions.FileNotLoadedException;

import java.io.IOException;
import java.util.*;

public class UserInOut extends Menu implements UI {
    private static boolean fileIsLoaded;
    private static final Engine engine = new Engine();
    private static Location type;
    private boolean firstSimulationHappened = false;

    // run the UI
    public void runProgram() {
        boolean runProgram = true;
        Scanner sc = new Scanner(System.in);
        int userChoice;

        printHeader();

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
                    case 6:
                        if (!fileIsLoaded)
                            throw new FileNotLoadedException();
                        findCycle();
                        break;
                    case 0:
                        runProgram = false;
                        break;
                    default:
                        System.out.println("\n -- Please choose a number from the menu (0-5) --");
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("\n -- Bad input: please enter your choice from the menu by the number -- ");
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
            sc.nextLine();
            System.out.println("\n *****************************************");
        }
        System.out.println("\n             -- Goodbye! -- \n");
    }

    // 1
    private static boolean fileLoad() {
        try {
            String filename, filePath;
            int userChoice = -1;
            Scanner sc = new Scanner(System.in);

            if (fileIsLoaded) {
                while (userChoice != 1 && userChoice != 2) {
                    System.out.println("\n -- There is a file that is loaded in the system --\n");
                    System.out.println(" what would you like to do?");
                    System.out.println(" 1. run over the current file and load a new one");
                    System.out.println(" 2. stay with the current file and return to the main menu");
                    System.out.print(" your choice: ");
                    userChoice = sc.nextInt();
                    sc.nextLine();
                }
            }
            if (userChoice == 2)
                return true;

            System.out.print(" Enter file path: ");
            filePath = sc.nextLine();
            engine.loadFile(filePath);

            filename = "--TODO--";
            if (filePath.contains("/"))
                filename = filePath.substring(filePath.lastIndexOf("/") + 1);
            if (filePath.contains("\\"))
                filename = filePath.substring(filePath.lastIndexOf("\\") + 1);

            System.out.println("\n -- The file '" + filename + "' has been loaded --");
            return true;
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
            if (fileIsLoaded) {
                System.out.println(" -- The last valid file entered into the system is still loaded --");
                return true;
            }
            return false;
        }
    }

    // 2
    private void generalInfo() {
        int numOfTargets = engine.getAmountOfTargets();
        Map<Location, Integer> numOfTypes = engine.howManyTargetsInEachLocation();
        System.out.println("\n --------------------------");
        System.out.println("|   There are " + numOfTargets + " targets   |");
        System.out.println("|   " + numOfTypes.get(Location.LEAF) + "-> leaves             |");
        System.out.println("|   " + numOfTypes.get(Location.MIDDLE) + "-> middles            |");
        System.out.println("|   " + numOfTypes.get(Location.ROOT) + "-> roots              |");
        System.out.println("|   " + numOfTypes.get(Location.INDEPENDENT) + "-> independents       |");
        System.out.println(" --------------------------");
    }

    // 3
    private static void targetsInfo() {
        boolean targetInFile;
        Scanner sc = new Scanner(System.in);

        System.out.print(" Enter target name: ");
        String targetName = sc.nextLine();
        targetInFile = engine.isTargetInGraphByName(targetName);

        while (!targetInFile) {
            System.out.println("\n -- The target you entered is not in the database --");
            if (!keepTryingInput()) {
                return;
            } else {
                System.out.print(" enter target name: ");
                targetName = sc.nextLine();
                targetInFile = engine.isTargetInGraphByName(targetName);
            }
        }
        TargetDTO dto = engine.getTargetDataTransferObjectByName(targetName);
        //print name and type
        System.out.println(" - name: " + targetName);
        System.out.println(" - type: " + dto.getTargetLocation().toString());
        //print dependent targets
        if (dto.getTargetDependsOn().isEmpty())
            System.out.println(" - depends: no depends");
        else
            System.out.println(" - depends: " + dto.getTargetDependsOn().toString());
        //print required targets
        if (dto.getTargetRequiredFor().isEmpty())
            System.out.println(" - required: no required");
        else
            System.out.println(" - required: " + dto.getTargetRequiredFor().toString());
        //print more info about the target
        String targetInfo = dto.getTargetInfo();
        if (targetInfo != null)
            System.out.println(" - info: " + targetInfo);

    }

    // 4
    private void pathBetweenTargets() {
        Scanner sc = new Scanner(System.in);
        String srcTargetName, destTargetName;
        boolean srcTargetExist, destTargetExist, stillTry;
        int res = 0;
        Set<List<String>> lst;

        System.out.print(" Enter the name of the first target: ");
        srcTargetName = sc.nextLine();
        System.out.print(" Enter the name of the second target: ");
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
            System.out.println(" ");
            if (!stillTry)
                return;

            System.out.print(" Enter the name of the first target: ");
            srcTargetName = sc.nextLine();
            System.out.print(" Enter the name of the second target: ");
            destTargetName = sc.nextLine();

            srcTargetExist = engine.isTargetInGraphByName(srcTargetName);
            destTargetExist = engine.isTargetInGraphByName(destTargetName);
        }

        // get BOND from user
        while (res != 1 && res != 2) {
            System.out.println("\n In which bond between the targets to search for?");
            System.out.println(" 1. the target " + srcTargetName + " depends on the target " + destTargetName);
            System.out.println(" 2. the target " + srcTargetName + " required for the target " + destTargetName);
            System.out.print(" Your choice: ");
            res = sc.nextInt();
        }

        if (res == 1)
            lst = engine.getPathBetweenTargets(srcTargetName, destTargetName, Bond.DEPENDS_ON);
        else
            lst = engine.getPathBetweenTargets(srcTargetName, destTargetName, Bond.REQUIRED_FOR);

        if (lst.isEmpty()) {
            System.out.print("\n The target " + srcTargetName + " has no ");
            if (res == 1)
                System.out.print(Bond.DEPENDS_ON);
            else
                System.out.print(Bond.REQUIRED_FOR);
            System.out.println(" path to the target " + destTargetName);
            return;
        }
        printListOfTargets(lst, res);
    }

    // 5
    private void Simulation() throws IOException {
        Scanner sc = new Scanner(System.in);
        int runTime;
        float probabilityForSuccess, probabilityForSuccessWarnings;
        boolean randomRunTime = false;


        if (firstSimulationHappened) {
            switch (newSimulateOrContinue()) {
                case 0:
                    return;
                case 1:
                    engine.setAllFailedAndSkippedTargetsFrozen();
                    System.out.println("\n -- CONTINUES SIMULATION --\n");
                    break;
                case 2:
                    engine.setAllTargetsFrozen();
                    System.out.println("\n -- SIMULATION FROM SCRATCH --\n");
                    break;
            }
        } else {
            System.out.println("\n -- SIMULATION FROM SCRATCH --\n");
            engine.setAllTargetsFrozen();
        }

        // get simulation time per target
        System.out.print(" enter the simulation time per target (in ms): ");
        runTime = sc.nextInt();

        switch (randomRunTime(runTime)) {
            case 0:
                return;
            case 1:
                break;
            case 2:
                randomRunTime = true;
                break;
        }
        probabilityForSuccess = getProbabilityToSuccess();
        if (probabilityForSuccess == 9)
            return;
        if (probabilityForSuccess != 0) {
            probabilityForSuccessWarnings = getProbabilityToSuccessWarnings();
            if (probabilityForSuccessWarnings == 9)
                return;
        } else {
            probabilityForSuccessWarnings = 0;
        }


        runSimulation(runTime, randomRunTime, probabilityForSuccess, probabilityForSuccessWarnings);
        firstSimulationHappened = true;
    }

    private int randomRunTime(int runTime) {
        int MenuChoice;
        printRandomRunTimeMenu(runTime);
        Scanner sc = new Scanner(System.in);
        MenuChoice = sc.nextInt();

        while (!(MenuChoice == 1 || MenuChoice == 2 || MenuChoice == 0)) {
            System.out.println("\n -- Enter number by the menu (1/2/0) --");
            printRandomRunTimeMenu(runTime);
            sc.nextLine();
            MenuChoice = sc.nextInt();
        }
        return MenuChoice;
    }

    private void printRandomRunTimeMenu(int runTime) {
        System.out.println("\n What would you prefer? ");
        System.out.println(" 1. fixed processing time - " + runTime + " ms per target");
        System.out.println(" 2. random processing time - up to " + runTime + " ms");
        System.out.println(" 0. cancel and return to the main menu");
        System.out.print(" Enter your choice: ");
    }


    private float getProbabilityToSuccess() {
        // get from user the probability to success

        float res;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n In what probability to success do you want the simulation to run? ");
        System.out.println(" 1   --> all the targets will success");
        System.out.println(" 0.5 --> half will success and half will fail");
        System.out.println(" 0   --> all will fail");
        System.out.print(" Enter any number between 0 to 1: ");
        res = sc.nextFloat();
        while (res < 0 || res > 1) {
            if (res == 9)
                return 9;
            System.out.println("\n If you prefer to return to the main menu enter '9'");
            System.out.println(" The probability to success has to be a number between 0 to 1");
            System.out.print(" Enter your choice: ");
            res = sc.nextFloat();
        }
        return res;

    }

    private float getProbabilityToSuccessWarnings() {
        // get probability it's a success with warnings
        float res;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n If the task succeeded, what the probability it's a success with warnings? ");
        System.out.println(" 1   --> it's a success with warnings");
        System.out.println(" 0   --> it's a success without warnings");
        System.out.print(" Enter any number between 0 to 1: ");
        res = sc.nextFloat();
        while (res < 0 || res > 1) {
            if (res == 9)
                return 9;
            System.out.println("\n If you prefer to return to the main menu enter '9'");
            System.out.println(" The probability to success with warnings has to be a number between 0 to 1");
            System.out.print(" Enter your choice: ");
            res = sc.nextFloat();
        }
        return res;
    }

    private void runSimulation(int runTime, boolean randomRunTime, float success, float successWithWarnings) throws IOException {
        Set<String> simTargets;
        long realRunTime;
        String runStringRes;

        System.out.println("\n\n ---------------------- ");
        System.out.println(" -- START SIMULATION -- ");
        System.out.println(" ---------------------- \n");

        simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        while (simTargets != null) {
            for (String s : simTargets) {

                System.out.println(engine.simulationStartInfo(s));
                if(randomRunTime)
                    realRunTime = engine.getSleepTime(runTime);
                else
                    realRunTime = runTime;
                System.out.println(" going to sleep for " + Engine.makeMStoString(realRunTime));
                System.out.println(" -- layla tov --");
                runStringRes = engine.simulationRunAndResult(s, realRunTime, success, successWithWarnings);
                System.out.println(" -- boker tov --");
                System.out.println(runStringRes);

                System.out.println(" - - - - - - - - - - - - \n");
            }
            simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        }
        firstSimulationHappened = true;
        printSimulationSummary();

        System.out.println("\n\n -------------------- ");
        System.out.println(" -- END SIMULATION -- ");
        System.out.println(" -------------------- \n");

    }

    private int newSimulateOrContinue() {
        int MenuChoice;

        printNewSimulateOrContinueMenu();
        Scanner sc = new Scanner(System.in);
        MenuChoice = sc.nextInt();

        while (!(MenuChoice == 1 || MenuChoice == 2 || MenuChoice == 0)) {
            System.out.println("\n -- Enter number by the menu (1/2/0) --");

            printNewSimulateOrContinueMenu();
            sc.nextLine();
            MenuChoice = sc.nextInt();
        }
        return MenuChoice;
    }

    private void printSimulationSummary() {
        Map<State, Integer> stateMap = engine.howManyTargetsInEachState();
        System.out.println("\n -------------------------------");
        System.out.println("   There are " + engine.getAmountOfTargets() + " targets    ");
        System.out.println("   The run time of the simulation took " + engine.getTotalRuntime());
        System.out.println(" -------------------------------");
        System.out.println("   " + stateMap.get(State.FINISHED_SUCCESS) + " -> succeed            ");
        System.out.println("   " + stateMap.get(State.FINISHED_WARNINGS) + " -> succeed with warning          ");
        System.out.println("   " + stateMap.get(State.FINISHED_FAILURE) + " -> failed       ");
        System.out.println("   " + stateMap.get(State.SKIPPED) + " -> skipped       ");
        System.out.println(" -------------------------------");
    }

    // 6
    private void findCycle() {
        boolean targetInFile;
        Scanner sc = new Scanner(System.in);

        System.out.print(" To check if a goal is part of a circle\n" +
                " Please enter the name of the target: ");
        String targetName = sc.nextLine();
        targetInFile = engine.isTargetInGraphByName(targetName);

        while (!targetInFile) {
            System.out.println(" -- The target you entered is not in the database --");
            if (!keepTryingInput()) {
                return;
            } else {
                System.out.println(" Please enter the target name again: ");
                targetName = sc.nextLine();
                targetInFile = engine.isTargetInGraphByName(targetName);
            }
        }
        Set<List<String>> res = engine.isTargetInCircleByName(targetName);
        if (res.size() == 0) {
            System.out.println("\n -- The target '" + targetName + "' is NOT part of a cycle -- ");
        } else {
            System.out.println("\n -- The target '" + targetName + "' is part of a cycle -- ");
            printListOfTargets(res, 1);
        }
    }

    private void printListOfTargets(Set<List<String>> lst, int whichWay) {
        boolean firstTarget = true;
        int lineCount = 0;
        System.out.println(" ");
        for (List<String> line : lst) {
            lineCount++;
            System.out.print(" " + lineCount + ") ");
            for (String targetName : line) {
                if (firstTarget) {
                    System.out.print(targetName);
                    firstTarget = false;
                } else {
                    if (whichWay == 1)
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


}