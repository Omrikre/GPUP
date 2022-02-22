package Engine.Tasks;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.Location;
import Engine.Enums.MissionState;
import Engine.Enums.State;

import java.util.*;

public class TaskManager {
    private List<MissionDTOWithoutCB> taskList;
    private Map<String, Map<String, TargetDTOWithoutCB>> targetsMap;
    private Map<String, List<MissionDTOWithoutCB>> workersMissionsMap; //map of workers with all missions each one has

    public TaskManager() {
        taskList = new ArrayList<>();
        targetsMap = new HashMap<>();
        workersMissionsMap = new HashMap<>();
    }

    public synchronized void updateTarget(String mName, TargetDTOWithoutCB t) {
        targetsMap.get(mName).get(t.getTargetName()).setTargetState(t.getTargetState());
        targetsMap.get(mName).get(t.getTargetName()).setTargetTime(t.getTargetTime());
        //set all req for skipped.
        if (t.getTargetState().equals(State.FINISHED_FAILURE)) {
            setAllReqForSkipped(mName, targetsMap.get(mName).get(t.getTargetName()));
        }

    }

    private void setAllReqForSkipped(String mName, TargetDTOWithoutCB t) {
        for (String s : t.getTargetRequiredFor()) {
            setAllReqForSkipped(mName, targetsMap.get(mName).get(s));
            if (!targetsMap.get(mName).get(s).getTargetState().equals(State.SKIPPED))
                targetsMap.get(mName).get(s).setTargetState(State.SKIPPED);
        }
    }

    public synchronized void signWorkerUpForTask(String wName, String missionName) {
        if (workersMissionsMap.containsKey(wName)) {
            workersMissionsMap.get(wName).add(getMissionByName(missionName));
        } else {
            List<MissionDTOWithoutCB> ls = new ArrayList<>();
            ls.add(getMissionByName(missionName));
            workersMissionsMap.put(wName, ls);
        }
    }

    public synchronized void removeWorkerFromAllTasks(String wName) {
        for (MissionDTOWithoutCB m : workersMissionsMap.get(wName)) {
            m.setWorkers(m.getWorkers() - 1);
            workersMissionsMap.get(wName).remove(m);
        }
    }

    public synchronized TargetDTOWithoutCB getAvailableTargetForWorker(String wName) {
        for (MissionDTOWithoutCB m : workersMissionsMap.get(wName)) {
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.INDEPENDENT) && t.getTargetState().equals(State.FROZEN)) {
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.LEAF) && t.getTargetState().equals(State.FROZEN)) {
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.MIDDLE) && t.getTargetState().equals(State.FROZEN)) {
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.ROOT) && t.getTargetState().equals(State.FROZEN)) {
                    return t;
                }
            }
        }
        return new TargetDTOWithoutCB(null, null, null, null, 0, null, 0, null, null, null, 0, 0, 0,
                0);
    }

    public synchronized void changeWorker(String wName, String mName, boolean add) {
        MissionDTOWithoutCB m = getMissionByName(mName);
        if (add) {
            m.setWorkers(m.getWorkers() + 1);
        } else {
            m.setWorkers(m.getWorkers() - 1);
            workersMissionsMap.get(wName).remove(getMissionByNameFromList(workersMissionsMap.get(wName), mName));
        }
    }

    private synchronized MissionDTOWithoutCB getMissionByNameFromList(List<MissionDTOWithoutCB> lst, String mName) {
        for (MissionDTOWithoutCB m : lst) {
            if (m.getMissionName().equals(mName))
                return m;
        }
        return null;
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
        Map<String, TargetDTOWithoutCB> mapMap = new HashMap<>();
        //turn all failed and skipped targets to frozen!
        for (TargetDTOWithoutCB t : targetsMap.get(name).values()) {
            if (t.getTargetState().equals(State.FINISHED_FAILURE) || t.getTargetState().equals(State.SKIPPED))
                t.setTargetState(State.FROZEN);
            mapMap.put(t.getTargetName(), t);
        }
        targetsMap.put(newName, mapMap);
    }

    public synchronized MissionDTOWithoutCB getMissionByName(String name) {
        for (MissionDTOWithoutCB c : taskList) {
            if (c.getMissionName().equals(name))
                return c;
        }
        return null;
    }

    public synchronized List<TargetDTOWithoutCB> getTargets(String missionName) { //targets for worker!
        //check if mission name status is paused or ready.
        //if yes, return missions
        MissionDTOWithoutCB m = getMissionByName(missionName);
        if (m.getStatus().equals(MissionState.READY.toString()) || m.getStatus().equals(MissionState.EXECUTION.toString())) {
            Map<String, TargetDTOWithoutCB> temp = targetsMap.get(missionName);
            return new ArrayList<>(temp.values());
        } else return new ArrayList<>();
    }

    public synchronized List<MissionDTOWithoutCB> getTaskDTOList() {
        return taskList;
    }

    public synchronized void setMissionStatus(String missionName, String mStatus) {
        getMissionByName(missionName).setStatus(mStatus);
    }
}
