package Engine.Tasks;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;

import java.util.*;

public class TaskManager {
    private List<MissionDTOWithoutCB> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public synchronized void addTask(MissionDTOWithoutCB task) {
        taskList.add(task);
    }

    public synchronized List<MissionDTOWithoutCB> getTaskDTOList() {
        return taskList;
    }

}
