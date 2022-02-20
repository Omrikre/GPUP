package components.missions.createNewMission;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class NewMissionCreatorController {

    @FXML private Label MsgLB;
    @FXML private Button createBT;
    @FXML private TextField missionNameTF;

    private boolean createNewMissionSucceed;
    private AppController mainController;


    @FXML public void initialize() {
        cleanup();
    }

    public void setMainController(AppController controller) { mainController = controller; }

    private void sendNewMissionRequest() {
        createBT.setDisable(true);
        if(createNewMissionSucceed)
            this.mainController.closeCreateNewMissionWin();

        //TODO - make the request
        changeBTToOK(true);
    }

    public void cleanup() {
        changeBTToOK(false);
        MsgLB.setText("");
    }

    private void changeBTToOK(boolean bool) {
        if (bool) {
            createBT.setText("OK");
            createBT.setDefaultButton(true);
            createBT.setDisable(false);
            createNewMissionSucceed = true;
        }
        else {
            createBT.setText("Create");
            createBT.setDefaultButton(false);
            createBT.setDisable(false);
            createNewMissionSucceed = false;
        }
        missionNameTF.requestFocus();
        missionNameTF.clear();
    }

    @FXML void createPR(ActionEvent event) {
        sendNewMissionRequest();
    }

    public void setupData(boolean isDup, boolean isFromScratch) {
    } //TODO - add all the other data
}
