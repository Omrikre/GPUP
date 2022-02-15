package Engine.DTO;

import Engine.Enums.MissionState;
import javafx.scene.control.CheckBox;

public class MissionDTO {
    private String missionName;
    private MissionState status;
    private Integer progress;
    private Integer workers;
    private Integer totalPrice;
    private String creatorName;
    private String graphName;
    private GraphDTO missionGraph;
    private Integer executedTargets;
    private Integer waitingTargets;
    private CheckBox selectedState;

    public MissionDTO(String missionName, MissionState status, Integer progress, Integer workers, Integer totalPrice, String creatorName, String graphName, GraphDTO missionGraph, Integer executedTargets, Integer waitingTargets, CheckBox selectedState) {
        this.missionName = missionName;
        this.status = status;
        this.progress = progress;
        this.workers = workers;
        this.totalPrice = totalPrice;
        this.creatorName = creatorName;
        this.graphName = graphName;
        this.missionGraph = missionGraph;
        this.executedTargets = executedTargets;
        this.waitingTargets = waitingTargets;
        this.selectedState = selectedState;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public MissionState getStatus() {
        return status;
    }

    public void setStatus(MissionState status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getWorkers() {
        return workers;
    }

    public void setWorkers(Integer workers) {
        this.workers = workers;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public GraphDTO getMissionGraph() {
        return missionGraph;
    }

    public void setMissionGraph(GraphDTO missionGraph) {
        this.missionGraph = missionGraph;
    }

    public Integer getExecutedTargets() {
        return executedTargets;
    }

    public void setExecutedTargets(Integer executedTargets) {
        this.executedTargets = executedTargets;
    }

    public Integer getWaitingTargets() {
        return waitingTargets;
    }

    public void setWaitingTargets(Integer waitingTargets) {
        this.waitingTargets = waitingTargets;
    }

    public CheckBox getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(CheckBox selectedState) {
        this.selectedState = selectedState;
    }
}
