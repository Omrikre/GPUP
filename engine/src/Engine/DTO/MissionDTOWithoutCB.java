package Engine.DTO;

import Engine.Enums.MissionState;
import javafx.scene.control.CheckBox;
import Engine.Engine;
import Engine.Enums.MissionState;
import Engine.Graph;
import javafx.scene.control.CheckBox;

public class MissionDTOWithoutCB {

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

    public MissionDTOWithoutCB(int amountOfTargets, String compilationFolder, int runTime, boolean randomRunTime, int success, int successWithWarnings, String missionName, String status, int progress, int workers, int totalPrice, String creatorName, String graphName, int executedTargets, int waitingTargets) {
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
        this.executedTargets = executedTargets;
        this.waitingTargets = waitingTargets;
    }

    public int getAmountOfTargets() {
        return amountOfTargets;
    }

    public void setAmountOfTargets(int amountOfTargets) {
        this.amountOfTargets = amountOfTargets;
    }

    public String getCompilationFolder() {
        return compilationFolder;
    }

    public void setCompilationFolder(String compilationFolder) {
        this.compilationFolder = compilationFolder;
    }

    public int getRunTime() {
        return runTime;
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getSuccessWithWarnings() {
        return successWithWarnings;
    }

    public void setSuccessWithWarnings(int successWithWarnings) {
        this.successWithWarnings = successWithWarnings;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getWorkers() {
        return workers;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
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

    public int getExecutedTargets() {
        return executedTargets;
    }

    public void setExecutedTargets(int executedTargets) {
        this.executedTargets = executedTargets;
    }

    public int getWaitingTargets() {
        return waitingTargets;
    }

    public void setWaitingTargets(int waitingTargets) {
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


