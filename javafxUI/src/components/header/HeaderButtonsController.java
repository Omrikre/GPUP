package components.header;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;




public class HeaderButtonsController {

    private AppController mainController;

    @FXML private Button homeBt;
    @FXML private Button infoBt;
    @FXML private Button graphBr;
    @FXML private Button taskBT;
    @FXML private Button settingsBt;
    @FXML private Button pathBt;

    @FXML public void initialize() {
        homeBt.setDefaultButton(true);
        infoBt.setDisable(true);
        graphBr.setDisable(true);
        taskBT.setDisable(true);
        pathBt.setDisable(true);
    }
    public void makeAllButtonsAble() {
        infoBt.setDisable(false);
        graphBr.setDisable(false);
        taskBT.setDisable(false);
        pathBt.setDisable(false);
    }

    @FXML void graphPr(ActionEvent event) { }
    @FXML void homePr(ActionEvent event) { mainController.showXMLLoadPane(); }
    @FXML void infoPr(ActionEvent event) { mainController.showInfoPane(); }
    @FXML void settingsPr(ActionEvent event) { mainController.showSettingsPane(); }
    @FXML void taskPr(ActionEvent event) {  mainController.showTaskPane(); }
    @FXML void pathPr(ActionEvent event) { mainController.showTablePane(); }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
