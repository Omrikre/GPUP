package components.table.cycle;

import components.table.tableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class cycleController {

    @FXML private VBox cycleVBPane;
    // find cycle tab
    @FXML private Label CYCLEselectedTargetLabel;
    @FXML private Button CYCLEclearTextBt;
    @FXML private Button CYCLEgetCycleBt;
    @FXML private Label CYCLEifInCycleLabel;
    @FXML private TextArea CYCLETextBox;

    private tableController parentController;
    private String selectedTarget;

    public void clearSelectedTargetLabel() {
        CYCLEselectedTargetLabel.setText(" -");
        selectedTarget = null;
    }

    public void setSelectedTargetLabel(String targetName) {
        CYCLEselectedTargetLabel.setText(targetName);
        selectedTarget = targetName;
    }


    public void setParentController(tableController parent) { parentController = parent; }

    // cycle tab
    public void cycleSetup() {}
    private void CycleClearTextBoxes() {
        System.out.println("1");
    }
    @FXML void cycleClearTextPr(ActionEvent event) {
        CYCLETextBox.clear();
    }
    @FXML void cycleGetCyclePr(ActionEvent event) {
        CYCLETextBox.setDisable(false);
        if(parentController.getIfInCycle(selectedTarget).size() == 0)
            CYCLETextBox.setText("-- The target '" + selectedTarget + "' isn't part of a cycle --");
        else
            CYCLETextBox.setText(parentController.getIfInCycle(selectedTarget).toString());
    }


    public void setCycleDisable(boolean disable) { CYCLEgetCycleBt.setDisable(disable); }






}
