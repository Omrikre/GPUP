package Engine.DTO;

public class MissionDTOWithoutCB {

    //simulation/compilation infos:
    private Integer amountOfTargets;
    private String src;
    private String compilationFolder; //for compilation task, else null
    private Integer runTime;
    private boolean randomRunTime;
    private Integer success;
    private Integer successWithWarnings;
    //the rest are for the display:
    private String missionName;
    private String status;
    private Integer progress;
    private Integer workers;
    private Integer totalPrice;
    private String creatorName;
    private String graphName;
    private Integer executedTargets;
    private Integer waitingTargets;
    private Integer independenceCount;
    private Integer leafCount;
    private Integer middleCount;
    private Integer rootCount;

    public MissionDTOWithoutCB(Integer amountOfTargets, String src, String compilationFolder, Integer runTime, boolean randomRunTime, Integer success, Integer successWithWarnings, String missionName, String status, Integer progress, Integer workers, Integer totalPrice, String creatorName, String graphName, Integer executedTargets, Integer waitingTargets, Integer independenceCount, Integer leafCount, Integer middleCount, Integer rootCount) {
        this.amountOfTargets = amountOfTargets;
        this.src=src;
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
        this.independenceCount = independenceCount;
        this.leafCount = leafCount;
        this.middleCount = middleCount;
        this.rootCount = rootCount;
    }

    public Integer getAmountOfTargets() {
        return amountOfTargets;
    }

    public void setAmountOfTargets(Integer amountOfTargets) {
        this.amountOfTargets = amountOfTargets;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    public boolean isRandomRunTime() {
        return randomRunTime;
    }

    public void setRandomRunTime(boolean randomRunTime) {
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
        return status;
    }

    public void setStatus(String status) {
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

    public Integer getIndependenceCount() {
        return independenceCount;
    }

    public void setIndependenceCount(Integer independenceCount) {
        this.independenceCount = independenceCount;
    }

    public Integer getLeafCount() {
        return leafCount;
    }

    public void setLeafCount(Integer leafCount) {
        this.leafCount = leafCount;
    }

    public Integer getMiddleCount() {
        return middleCount;
    }

    public void setMiddleCount(Integer middleCount) {
        this.middleCount = middleCount;
    }

    public Integer getRootCount() {
        return rootCount;
    }

    public void setRootCount(Integer rootCount) {
        this.rootCount = rootCount;
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


