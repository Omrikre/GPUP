package Engine.Tasks;

import Engine.Graph;

public class CompilationTask extends Task implements Runnable {
    private String src; //check if exists!
    private String compilationFolder; //create if doesn't exist!
    Graph.Target target, realTarget;

    public CompilationTask(String src, String compilationFolder, Graph.Target target, Graph.Target realTarget) {
        super("Compilation");
        this.src = src;
        this.compilationFolder = compilationFolder;
        this.target = target;
        this.realTarget = realTarget;
    }

    ProcessBuilder n = new ProcessBuilder();

    @Override
    public void run() {

    }
}
