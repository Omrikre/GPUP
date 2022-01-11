package Engine.Tasks;

import Engine.Graph;

import java.io.File;
import java.io.IOException;

public class CompilationTask extends Task implements Runnable {
    private String src; //check if exists!
    private String compilationFolder; //create if doesn't exist!
    private String FQN;
    Graph.Target target, realTarget;

    public CompilationTask(String src, String compilationFolder, Graph.Target target, Graph.Target realTarget) {
        super("Compilation");
        this.src = src;
        this.compilationFolder = compilationFolder;
        this.target = target;
        this.realTarget = realTarget;
        FQN = realTarget.getInfo();
        //convert to real path
        FQN.replace(".", "/");
        FQN += ".java";

    }

    @Override
    public void run() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(src));
        processBuilder.command("javac", "-d", compilationFolder, "-cp", compilationFolder, FQN);
        try {
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
