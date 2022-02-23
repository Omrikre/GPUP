package Engine.Tasks;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.DTO.TargetForWorkerDTO;
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

    //gets a set of missionDTOs and finds a target, returns it
    public synchronized TargetForWorkerDTO getTargetFromSetOfMissions(Set<String> set) {
        for (String s : set) { //going through every mission
            MissionDTOWithoutCB m = getMissionByName(s);
            for (TargetDTOWithoutCB t : targetsMap.get(s).values()) { //trying to find a viable target in a mission
                if (t.getTargetLocation().equals(Location.INDEPENDENT) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    t.setTargetState(State.WAITING);
                    return new TargetForWorkerDTO(m.getAmountOfTargets(), m.getSrc(), m.getCompilationFolder(), m.getRunTime(), m.isRandomRunTime(), m.getSuccess(),
                            m.getSuccessWithWarnings(), t);
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(s).values()) { //trying to find a viable target in a mission
                if (t.getTargetLocation().equals(Location.LEAF) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    t.setTargetState(State.WAITING);
                    return new TargetForWorkerDTO(m.getAmountOfTargets(), m.getSrc(), m.getCompilationFolder(), m.getRunTime(), m.isRandomRunTime(), m.getSuccess(),
                            m.getSuccessWithWarnings(), t);
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(s).values()) { //trying to find a viable target in a mission
                if (t.getTargetLocation().equals(Location.MIDDLE) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    t.setTargetState(State.WAITING);
                    return new TargetForWorkerDTO(m.getAmountOfTargets(), m.getSrc(), m.getCompilationFolder(), m.getRunTime(), m.isRandomRunTime(), m.getSuccess(),
                            m.getSuccessWithWarnings(), t);
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(s).values()) { //trying to find a viable target in a mission
                if (t.getTargetLocation().equals(Location.ROOT) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    t.setTargetState(State.WAITING);
                    return new TargetForWorkerDTO(m.getAmountOfTargets(), m.getSrc(), m.getCompilationFolder(), m.getRunTime(), m.isRandomRunTime(), m.getSuccess(),
                            m.getSuccessWithWarnings(), t);
                }
            }
        }
        return null;
    }

//    public synchronized ----- getTargetDTOForWorker(String tName, String mName, String wName){
//
//    }

    public synchronized MissionDTOWithoutCB getMissionForWorker(String wName, String mName) {
        return getMissionByNameFromList(workersMissionsMap.get(wName), mName);
    }

    public synchronized void updateTarget(String mName, TargetDTOWithoutCB t) {
        getMissionByName(mName).setWaitingTargets(getMissionByName(mName).getWaitingTargets() - 1);
        getMissionByName(mName).setExecutedTargets(getMissionByName(mName).getExecutedTargets() + 1);
        getMissionByName(mName).setProgressCounter();
        targetsMap.get(mName).get(t.getTargetName()).setTargetState(t.getTargetState());
        targetsMap.get(mName).get(t.getTargetName()).setTargetTime(t.getTargetTime());
        //set all req for skipped.
        if (t.getTargetState().equals(State.FINISHED_FAILURE)) {
            setAllReqForSkipped(mName, targetsMap.get(mName).get(t.getTargetName()));
        }
        //TODO if mission finished, remove worker from it?
    }

    public synchronized List<TargetForWorkerDTO> getTargetForWorkerDTO(String wName) { //gets a list of all active targets for the current worker
        List<TargetForWorkerDTO> res = new ArrayList<>();
        for (MissionDTOWithoutCB m : workersMissionsMap.get(wName)) {
            for (TargetDTOWithoutCB temp : targetsMap.get(m.getMissionName()).values()) {
                if (temp.getTargetState().equals(State.WAITING)) {
                    temp.setTargetState(State.IN_PROCESS);
                    TargetForWorkerDTO toAdd = new TargetForWorkerDTO(m.getMissionName(), m.getStatus(), temp.getTargetName(), temp.getTargetState().toString(), 0);
                    res.add(toAdd);
                }
                if (temp.getTargetState().equals(State.FINISHED_SUCCESS) ||
                        temp.getTargetState().equals(State.FINISHED_WARNINGS) ||
                        temp.getTargetState().equals(State.FINISHED_FAILURE)) {
                    TargetForWorkerDTO toAdd = new TargetForWorkerDTO(m.getMissionName(), m.getStatus(), temp.getTargetName(), temp.getTargetState().toString(), 0);
                    res.add(toAdd);
                }
            }
        }
        return res;
    }

    private void setAllReqForSkipped(String mName, TargetDTOWithoutCB t) {
        for (String s : t.getTargetRequiredFor()) {
            setAllReqForSkipped(mName, targetsMap.get(mName).get(s));
            if (!targetsMap.get(mName).get(s).getTargetState().equals(State.SKIPPED)) {
                targetsMap.get(mName).get(s).setTargetState(State.SKIPPED);
                getMissionByName(mName).setProgressCounter();
            }
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
        if (workersMissionsMap.containsKey(wName)) {
            for (MissionDTOWithoutCB m : workersMissionsMap.get(wName)) {
                m.setWorkers(m.getWorkers() - 1);
                workersMissionsMap.get(wName).remove(m);
            }
        }
    }

    public synchronized TargetDTOWithoutCB getAvailableTargetForWorker(String wName) {
        for (MissionDTOWithoutCB m : workersMissionsMap.get(wName)) {
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.INDEPENDENT) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.LEAF) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.MIDDLE) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
                    return t;
                }
            }
            for (TargetDTOWithoutCB t : targetsMap.get(m.getMissionName()).values()) {
                if (t.getTargetLocation().equals(Location.ROOT) && t.getTargetState().equals(State.FROZEN)) {
                    m.setWaitingTargets(m.getWaitingTargets() + 1);
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
        if (getMissionByName(task.getMissionName()) != null) {
            taskList.add(task);
            targetsMap.put(task.getMissionName(), targets);
        }
    }

    public synchronized void addTaskFromScratch(String name, String newName, String creatorName, Map<String, TargetDTOWithoutCB> targets) {
        if (getMissionByName(newName) != null) {
            MissionDTOWithoutCB original = getMissionByName(name);
            MissionDTOWithoutCB temp = new MissionDTOWithoutCB(original.getAmountOfTargets(), original.getTargets(), original.getSrc(), original.getCompilationFolder(), original.getRunTime(), original.isRandomRunTime(),
                    original.getSuccess(), original.getSuccessWithWarnings(), newName, MissionState.READY.toString(), 0, 0, original.getTotalPrice(), creatorName, original.getGraphName(), 0, 0,
                    original.getIndependenceCount(), original.getLeafCount(), original.getMiddleCount(), original.getRootCount());
            taskList.add(temp);
            targetsMap.put(newName, targets);
        }
    }

    public synchronized void addTaskIncremental(String name, String newName, String creatorName) {
        if (getMissionByName(newName) != null) {
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
