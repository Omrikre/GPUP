package components.graphManager.info;

import Engine.DTO.GraphDTO;
import Engine.DTO.TargetDTO;
import components.graphManager.GraphController;
import components.graphManager.info.generalInfo.generalInfoController;
import components.graphManager.info.targetInfo.targetInfoController;
import components.graphManager.info.treeViewInfo.treeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class InfoController {

    @FXML
    private Tab generalInfoTab;
    @FXML
    private Tab infoByTargetTab;
    @FXML
    private Tab treeViewTab;

    @FXML
    private HBox generalInfoGP;
    @FXML
    private generalInfoController generalInfoGPController;
    @FXML
    private VBox infoByTargetGP;
    @FXML
    private targetInfoController infoByTargetGPController;
    @FXML
    private BorderPane treeViewBP;
    @FXML
    private treeViewController treeViewBPController;

    private GraphController parentController;
    private GraphDTO graph;


    public void setParentController(GraphController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        treeViewBPController.setParentController(this);
        infoByTargetGPController.setParentController(this);
    }

    public void setupData(GraphDTO graph) {
        this.graph = graph;
/*
        if(generalInfoGPController != null) {
            generalInfoGPController.setupData(mainController.getGeneralInfoTable(), mainController.getNumOfTargets(), mainController.getGraphContainsCycle(), mainController.getFileName(), mainController.getNumOfSets(), mainController.getSerialSets());
        } else System.out.println("null fuck 1");
        if(infoByTargetGPController != null) {
            infoByTargetGPController.resetData();
            infoByTargetGPController.setChoiceTargetBox(mainController.getTargetList());
        } else System.out.println("null fuck 2");
        if(treeViewBPController != null) {
            if (!mainController.getGraphContainsCycle()) {
                treeViewBPController.setTrees(mainController.getTargetList());
                treeViewTab.setDisable(false);
            }
            else {
                treeViewTab.setDisable(true);
            }
        } else System.out.println("null fuck 2");

    }
    public TargetDTO getTargetDTO(String targetName) { return mainController.getTargetDTO(targetName); }

    public String getSSByName(String name) { return mainController.getSerialSetByName(name).toString(); }
*/ //TODO - get data from engine
    }
}