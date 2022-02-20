package Engine.DTO;

import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Graph;
import javafx.scene.control.CheckBox;

import java.util.Set;

public class TargetDTOWithoutCB {
    private String targetName;
    private Location targetLocation;
    private String targetLocationString; // +
    private Set<String> targetDependsOn;
    private int targetDependsOnNum; // +
    private Set<String> targetRequiredFor;
    private int targetRequiredForNum; // +
    private String targetInfo;
    private State targetState;
    private String targetStateString; // +
    private long targetTime;
    private int serialSetsBelongs; //how many serial sets the target is in
    private int totalDependencies;
    private int totalRequierments;


    public TargetDTOWithoutCB(String targetName, Location targetLocation, String targetLocationString, Set<String> targetDependsOn, int targetDependsOnNum, Set<String> targetRequiredFor, int targetRequiredForNum, String targetInfo, State targetState, String targetStateString, long targetTime, int serialSetsBelongs, int totalDependencies, int totalRequierments) {
        this.targetName = targetName;
        this.targetLocation = targetLocation;
        this.targetLocationString = targetLocationString;
        this.targetDependsOn = targetDependsOn;
        this.targetDependsOnNum = targetDependsOnNum;
        this.targetRequiredFor = targetRequiredFor;
        this.targetRequiredForNum = targetRequiredForNum;
        this.targetInfo = targetInfo;
        this.targetState = targetState;
        this.targetStateString = targetStateString;
        this.targetTime = targetTime;
        this.serialSetsBelongs = serialSetsBelongs;
        this.totalDependencies = totalDependencies;
        this.totalRequierments = totalRequierments;
    }

    public TargetDTOWithoutCB(Graph.Target t) {
        this.targetName = t.getName();
        this.targetLocation = t.getLocation();
        this.targetLocationString = t.getLocation().toString();
        this.targetDependsOn = t.getDependsOn();
        this.targetDependsOnNum = this.targetDependsOn.size();
        this.targetRequiredFor = t.getRequiredFor();
        this.targetRequiredForNum = this.targetRequiredFor.size();
        this.targetInfo = t.getInfo();
        this.targetState = t.getState();
        this.targetStateString = this.targetState.toString();
        this.targetTime = t.getTime();
        this.serialSetsBelongs = t.getSerialSetsBelongs();
        this.totalDependencies = t.getDependsOn().size();
        this.totalRequierments = t.getRequiredFor().size();
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getTargetLocationString() {
        return targetLocationString;
    }

    public void setTargetLocationString(String targetLocationString) {
        this.targetLocationString = targetLocationString;
    }

    public Set<String> getTargetDependsOn() {
        return targetDependsOn;
    }

    public void setTargetDependsOn(Set<String> targetDependsOn) {
        this.targetDependsOn = targetDependsOn;
    }

    public int getTargetDependsOnNum() {
        return targetDependsOnNum;
    }

    public void setTargetDependsOnNum(int targetDependsOnNum) {
        this.targetDependsOnNum = targetDependsOnNum;
    }

    public Set<String> getTargetRequiredFor() {
        return targetRequiredFor;
    }

    public void setTargetRequiredFor(Set<String> targetRequiredFor) {
        this.targetRequiredFor = targetRequiredFor;
    }

    public int getTargetRequiredForNum() {
        return targetRequiredForNum;
    }

    public void setTargetRequiredForNum(int targetRequiredForNum) {
        this.targetRequiredForNum = targetRequiredForNum;
    }

    public String getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(String targetInfo) {
        this.targetInfo = targetInfo;
    }

    public State getTargetState() {
        return targetState;
    }

    public void setTargetState(State targetState) {
        this.targetState = targetState;
    }

    public String getTargetStateString() {
        return targetStateString;
    }

    public void setTargetStateString(String targetStateString) {
        this.targetStateString = targetStateString;
    }

    public long getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(long targetTime) {
        this.targetTime = targetTime;
    }


    public int getSerialSetsBelongs() {
        return serialSetsBelongs;
    }

    public void setSerialSetsBelongs(int serialSetsBelongs) {
        this.serialSetsBelongs = serialSetsBelongs;
    }

    public int getTotalDependencies() {
        return totalDependencies;
    }

    public void setTotalDependencies(int totalDependencies) {
        this.totalDependencies = totalDependencies;
    }

    public int getTotalRequierments() {
        return totalRequierments;
    }

    public void setTotalRequierments(int totalRequierments) {
        this.totalRequierments = totalRequierments;
    }

    public boolean isRoot() {
        if(totalRequierments == 0)
            return true;
        else
            return false;
    }

    public boolean isLeaf() {
        if(totalDependencies == 0)
            return true;
        else
            return false;
    }

    public boolean isIndependent() {
        if (isLeaf() && isRoot())
            return true;
        else
            return false;
    }
}
