package Engine.DTO;


public class TargetForWorkerDTO {
    private String missionName;
    private String taskType;
    private String targetName;
    private String status;
    private Integer credits;

    public TargetForWorkerDTO(String missionName, String taskType, String targetName, String status, Integer credits) {
        this.missionName = missionName;
        this.taskType = taskType;
        this.targetName = targetName;
        this.status = status;
        this.credits = credits;
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
