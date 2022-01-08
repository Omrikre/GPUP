package Engine.DTO;

import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Engine.Graph;
import javafx.scene.control.CheckBox;

import java.util.Set;

public class TargetDTO {
    // members
    private String targetName; // +
    private Location targetLocation;
    private String targetLocationString; // +
    private Set<String> targetDependsOn;
    private int targetDependsOnNum; // +
    private Set<String> targetRequiredFor;
    private int targetRequiredForNum; // +
    private String targetInfo; // +
    private State targetState;
    private String targetStateString; // +
    private long targetTime;
    private CheckBox selectedState;
    private int serialSetsBelongs; //how many serial sets the target is in
    private int totalDependencies;
    private int totalRequierments;

    // constructor
    public TargetDTO(Graph.Target t) {
        this.targetName = t.getName();
        this.targetLocation = t.getLocation();
        this.targetDependsOn = t.getDependsOn();
        this.targetRequiredFor = t.getRequiredFor();
        this.targetInfo = t.getInfo();
        this.targetState = t.getState();
        this.targetTime = t.getTime();
        this.serialSetsBelongs = t.getSerialSetsBelongs();
        this.totalDependencies = t.getNumberOfBonds(Bond.DEPENDS_ON);
        this.totalRequierments = t.getNumberOfBonds(Bond.REQUIRED_FOR);

        // stupid verbs for javaFX tables
        this.targetLocationString = targetLocation.toString();
        this.targetDependsOnNum = targetDependsOn.size();
        this.targetRequiredForNum = targetRequiredFor.size();
        this.targetStateString = targetState.toString();
        this.selectedState = null;
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

    public String getTargetLocationString() {
        return targetLocationString;
    }

    public int getTargetDependsOnNum() {
        return targetDependsOnNum;
    }

    public int getTargetRequiredForNum() {
        return targetRequiredForNum;
    }

    public String getTargetStateString() {
        return targetStateString;
    }

    public CheckBox getSelectedState() {
        return selectedState;
    }

    public int getSerialSetsBelongs() {
        return serialSetsBelongs;
    }

    // setters
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public void setTargetDependsOn(Set<String> targetDependsOn) {
        this.targetDependsOn = targetDependsOn;
    }

    public void setTargetRequiredFor(Set<String> targetRequiredFor) {
        this.targetRequiredFor = targetRequiredFor;
    }

    public void setTargetInfo(String targetInfo) {
        this.targetInfo = targetInfo;
    }

    public void setTargetState(State targetState) {
        this.targetState = targetState;
    }

    public void setTargetTime(long targetTime) {
        this.targetTime = targetTime;
    }

    public void setTargetLocationString(String targetLocationString) {
        this.targetLocationString = targetLocationString;
    }

    public void setTargetDependsOnNum(int targetDependsOnNum) {
        this.targetDependsOnNum = targetDependsOnNum;
    }

    public void setTargetRequiredForNum(int targetRequiredForNum) {
        this.targetRequiredForNum = targetRequiredForNum;
    }

    public void setTargetStateString(String targetStateString) {
        this.targetStateString = targetStateString;
    }

    public void setSelectedState(CheckBox selectedState) {
        this.selectedState = selectedState;
    }

    public void setSerialSetsBelongs(int num) {
        serialSetsBelongs = num;
    }

    // methods
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
                ", serialSetsBelongs=" + serialSetsBelongs +
                '}';
    }

    public boolean isRoot() {
        return targetLocation == Location.ROOT;
    }

    public boolean isLeaf() {
        return targetLocation == Location.LEAF;
    }

    public boolean isIndependent() {
        return targetLocation == Location.INDEPENDENT;
    }

}
