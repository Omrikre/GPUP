package Engine.Tasks;

import Engine.DTO.TargetDTOWithoutCB;
import Engine.Engine;
import Engine.Enums.State;
import Engine.Graph;
import javafx.beans.property.IntegerProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Engine.Engine.makeMStoString;

public class CompilationTask extends Task implements Runnable {
    private String src; //check if exists!
    private String compilationFolder; //create if doesn't exist!
    private String FQN;
    private String javac, log;
    private Integer progressCount;
    private TargetDTOWithoutCB target;
    private int amountOfTargets;
    private String folder;


    public CompilationTask(String folder, int amountOfTargets, String src, String compilationFolder, TargetDTOWithoutCB target) {
        super("Compilation");
        this.folder=folder;
        this.src = src;
        this.compilationFolder = compilationFolder;
       this.target=target;
        this.amountOfTargets = amountOfTargets;
        FQN = target.getTargetInfo();
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
        long startTime=(System.currentTimeMillis());
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

        long endTime=(System.currentTimeMillis());
        target.setTargetTime(endTime-startTime);
        javac = "The success line: " + result.getOutputStream().toString();
        if (result.exitValue() != 0)
            javac = "The failure line: " + result.getErrorStream().toString();
        log = "the file being compiled: " + target.getTargetInfo() + "\n"
                + "The CMD line about to be excecuted: " + "javac -d " + compilationFolder + " -cp " + compilationFolder + " " + FQN + "\n"
                + "how much time the compiler worked: " + target.getTargetTime() + "ms";
        try (Writer out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream((folder + "\\" + target.getTargetName() + ".log"), true)))) {
            out.write(log + "\n" + javac + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.exitValue() == 0) {
            target.setTargetState(State.FINISHED_SUCCESS);
        } else {
            target.setTargetState(State.FINISHED_FAILURE);
        }

    }
}
