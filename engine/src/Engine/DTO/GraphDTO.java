package Engine.DTO;

import javafx.scene.control.CheckBox;

import java.util.Map;


public class GraphDTO {
    // members
    private String graphName;
    private String uploadByAdminName;
    private Map<String, Integer> pricePerTarget;
    private Integer independenceCount;
    private Integer leafCount;
    private Integer middleCount;
    private Integer rootCount;
   // private CheckBox selectedState;

    public GraphDTO(String graphName, String uploadByAdminName, Map<String,Integer> pricePerTarget, Integer independenceCount, Integer leafCount, Integer middleCount, Integer rootCount) {
        this.graphName = graphName;
        this.uploadByAdminName = uploadByAdminName;
        this.pricePerTarget = pricePerTarget;
        this.independenceCount = independenceCount;
        this.leafCount = leafCount;
        this.middleCount = middleCount;
        this.rootCount = rootCount;
        //this.selectedState=selectedState;
    }

//    public CheckBox getSelectedState() {
//        return selectedState;
//    }
//
//    public void setSelectedState(CheckBox selectedState) {
//        this.selectedState = selectedState;
//    }

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

    public Map<String,Integer> getPricePerTarget() {
        return pricePerTarget;
    }

    public void setPricePerTarget(Map<String, Integer> pricePerTarget) {
        this.pricePerTarget = pricePerTarget;
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
