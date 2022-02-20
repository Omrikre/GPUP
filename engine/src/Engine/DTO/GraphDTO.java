package Engine.DTO;

import javafx.scene.control.CheckBox;

import java.util.Map;


public class GraphDTO {
    // members
    private String graphName;
    private String uploadByAdminName;
    private Integer simPricePerTarget;
    private Integer compPricePerTarget;
    private Integer independenceCount;
    private Integer leafCount;
    private Integer middleCount;
    private Integer rootCount;
    private CheckBox selectedState;
    private boolean containsCycle;


    public GraphDTO(String graphName, String uploadByAdminName, Integer simPricePerTarget, Integer compPricePerTarget, Integer independenceCount, Integer leafCount, Integer middleCount, Integer rootCount, CheckBox selectedState ,boolean containsCycle) {
        this.graphName = graphName;
        this.uploadByAdminName = uploadByAdminName;
        this.simPricePerTarget = simPricePerTarget;
        this.compPricePerTarget = compPricePerTarget;
        this.independenceCount = independenceCount;
        this.leafCount = leafCount;
        this.middleCount = middleCount;
        this.rootCount = rootCount;
        this.selectedState = selectedState;
        this.containsCycle = containsCycle;
    }

    public GraphDTO(String graphName, String username, int simulationPrice, int compilationPrice, Integer independenceCount, Integer leafCount, Integer middleCount, Integer rootCount) {
        this.graphName = graphName;
        this.uploadByAdminName = username;
        this.simPricePerTarget = simulationPrice;
        this.compPricePerTarget = compilationPrice;
        this.independenceCount = independenceCount;
        this.leafCount = leafCount;
        this.middleCount = middleCount;
        this.rootCount = rootCount;
        this.selectedState = null;
        this.containsCycle = true;
    }

    public boolean isContainsCycle() {
        return containsCycle;
    }

    public void setContainsCycle(boolean containsCycle) {
        this.containsCycle = containsCycle;
    }

    public Integer getSimPricePerTarget() {
        return simPricePerTarget;
    }

    public void setSimPricePerTarget(Integer simPricePerTarget) {
        this.simPricePerTarget = simPricePerTarget;
    }

    public Integer getCompPricePerTarget() {
        return compPricePerTarget;
    }

    public void setCompPricePerTarget(Integer compPricePerTarget) {
        this.compPricePerTarget = compPricePerTarget;
    }

    public CheckBox getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(CheckBox selectedState) {
        this.selectedState = selectedState;
    }

    public String getGraphName() {
        return graphName;
    }

    public void setGraphName(String graphName) {
        this.graphName = graphName;
    }

    public String getUploadByAdminName() {
        return uploadByAdminName;
    }

    public void setUploadByAdminName(String uploadByAdminName) {
        this.uploadByAdminName = uploadByAdminName;
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
}
