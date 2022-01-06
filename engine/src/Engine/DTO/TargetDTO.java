package Engine.DTO;

import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Graph;

import java.util.Set;

public class TargetDTO {
    // members
    private String targetName;
    private Location targetLocation;
    private Set<String> targetDependsOn;
    private Set<String> targetRequiredFor;
    private String targetInfo;
    private State targetState;
    private long targetTime;

    // c'tor
    public TargetDTO(Graph.Target t) {
        this.targetName = t.getName();
        this.targetLocation = t.getLocation();
        this.targetDependsOn = t.getDependsOn();
        this.targetRequiredFor = t.getRequiredFor();
        this.targetInfo = t.getInfo();
        this.targetState = t.getState();
        this.targetTime = t.getTime();
    }

    // getters
    public String getTargetName() {
        return targetName;
    }
    public Location getTargetLocation() {
        return targetLocation;
    }
    public Set<String> getTargetDependsOn() {
        return targetDependsOn;
    }
    public Set<String> getTargetRequiredFor() {
        return targetRequiredFor;
    }
    public String getTargetInfo() {
        return targetInfo;
    }
    public State getTargetState() {
        return targetState;
    }
    public long getTargetTime() {
        return targetTime;
    }

    // setters
    public void setTargetName(String targetName) { this.targetName = targetName; }
    public void setTargetLocation(Location targetLocation) { this.targetLocation = targetLocation; }
    public void setTargetDependsOn(Set<String> targetDependsOn) { this.targetDependsOn = targetDependsOn; }
    public void setTargetRequiredFor(Set<String> targetRequiredFor) { this.targetRequiredFor = targetRequiredFor; }
    public void setTargetInfo(String targetInfo) { this.targetInfo = targetInfo; }
    public void setTargetState(State targetState) { this.targetState = targetState; }
    public void setTargetTime(long targetTime) { this.targetTime = targetTime; }

    // toString
    @Override
    public String toString() {
        return "TargetDTO{" +
                "targetName='" + targetName + '\'' +
                ", targetLocation=" + targetLocation +
                ", targetDependsOn=" + targetDependsOn +
                ", targetRequiredFor=" + targetRequiredFor +
                ", targetInfo='" + targetInfo + '\'' +
                ", targetState=" + targetState +
                ", targetTime=" + targetTime +
                '}';
    }
}
