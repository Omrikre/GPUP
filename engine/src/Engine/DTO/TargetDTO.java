package Engine.DTO;

import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Graph;

import java.sql.Time;
import java.util.Set;

public class TargetDTO {
    private final String targetName;
    private final Location targetLocation;
    private final Set<Graph.Target> targetDependsOn;
    private final Set<Graph.Target> targetRequiredFor;
    private final String targetInfo;
    private final State targetState;

    public TargetDTO(Graph.Target t) {
        this.targetName = t.getName();
        this.targetLocation = t.getLocation();
        this.targetDependsOn = t.getDependsOn();
        this.targetRequiredFor = t.getRequiredFor();
        this.targetInfo = t.getInfo();
        this.targetState = t.getState();
    }

    public String getTargetName() {
        return targetName;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public Set<Graph.Target> getTargetDependsOn() {
        return targetDependsOn;
    }

    public Set<Graph.Target> getTargetRequiredFor() {
        return targetRequiredFor;
    }

    public String getTargetInfo() {
        return targetInfo;
    }

    public State getTargetState() {
        return targetState;
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
                ", targetTime=" +
                '}';
    }
}
