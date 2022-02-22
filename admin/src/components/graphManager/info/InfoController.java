package components.graphManager.info;

import Engine.DTO.GraphDTO;
import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import components.graphManager.GraphController;
import components.graphManager.info.generalInfo.generalInfoController;
import components.graphManager.info.targetInfo.targetInfoController;
import components.graphManager.info.treeViewInfo.treeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class InfoController {

    @FXML private Tab generalInfoTab;
    @FXML private Tab infoByTargetTab;
    @FXML private Tab treeViewTab;

    @FXML private HBox generalInfoGP;
    @FXML private generalInfoController generalInfoGPController;
    @FXML private VBox infoByTargetGP;
    @FXML private targetInfoController infoByTargetGPController;

    private GraphController parentController;
    private GraphDTO graph;
    private List<TargetDTOWithoutCB> targets;


    public void setParentController(GraphController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        infoByTargetGPController.setParentController(this);
    }

    public void setupData(GraphDTO graph, List<TargetDTOWithoutCB> targets) {

        this.targets = targets;
        this.graph = graph;
        if(generalInfoGPController != null) {
            generalInfoGPController.setupData(graph, targets); //TODO
        } else System.out.println("null fuck 1");
        if(infoByTargetGPController != null) {
            infoByTargetGPController.resetData();
            infoByTargetGPController.setChoiceTargetBox(targets);
        } else System.out.println("null fuck 2");

    }
    public TargetDTOWithoutCB getTargetDTO(String targetName) {
        for (TargetDTOWithoutCB t : targets) {
            if (t.getTargetName() == targetName)
                return t;
        }
        System.out.println("error infoController");
        return null;
    }
}