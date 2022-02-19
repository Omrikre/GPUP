package Engine.DTO;

import Engine.Engine;
import Engine.Enums.MissionState;
import Engine.Graph;
import javafx.scene.control.CheckBox;

public class MissionDTO {
    //simulation/compilation infos:
    private int amountOfTargets;
    private String compilationFolder; //for compilation task, else null
    private int runTime;
    private boolean randomRunTime;
    private int success;
    private int successWithWarnings;
    //the rest are for the display:
    private String missionName;
    private String status;
    private int progress;
    private int workers;
    private int totalPrice;
    private String creatorName;
    private String graphName;
    private int executedTargets;
    private int waitingTargets;
    private CheckBox selectedState;

    public MissionDTO(Integer amountOfTargets, String compilationFolder, Integer runTime, Boolean randomRunTime, Integer success, Integer successWithWarnings, String missionName, MissionState status, Integer progress, Integer workers, Integer totalPrice, String creatorName, String graphName, Integer executedTargets, Integer waitingTargets) {
        this.amountOfTargets = amountOfTargets;
        this.compilationFolder = compilationFolder;
        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.success = success;
        this.successWithWarnings = successWithWarnings;
        this.missionName = missionName;
        this.status = status.toString();
        this.progress = progress;
        this.workers = workers;
        this.totalPrice = totalPrice;
        this.creatorName = creatorName;
        this.graphName = graphName;
        this.executedTargets = executedTargets;
        this.waitingTargets = waitingTargets;
        this.selectedState = selectedState;
    }

    public void setAmountOfTargets(int amountOfTargets) {
        this.amountOfTargets = amountOfTargets;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public boolean isRandomRunTime() {
        return randomRunTime;
    }

    public void setRandomRunTime(boolean randomRunTime) {
        this.randomRunTime = randomRunTime;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setSuccessWithWarnings(int successWithWarnings) {
        this.successWithWarnings = successWithWarnings;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setExecutedTargets(int executedTargets) {
        this.executedTargets = executedTargets;
    }

    public void setWaitingTargets(int waitingTargets) {
        this.waitingTargets = waitingTargets;
    }

    public CheckBox getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(CheckBox selectedState) {
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(MissionState status) {
        this.status = status.toString();
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


    @Override
    public String toString() {
        return "MissionDTO{" +
                "amountOfTargets=" + amountOfTargets +
                ", compilationFolder='" + compilationFolder + '\'' +
                ", runTime=" + runTime +
                ", randomRunTime=" + randomRunTime +
                ", success=" + success +
                ", successWithWarnings=" + successWithWarnings +
                ", missionName='" + missionName + '\'' +
                ", status=" + status +
                ", progress=" + progress +
                ", workers=" + workers +
                ", totalPrice=" + totalPrice +
                ", creatorName='" + creatorName + '\'' +
                ", graphName='" + graphName + '\'' +
                ", executedTargets=" + executedTargets +
                ", waitingTargets=" + waitingTargets +
                '}';
    }
}
