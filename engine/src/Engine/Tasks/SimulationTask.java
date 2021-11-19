package Engine.Tasks;

import Engine.Enums.State;
import Engine.TargetDTO;

import java.util.Random;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SimulationTask {
    private static void runSimulation(int runTime, boolean randomRunTime, float success, float successWithWarnings) throws InterruptedException {
        Set<String> simTargets;
        int realRunTime = runTime, sumRunTimeOfAllTargets = 0;
        int successWithWarningsCounter = 0, successCounter = 0, failedCounter = 0;
        Random rand = new Random();
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

                targetState = changTargetState(s,success,successWithWarnings, makeMStoString(realRunTime));
                System.out.println(" running result: " + targetState.toString() + " \n");
                printTheTargetsChanges(targetState, s);
                sumRunTimeOfAllTargets += realRunTime;
                switch(targetState){
                    case FINISHED_FAILURE:
                        failedCounter++;
                        break;
                    case FINISHED_SUCCESS:
                        successCounter++;
                        break;
                    case FINISHED_WARNINGS:
                        successWithWarningsCounter++;
                        break;
                }
                System.out.println(" - - - - - - - - - - - - \n");
            }
            simTargets = engine.getSetOfWaitingTargetsNamesBottomsUp();
        }
        printSimulationSummary(sumRunTimeOfAllTargets, failedCounter, successCounter, successWithWarningsCounter);
        System.out.println("\n\n -------------------- ");
        System.out.println(" -- END SIMULATION -- ");
        System.out.println(" -------------------- \n");

    }

    private static void goToSleep(long sleepTime) {
        try {
            System.out.println(" going to sleep for " + makeMStoString(sleepTime));
            System.out.println(" -- layla tov --");
            sleep(sleepTime);
            System.out.println(" -- boker tov --");
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    private static State changTargetState(String targetName ,float success ,float successWithWarnings, String runTime) {
        Random rand = new Random();
        float magicNumber = rand.nextFloat();
        if(success >= magicNumber) {
            magicNumber = rand.nextFloat();
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
                System.out.println(" The following targets become 'waiting' because the target " + targetName + " succeed");
            else
                System.out.println(" The following targets become 'skipped' because the target " + targetName + " failed");

            for (String target : targetChanges) {
                if(firstPrint) {
                    System.out.print(" >> " + target);
                    firstPrint = false;
                }
                else
                    System.out.print(", " + target);
            }
            System.out.println(" ");
        }
        else
            System.out.println(" No changes were made to other targets");
        System.out.println(" ");
    }
    private static void printSimulationSummary(int sumRunTime, int failed, int success, int successWithWarnings) {
        int skipped = engine.getAmountOfTargets() - failed - success - successWithWarnings;
        System.out.println("\n -------------------------------");
        System.out.println("   There are " + engine.getAmountOfTargets() + " targets    ");
        System.out.println("   The run time of the simulation took " + makeMStoString(sumRunTime));
        System.out.println(" -------------------------------");
        System.out.println("   " + success + " -> succeed            ");
        System.out.println("   " + successWithWarnings + " -> succeed with warning          ");
        System.out.println("   " + failed + " -> failed       ");
        System.out.println("   " + skipped + " -> skipped       ");
        System.out.println(" -------------------------------");
    }
    private static String makeMStoString(long time) {
        long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        long hour = (time / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
    }

}
