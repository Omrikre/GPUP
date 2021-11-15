package UI;
import Engine.*;
import Engine.Enums.Location;
import Engine.Enums.State;
import Exceptions.*;

import java.sql.Time;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static java.lang.Math.random;
import static java.lang.Thread.sleep;

public class UserInOut extends Menu {
    private static boolean fileIsLoaded;
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
                sc.nextLine();
            } catch (FileNotLoadedException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
            catch (Exception e) {}
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
        System.out.println("depends: " + dto.getTargetDependsOn().toString());
        //print required targets
        System.out.println("required: " + dto.getTargetRequiredFor().toString());
        //print more info about the target
        String targetInfo = dto.getTargetInfo();
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
        srcTargetExist = engine.isTargetInGraphByName(srcTargetName);
        destTargetExist = engine.isTargetInGraphByName(destTargetName);
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

            srcTargetExist = engine.isTargetInGraphByName(srcTargetName);
            destTargetExist = engine.isTargetInGraphByName(destTargetName);
        }

        //TODO fix the type
        engine.getPathBetweenTargets(srcTargetName, destTargetName, 1);
    }



    //TODO finish simul, print the res, create log
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

        System.out.println("\n\n -- START SIMULATION -- ");
        simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        while(simTargets != null) {
            simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();

            for (String s : simTargets) {
                if (randomRunTime)
                    realRunTime = rand.nextInt(runTime);
                System.out.println("\n target name: " + s);
                dto = engine.getTargetDataTransferObjectByName(s);
                targetInfo = dto.getTargetInfo();
                if (targetInfo != null)
                    System.out.println(" target info: " + targetInfo);
                else
                    System.out.println(" no additional info for this target");
                goToSleep(realRunTime);

                runningTime.setTime(realRunTime);
                targetState = changTargetState(s,success,successWithWarnings, runningTime);
                System.out.println(" running result: " + targetState.toString());
            }
        }
        System.out.println("\n\n -- END SIMULATION -- \n");

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
    // sleep machine
    private static void goToSleep(int sleepTime) throws InterruptedException {
        try {
            System.out.println(" goes to sleep for " + sleepTime + " ms");
            System.out.println(" -- layla tov --");
            sleep(sleepTime);
            System.out.println(" -- boker tov --");
            }
        catch (InterruptedException e) {} //TODO

    }

    private static State changTargetState(String targetName ,double success ,double successWithWarnings, Time runTime) {
        Random rand = new Random();
        double magicNumber = rand.nextDouble();
        if(success >= magicNumber) {
            magicNumber = rand.nextDouble();
            if (successWithWarnings >= magicNumber) {
                setFinishedState(targetName, State.FINISHED_WARNINGS, runTime);
                return State.FINISHED_WARNINGS;
            }
            else {
                setFinishedState(targetName, State.FINISHED_SUCCESS, runTime);
                return State.FINISHED_SUCCESS;
            }
        }
        else {
            setFinishedState(targetName, State.FINISHED_FAILURE, runTime);
            return State.FINISHED_FAILURE;
        }
    }
}


