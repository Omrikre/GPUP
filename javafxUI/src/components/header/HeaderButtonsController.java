package components.header;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;




public class HeaderButtonsController {

    private AppController mainController;

    @FXML private Button homeBt;
    @FXML private Button infoBt;
    //@FXML private Button graphBt;
    @FXML private Button taskBt;
    @FXML private Button settingsBt;
    @FXML private Button tableBt;

    @FXML public void initialize() {
        homeBt.setDefaultButton(true);
        infoBt.setDisable(true);
        //graphBt.setDisable(true);
        taskBt.setDisable(true);
        tableBt.setDisable(true);
    }
    public void makeAllButtonsAble() {
        infoBt.setDisable(false);
        //graphBt.setDisable(false);
        taskBt.setDisable(false);
        tableBt.setDisable(false);
    }

    //@FXML void graphPr(ActionEvent event) { }
    @FXML void homePr(ActionEvent event) { mainController.showXMLLoadPane(); }
    @FXML void infoPr(ActionEvent event) { mainController.showInfoPane();
        //infoBt.setStyle("-fx-background-color: #1e81b0");
    }
    @FXML void settingsPr(ActionEvent event) { mainController.showSettingsPane(); }
    @FXML void taskPr(ActionEvent event) {  mainController.showTaskPane();
        //infoBt.setStyle("-fx-background-color: linear-gradient(#61a2b1, #2A5058)");
        //infoBt.setTextFill("");
    }
    @FXML void pathPr(ActionEvent event) { mainController.showTablePane(); }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
