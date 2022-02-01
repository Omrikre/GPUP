package Engine.Tasks;

import Engine.Engine;
import Engine.Enums.State;
import Engine.Graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompilationTask extends Task implements Runnable {
    private String src; //check if exists!
    private String compilationFolder; //create if doesn't exist!
    private String FQN;
    private String javac, log;
    private Integer progressCount;
    private Graph.Target target, realTarget;
    private Engine e;
    private int amountOfTargets;

    public CompilationTask(int amountOfTargets, Engine e, String src, String compilationFolder, Graph.Target target, Graph.Target realTarget) {
        super("Compilation");
        this.src = src;
        this.compilationFolder = compilationFolder;
        this.target = target;
        this.realTarget = realTarget;
        this.e = e;
        this.amountOfTargets = amountOfTargets;
        FQN = realTarget.getInfo();
        //convert to real path
        FQN = FQN.replace(".", "\\");
        FQN += ".java";
        FQN = src + "\\" + FQN;

    }

    @Override
    public void run() {
        String log = "";
        String javac = "";
        ProcessBuilder processBuilder = new ProcessBuilder("javac", "-d", compilationFolder, "-cp", compilationFolder, FQN);

        //processBuilder.directory(new File("C:\\"));
        processBuilder.directory(new File(src));
        //processBuilder.command("javac", "-d", compilationFolder, "-cp", compilationFolder, FQN);
        Process result = null;
        realTarget.setStartingTime(System.currentTimeMillis());
        target.setStartingTime(System.currentTimeMillis());
        try {
            result = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            result.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        e.progressCounter++;
        e.calculateProgress(amountOfTargets);
        realTarget.setEndingTime(System.currentTimeMillis());
        target.setEndingTime(System.currentTimeMillis());
        realTarget.setTime();
        target.setTime();
        javac = "The success line: " + result.getOutputStream().toString();
        if (result.exitValue() != 0)
            javac = "The failure line: " + result.getErrorStream().toString();
        log = "the file being compiled: " + target.getInfo() + "\n"
                + "The CMD line about to be excecuted: " + "javac -d " + compilationFolder + " -cp " + compilationFolder + " " + FQN + "\n"
                + "how much time the compiler worked: " + target.getTime() + "ms";
        e.updateJavac(javac);
        e.updateLog(log);
        e.createTargetFileByName(target.getName());
        try {
            e.saveTargetInfoToFile(log + "\n" + javac);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (result.exitValue() == 0) {
            target.setFinishedTargetState(State.FINISHED_SUCCESS);
            realTarget.setFinishedTargetState(State.FINISHED_SUCCESS);
        } else {
            target.setFinishedTargetState(State.FINISHED_FAILURE);
            realTarget.setFinishedTargetState(State.FINISHED_FAILURE);
        }

    }
}
