package components.table.whatIf;

import components.table.tableController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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


    public void setParentController(tableController parent) { parentController = parent; }

    // what if tab
    public void whatIfSetup() {}
    public void setWhatBtDisable(boolean disable) { WHATgetWhatBt.setDisable(disable);}
    private void whatIfClearTextBoxes() {}
    @FXML void whatIfClearTextPr(ActionEvent event) {
        System.out.println("1");
    }
    @FXML void whatIfGetWhatIfPr(ActionEvent event) {
        System.out.println("1");
    }




}

