package Engine.Tasks;

import java.util.HashSet;
import java.util.Set;

public class TaskManager {
    private Set<Task> taskSet;

    public TaskManager() {
        taskSet = new HashSet<>();
    }

    public synchronized void addTask(Task task) {
        taskSet.add(task);
    }

    

}
