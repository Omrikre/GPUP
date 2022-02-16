package Engine.Tasks;

import java.util.HashSet;
import java.util.Set;

public class TaskManager {
    private Set<CompilationTask> compilationTaskSet;
    private Set<SimulationTask> simulationTaskSet;

    public TaskManager() {
        compilationTaskSet = new HashSet<>();
        simulationTaskSet = new HashSet<>();
    }

    public void addTask(Task task, Object obj) {
        if (task.getName().equals("Simulation"))
            simulationTaskSet.add((SimulationTask) obj);
        else if (task.getName().equals("Compilation"))
            compilationTaskSet.add((CompilationTask) obj);
    }

}
