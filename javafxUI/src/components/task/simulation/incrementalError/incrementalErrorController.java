package components.task.simulation.incrementalError;

import components.app.AppController;
import components.task.compilation.compilationController;
import components.task.simulation.simulationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;

public class incrementalErrorController {

    private simulationController parentController;
    private compilationController parentCompController;


    @FXML private Button OKBt;
    @FXML private TextField lastRunTargetsTB;
    @FXML private TextField selectedTargetsTB;


    @FXML public void initialize() {
        lastRunTargetsTB.setEditable(false);
        selectedTargetsTB.setEditable(false);
    }


    public void setParentController(simulationController parent) {
        parentController = parent;
    }
    public void setParentCompController(compilationController parent) {
        parentCompController = parent;
    }


    @FXML void OkPr(ActionEvent event) {
        if(parentController != null)
            parentController.closeError();
        else
            parentCompController.closeError();
    }

    public void setupData() {
        ArrayList<String> currRunArr;
        ArrayList<String> lastRunArr;
        if(parentController != null) {
            currRunArr = parentController.getRunTargetsArray();
            lastRunArr = parentController.getLastRunTargetsArray();
        }
        else {
            currRunArr = parentCompController.getRunTargetsArray();
            lastRunArr = parentCompController.getLastRunTargetsArray();
        }
        Collections.sort(currRunArr);
        Collections.sort(lastRunArr);

        selectedTargetsTB.setText(currRunArr.toString());
        lastRunTargetsTB.setText(lastRunArr.toString());
    }
}


