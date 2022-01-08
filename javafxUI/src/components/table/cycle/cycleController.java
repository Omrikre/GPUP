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


    public void setParentController(tableController parent) { parentController = parent; }

    // cycle tab
    public void cycleSetup() {}
    private void CycleClearTextBoxes() {
        System.out.println("1");
    }
    @FXML void cycleClearTextPr(ActionEvent event) {
        System.out.println("1");
    }
    @FXML void cycleGetCyclePr(ActionEvent event) {
        System.out.println("1");
    }


    public void setCycleDisable(boolean disable) { CYCLEgetCycleBt.setDisable(disable); }






}
