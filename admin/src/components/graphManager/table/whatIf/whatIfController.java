package components.graphManager.table.whatIf;

import components.graphManager.table.tableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class whatIfController {

    @FXML private VBox whatIfVBPane;
    // what if tab
    @FXML private Label WHATselectedTargetLabel;
    @FXML private Button WHATclearTextBt;
    @FXML private Button WHATgetWhatBt;
    @FXML private TextArea WHATdepTextBox;
    @FXML private TextArea WHATreqTextBox;

    private tableController parentController;
    private String selectedTarget;


    @FXML
    void whatIfGetWhatIfPr(ActionEvent event) {

    }

    public void clearSelectedTargetLabel() {
        WHATselectedTargetLabel.setText(" -");
        selectedTarget = null;
    }

    public void setSelectedTargetLabel(String targetName) {
        WHATselectedTargetLabel.setText(targetName);
        selectedTarget = targetName;
    }


    public void setParentController(tableController parent) { parentController = parent; }

    // what if tab
    public void whatIfSetup() {}
    public void setWhatBtDisable(boolean disable) { WHATgetWhatBt.setDisable(disable);}
    private void whatIfClearTextBoxes() {}
    @FXML void whatIfClearTextPr(ActionEvent event) {

    }
    /*
    @FXML void whatIfGetWhatIfPr(ActionEvent event) {
        Set<String> res = parentController.getWhatIf(selectedTarget, Bond.DEPENDS_ON);
        if(res.isEmpty())
            WHATdepTextBox.setText(" -- No Dependency --");
        else
            WHATdepTextBox.setText(res.toString());
        res = parentController.getWhatIf(selectedTarget, Bond.REQUIRED_FOR);
        if(res.isEmpty())
            WHATreqTextBox.setText(" -- No Requirements --");
        else
            WHATreqTextBox.setText(res.toString());

    }
*/



}

