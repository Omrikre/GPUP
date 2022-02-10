package components.graphManager.graphHeader;

import components.app.AppController;
import components.graphManager.GraphController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GraphHeaderController {

    @FXML private Button XMLBT;
    @FXML private Button graphBT;
    @FXML private Button targetsBT;

    private GraphController graphParentController;

    @FXML void XMLPR(ActionEvent event) { graphParentController.showXMLManagerPane(); }
    @FXML void graphPR(ActionEvent event) { graphParentController.showGraphInfoPane(); }
    @FXML void targetsPR(ActionEvent event) { graphParentController.showTargetsInfoPane(); }

    @FXML public void initialize() {
        makeButtonsDisable(false);
    }

    public void makeButtonsDisable(boolean bool) {
        graphBT.setDisable(bool);
        targetsBT.setDisable(bool);
    }

    public void setGraphParentController(GraphController graphController) { this.graphParentController = graphController;}


}
