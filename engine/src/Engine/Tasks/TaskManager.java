package Engine.Tasks;

import Engine.DTO.MissionDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaskManager {
    private Map<String, MissionDTO> taskMap;

    public TaskManager() {
        taskMap = new HashMap<>();
    }

    public synchronized void addTask(MissionDTO task) {
        taskMap.put(task.getMissionName(), task);
    }

    public synchronized Map<String, MissionDTO> getTaskDTOMap() {
        return taskMap;
    }

}
