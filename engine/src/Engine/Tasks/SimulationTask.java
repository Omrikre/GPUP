package Engine.Tasks;

import Engine.Enums.State;
import Engine.Graph;

import java.util.Random;

import static java.lang.Thread.sleep;

public class SimulationTask extends Task implements Runnable {
    private final int runTime;
    private final boolean randomRunTime;
    private Graph.Target t, realTarget;
    private final int success;
    private final int successWithWarnings;

    public SimulationTask(int runTime, boolean randomRunTime, Graph.Target t, Graph.Target realTarget, int success, int successWithWarnings) {
        super("Simulation");
        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.t = t;
        this.realTarget = realTarget;
        this.success = success;
        this.successWithWarnings = successWithWarnings;
    }

    @Override
    public void run() {
        int sleepTime;
        if (randomRunTime) {
            Random rand = new Random();
            sleepTime = rand.nextInt(runTime);
        } else sleepTime = runTime;
        t.setState(State.IN_PROCESS);
        realTarget.setState(State.IN_PROCESS);
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.setTargetStateByParameters(success, successWithWarnings);
        realTarget.setTargetStateByParameters(success, successWithWarnings);
    }

    /*public String simulationStartInfo(TargetDTO target) {
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

    public String simulationRunAndResult(String targetChanges, State state, long runTime) {
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

    public State changeTargetState(float success, float successWithWarnings) {
        Random rand = new Random();
        float magicNumber = rand.nextFloat();
        if (success >= magicNumber) {
            magicNumber = rand.nextFloat();
            if (successWithWarnings >= magicNumber) {
                return State.FINISHED_WARNINGS;
            } else {
                return State.FINISHED_SUCCESS;
            }
        } else {
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
        } else
            res = " No changes were made to other targets";
        res += "\n";

        return res;
    }*/


}
