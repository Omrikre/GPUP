package Engine.Tasks;

import Engine.DTO.TargetDTOWithoutCB;
import Engine.Engine;
import Engine.Enums.State;
import Engine.Graph;
import javafx.beans.property.IntegerProperty;

import java.io.*;
import java.util.Random;

import static Engine.Engine.makeMStoString;
import static java.lang.Thread.sleep;

public class SimulationTask extends Task implements Runnable {
    private final int runTime;
    private final boolean randomRunTime;
    private TargetDTOWithoutCB t;
    private final int success;
    private final int successWithWarnings;
    private String javac = "", log;
    private int amountOfTargets;
    private String folder;

    public SimulationTask(String folder, int amountOfTargets, int runTime, boolean randomRunTime, TargetDTOWithoutCB t,
                          int success, int successWithWarnings) {
        super("Simulation");
        this.folder = folder;
        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.t = t;
        this.success = success;
        this.successWithWarnings = successWithWarnings;
        this.amountOfTargets = amountOfTargets;
    }

    @Override
    public void run() {
        String log;
        int sleepTime;
        if (randomRunTime) {
            Random rand = new Random();
            sleepTime = rand.nextInt(runTime);
        } else sleepTime = runTime;
        log = "going to sleep for " + sleepTime + " ms";
        long startTime = (System.currentTimeMillis());
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log += "\nwoke up!";

        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream((folder + "\\" + t.getTargetName() + ".log"), true)))) {
            out.write(log + "\n" + javac + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = (System.currentTimeMillis());
        t.setTargetTime(endTime - startTime);
        setTargetStateByParameters(success, successWithWarnings);
    }

    private void setTargetStateByParameters(int success, int successWithWarnings) {
        Random rand = new Random();
        float magicNumber = rand.nextFloat();
        if ((float) (success) / 100 >= magicNumber) {
            magicNumber = rand.nextFloat();
            if ((float) (successWithWarnings) >= magicNumber) {
                t.setTargetState(State.FINISHED_WARNINGS);
            } else {
                t.setTargetState(State.FINISHED_SUCCESS); //TODO - in servlet after sending, dont forget to set other targets states!
            }
        } else {
            t.setTargetState(State.FINISHED_FAILURE);
        }
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
