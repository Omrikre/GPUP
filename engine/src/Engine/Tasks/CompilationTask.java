package Engine.Tasks;

import Engine.Enums.State;
import Engine.Graph;

import java.io.File;
import java.io.IOException;

public class CompilationTask extends Task implements Runnable {
    private String src; //check if exists!
    private String compilationFolder; //create if doesn't exist!
    private String FQN;
    private String javac, log;
    private Integer progressCount;
    Graph.Target target, realTarget;

    public CompilationTask(Integer progressCounter, String javac, String log, String src, String compilationFolder, Graph.Target target, Graph.Target realTarget) {
        super("Compilation");
        this.src = src;
        this.compilationFolder = compilationFolder;
        this.target = target;
        this.realTarget = realTarget;
        this.javac = javac;
        this.log = log;
        progressCount = progressCounter;
        FQN = realTarget.getInfo();
        //convert to real path
        FQN = FQN.replace(".", "/");
        FQN += ".java";

    }

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(src));
        processBuilder.command("javac", "-d", compilationFolder, "-cp", compilationFolder, FQN);
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
        javac = result.getOutputStream().toString();
        log = "";
        progressCount++;
        realTarget.setEndingTime(System.currentTimeMillis());
        target.setEndingTime(System.currentTimeMillis());
        realTarget.setTime();
        target.setTime();
        if (result.exitValue() == 0) {
            target.setState(State.FINISHED_SUCCESS);
            realTarget.setState(State.FINISHED_SUCCESS);
        } else {
            target.setState(State.FINISHED_FAILURE);
            realTarget.setState(State.FINISHED_FAILURE);
        }

    }
}
