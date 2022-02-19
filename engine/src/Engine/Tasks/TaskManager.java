package Engine.Tasks;

import Engine.DTO.MissionDTO;

import java.util.*;

public class TaskManager {
    private List<MissionDTO> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public synchronized void addTask(MissionDTO task) {
        taskList.add(task);
    }

    public synchronized List<MissionDTO> getTaskDTOList() {
        return taskList;
    }

}
