package components.header;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;




public class HeaderButtonsController {

    private AppController mainController;

    @FXML private Button homeBt;
    @FXML private Button dashboardBT;
    @FXML private Button missionsBT;
    @FXML private Button GraphBT;
    @FXML private Button settingsBt;
    @FXML private Button chatBT;


    @FXML public void initialize() {
        homeBt.setDefaultButton(true);
        chatBT.setDefaultButton(true);
        makeAllButtonsDisable(true);
    }
    public void makeAllButtonsDisable(boolean bool) {
        missionsBT.setDisable(bool);
        GraphBT.setDisable(bool);
        dashboardBT.setDisable(bool);
        chatBT.setDisable(bool);
    }

    @FXML void homePr(ActionEvent event) { mainController.showHomePane(); }
    @FXML void dashboardPR(ActionEvent event) { mainController.showDashboardPane(); }
    @FXML void missionsPR(ActionEvent event) { mainController.showMissionsPane(); }
    @FXML void GraphPR(ActionEvent event) { mainController.showGraphPane(); }
    @FXML void settingsPr(ActionEvent event) { mainController.showSettingsPane(); }
    @FXML void ChatPR(ActionEvent event) { mainController.showChatPane(); }



    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
