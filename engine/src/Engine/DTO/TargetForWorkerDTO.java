package Engine.DTO;


public class TargetForWorkerDTO {
    private String missionName;
    private String taskType;
    private String targetName;
    private String status;
    private Integer credits;
    //additional info for running tasks on the target:
    private Integer amountOfTargets;
    private String src;
    private String compilationFolder; //for compilation task, else null
    private Integer runTime;
    private boolean randomRunTime;
    private Integer success;
    private Integer successWithWarnings;
    private TargetDTOWithoutCB t;

    public TargetForWorkerDTO(String missionName, String taskType, String targetName, String status, Integer credits) {
        this.missionName = missionName;
        this.taskType = taskType;
        this.targetName = targetName;
        this.status = status;
        this.credits = credits;
    }

    public TargetForWorkerDTO(Integer amountOfTargets, String src, String compilationFolder, Integer runTime, boolean randomRunTime, Integer success, Integer successWithWarnings, TargetDTOWithoutCB t) {
        this.amountOfTargets = amountOfTargets;
        this.src = src;
        this.compilationFolder = compilationFolder;
        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.success = success;
        this.successWithWarnings = successWithWarnings;
        this.t = t;
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

    public TargetDTOWithoutCB getT() {
        return t;
    }

    public void setT(TargetDTOWithoutCB t) {
        this.t = t;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}
