package components.info;

import Engine.DTO.TargetDTO;
import components.app.AppController;
import components.info.generalInfo.generalInfoController;
import components.info.targetInfo.targetInfoController;
import components.info.treeViewInfo.treeViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class InfoController {

    @FXML private Tab generalInfoTab;
    @FXML private Tab infoByTargetTab;
    @FXML private Tab treeViewTab;


    @FXML private HBox generalInfoGP;
    @FXML private generalInfoController generalInfoGPController;
    @FXML private VBox infoByTargetGP;
    @FXML private targetInfoController infoByTargetGPController;
    @FXML private BorderPane treeViewBP;
    @FXML private treeViewController treeViewBPController;

    private AppController mainController;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    @FXML public void initialize() {
        treeViewBPController.setParentController(this);
        infoByTargetGPController.setParentController(this);
    }

    public void setupData() {
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
}
