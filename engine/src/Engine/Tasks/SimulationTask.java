package Engine.Tasks;

import Engine.Enums.State;
import Engine.TargetDTO;

import java.util.Random;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SimulationTask {
    private long totalRunTime = 0;

    private String SimulationStartInfo(TargetDTO target) {
        String targetInfo;
        String res;

        res = " target name: " + target.getTargetName();
        targetInfo = target.getTargetInfo();
        if (targetInfo != null)
            res = res + "\n target info: " + targetInfo;
        else
            res = res + "\n no additional info for this target";
        return res;
    }

    private String SimulationRunAndResult(String targetName, int runTime, float success, float successWithWarnings) {
        State targetState;
        String res;

        goToSleep(runTime);

        targetState = changTargetState(targetName, success, successWithWarnings, makeMStoString(runTime));
        res = " running result: " + targetState.toString() + " \n\n";
        res += printTheTargetsChanges(targetState, targetName);
        //setTotalRunTime(getTotalRunTime() + realRunTime); //TODO

        return res;
    }

    private long getSleepTime(int runTime) {
        Random rand = new Random();
        return rand.nextInt(runTime);
    }

    private static void goToSleep(long sleepTime) {
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private State changTargetState(String targetName, float success, float successWithWarnings, String runTime) {
        Random rand = new Random();
        float magicNumber = rand.nextFloat();
        if (success >= magicNumber) {
            magicNumber = rand.nextFloat();
            if (successWithWarnings >= magicNumber) {
                setFinishedState(targetName, State.FINISHED_WARNINGS, runTime);
                return State.FINISHED_WARNINGS;
            } else {
                setFinishedState(targetName, State.FINISHED_SUCCESS, runTime);
                return State.FINISHED_SUCCESS;
            }
        } else {
            setFinishedState(targetName, State.FINISHED_FAILURE, runTime);
            return State.FINISHED_FAILURE;
        }
    }

    private String printTheTargetsChanges(State state, String targetName) {
        Set<String> targetChanges;
        String res;
        boolean firstPrint = true;
        boolean mainTargetSucceed = (state == State.FINISHED_SUCCESS || state == State.FINISHED_WARNINGS);

        if (mainTargetSucceed)
            targetChanges = getRunnableTargetsNamesFromFinishedTarget(targetName);
        else
            targetChanges = getSkippedTargetsNamesFromFailedTarget(targetName);

        if (!targetChanges.isEmpty()) {
            if (mainTargetSucceed)
                res = " The following targets become 'waiting' because the target " + targetName + " succeed \n";
            else
                res = " The following targets become 'skipped' because the target " + targetName + " failed \n";

            for (String target : targetChanges) {
                if (firstPrint) {
                    res += " >> " + target;
                    firstPrint = false;
                } else
                    res += ", " + target;
            }
            res += "\n";
        } else
            res = "\n No changes were made to other targets";
        res += "\n";

        return res;
    }

    private void printSimulationSummary(int sumRunTime, int failed, int success, int successWithWarnings) {
        int skipped = getAmountOfTargets() - failed - success - successWithWarnings;
        System.out.println("\n -------------------------------");
        System.out.println("   There are " + getAmountOfTargets() + " targets    ");
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
