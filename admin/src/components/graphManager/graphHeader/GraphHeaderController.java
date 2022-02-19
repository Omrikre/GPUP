package components.graphManager.graphHeader;

import components.graphManager.GraphController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GraphHeaderController {

    @FXML private Button XMLBT;
    @FXML private Button graphBT;
    @FXML private Button targetsBT;
    @FXML private Button missionCreatorBT;

    private GraphController graphParentController;

    @FXML void XMLPR(ActionEvent event) { graphParentController.showXMLManagerPane(); }
    @FXML void graphPR(ActionEvent event) { graphParentController.showGraphInfoPane(); }
    @FXML void targetsPR(ActionEvent event) { graphParentController.showTargetsInfoPane(); }
    @FXML void missionCreatorPR(ActionEvent event) { graphParentController.showMissionCreatorPane(); }

    @FXML public void initialize() {
        makeButtonsDisable(false);
    }

    public void makeButtonsDisable(boolean bool) {
        graphBT.setDisable(bool);
        targetsBT.setDisable(bool);
        missionCreatorBT.setDisable(bool);
    }

    public void setGraphParentController(GraphController graphController) { this.graphParentController = graphController;}


}
