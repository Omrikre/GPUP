package components.graphManager.info.cycleWarningInfo;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class cycleWarningInfoController {

    @FXML private Button OKBt;
    private AppController mainController;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    @FXML void OkPr(ActionEvent event) { mainController.closeCycleWarning(); }

}
