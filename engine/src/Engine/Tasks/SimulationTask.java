package Engine.Tasks;

import Engine.Enums.State;
import Engine.DTO.TargetDTO;

import java.util.Random;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SimulationTask extends Task {
    public SimulationTask() {
        super("Simulation");
    }

    public String simulationStartInfo(TargetDTO target) {
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

    public String simulationRunAndResult(String targetChanges, String targetName, State state, long runTime) {
        String res;
        goToSleep(runTime);
        res = " running result: " + state.toString() + " \n\n";
        res += targetChanges;
        super.addTotalRuntime(runTime);
        return res;
    }

    public int getSleepTime(int runTime) {
        Random rand = new Random();
        return rand.nextInt(runTime);
    }

    private void goToSleep(long sleepTime) {
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

    public String getTargetChanges(boolean mainTargetSucceed, Set<String> targetChanges, String targetName) {
        String res;
        boolean firstPrint = true;
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

}
