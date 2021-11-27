package Engine.DTO;

import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Graph;

import java.util.Set;

public class TargetDTO {
    private final String targetName;
    private final Location targetLocation;
    private final Set<String> targetDependsOn;
    private final Set<String> targetRequiredFor;
    private final String targetInfo;
    private final State targetState;
    private final long targetTime;

    public TargetDTO(Graph.Target t) {
        this.targetName = t.getName();
        this.targetLocation = t.getLocation();
        this.targetDependsOn = t.getDependsOn();
        this.targetRequiredFor = t.getRequiredFor();
        this.targetInfo = t.getInfo();
        this.targetState = t.getState();
        this.targetTime = t.getTime();
    }

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
