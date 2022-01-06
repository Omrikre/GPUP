package components.info;

import Engine.DTO.TargetDTO;
import components.app.AppController;
import components.header.HeaderButtonsController;
import components.info.generalInfo.generalInfoController;
import components.info.targetInfo.targetInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import static components.app.CommonResourcesPaths.*;

public class InfoController {

    @FXML private Tab generalInfoTab;
    @FXML private Tab infoByTargetTab;

    @FXML private GridPane generalInfoGP;
    @FXML private generalInfoController generalInfoGPController;
    @FXML private GridPane infoByTargetGP;
    @FXML private targetInfoController infoByTargetGPController;

    private AppController mainController;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    @FXML public void initialize() {
    }
    private void setMainInSubComponents() {
        if (generalInfoGPController != null) {
        }
    }

    public void setupData() {
        if(generalInfoGPController != null) {
            generalInfoGPController.setNumOfTargets(mainController.getNumOfTargets());
            generalInfoGPController.setTable(mainController.getGeneralInfoTable());
        }
        else {
            System.out.println("null fuck 1");
        }
        if(infoByTargetGPController != null) {
            infoByTargetGPController.setChoiceTargetBox(mainController.getTargetList());
        }
        else {
            System.out.println("null fuck 2");
        }
    }
    public void getTargetDTO(String targetName) {
        TargetDTO dto = mainController.getTargetDTO(targetName);

    }
}
