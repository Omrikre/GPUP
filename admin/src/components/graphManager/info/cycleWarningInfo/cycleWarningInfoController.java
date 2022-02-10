package components.graphManager.info.cycleWarningInfo;

import components.app.AppController;
import components.graphManager.GraphController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class cycleWarningInfoController {

    @FXML private Button OKBt;
    private GraphController parentController;


    public void setMainController(GraphController parentController) {
        this.parentController = parentController;
    }
    @FXML void OkPr(ActionEvent event) { parentController.closeCycleWarning(); }


    public void setParentController(GraphController graphController) {
    }
}
