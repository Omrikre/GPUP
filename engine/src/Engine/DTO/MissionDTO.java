package Engine.DTO;

import Engine.Engine;
import Engine.Enums.MissionState;
import Engine.Graph;
import javafx.scene.control.CheckBox;

public class MissionDTO {
    //simulation/compilation infos:
    private Integer amountOfTargets;
    private String compilationFolder; //for compilation task, else null
    private Integer runTime;
    private Boolean randomRunTime;
    private Integer success;
    private Integer successWithWarnings;
    //the rest are for the display:
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

    public MissionDTO(Integer amountOfTargets, String compilationFolder, Integer runTime, Boolean randomRunTime, Integer success, Integer successWithWarnings, String missionName, MissionState status, Integer progress, Integer workers, Integer totalPrice, String creatorName, String graphName, GraphDTO missionGraph, Integer executedTargets, Integer waitingTargets, CheckBox selectedState) {
        this.amountOfTargets = amountOfTargets;
        this.compilationFolder = compilationFolder;
        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.success = success;
        this.successWithWarnings = successWithWarnings;
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

    public Integer getAmountOfTargets() {
        return amountOfTargets;
    }

    public void setAmountOfTargets(Integer amountOfTargets) {
        this.amountOfTargets = amountOfTargets;
    }

    public String getCompilationFolder() {
        return compilationFolder;
    }

    public void setCompilationFolder(String compilationFolder) {
        this.compilationFolder = compilationFolder;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Boolean getRandomRunTime() {
        return randomRunTime;
    }

    public void setRandomRunTime(Boolean randomRunTime) {
        this.randomRunTime = randomRunTime;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getSuccessWithWarnings() {
        return successWithWarnings;
    }

    public void setSuccessWithWarnings(Integer successWithWarnings) {
        this.successWithWarnings = successWithWarnings;
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
