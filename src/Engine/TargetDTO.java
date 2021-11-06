package Engine;

import java.util.Set;

public class TargetDTO {
    private final String targetName;
    private final Location targetLocation;
    private final Set<Graph.Target> targetDependsOn;
    private final Set<Graph.Target> targetRequiredFor;
    private final String targetInfo;

    public TargetDTO(Graph.Target t) {
        targetName = t.getName();
        targetLocation = t.getLocation();
        targetDependsOn = t.getDependsOn();
        targetRequiredFor = t.getRequiredFor();
        targetInfo = t.getInfo();
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
}
