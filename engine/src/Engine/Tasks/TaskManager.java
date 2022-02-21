package Engine.Tasks;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.MissionState;

import java.util.*;

public class TaskManager {
    private List<MissionDTOWithoutCB> taskList;
    private Map<String, Map<String, TargetDTOWithoutCB>> targetsMap;

    public TaskManager() {
        taskList = new ArrayList<>();
        targetsMap = new HashMap<>();
    }

    public synchronized void addTask(MissionDTOWithoutCB task, Map<String, TargetDTOWithoutCB> targets) {
        taskList.add(task);
        targetsMap.put(task.getMissionName(), targets);
    }

    public synchronized void addTaskFromScratch(String name, String newName, String creatorName, Map<String, TargetDTOWithoutCB> targets) {
        MissionDTOWithoutCB original = getMissionByName(name);
        MissionDTOWithoutCB temp = new MissionDTOWithoutCB(original.getAmountOfTargets(), original.getTargets(), original.getSrc(), original.getCompilationFolder(), original.getRunTime(), original.isRandomRunTime(),
                original.getSuccess(), original.getSuccessWithWarnings(), newName, MissionState.READY.toString(), 0, 0, original.getTotalPrice(), creatorName, original.getGraphName(), 0, 0,
                original.getIndependenceCount(), original.getLeafCount(), original.getMiddleCount(), original.getRootCount());
        taskList.add(temp);
        targetsMap.put(newName, targets);
    }

    public synchronized void addTaskIncremental(String name, String newName, String creatorName) {
        MissionDTOWithoutCB original = getMissionByName(name);
        MissionDTOWithoutCB temp = new MissionDTOWithoutCB(original.getAmountOfTargets(), original.getTargets(), original.getSrc(), original.getCompilationFolder(), original.getRunTime(), original.isRandomRunTime(),
                original.getSuccess(), original.getSuccessWithWarnings(), newName, MissionState.READY.toString(), 0, 0, original.getTotalPrice(), creatorName, original.getGraphName(), 0, 0,
                original.getIndependenceCount(), original.getLeafCount(), original.getMiddleCount(), original.getRootCount());
        taskList.add(temp);
        targetsMap.put(newName, targetsMap.get(name));
    }

    public synchronized MissionDTOWithoutCB getMissionByName(String name) {
        for (MissionDTOWithoutCB c : taskList) {
            if (c.getMissionName().equals(name))
                return c;
        }
        return null;
    }

    public synchronized Map<String, TargetDTOWithoutCB> getTargets(String missionName) { //targets for worker!
        //check if mission name status is paused or ready.
        //if yes, return missions
        MissionDTOWithoutCB m = getMissionByName(missionName);
        if (m.getStatus().equals(MissionState.READY.toString()) || m.getStatus().equals(MissionState.EXECUTION.toString()))
            return targetsMap.get(missionName);
        else return null;
    }

    public synchronized List<MissionDTOWithoutCB> getTaskDTOList() {
        return taskList;
    }

    public synchronized void setMissionStatus(String missionName, String mStatus) {
        getMissionByName(missionName).setStatus(mStatus);
    }
}
