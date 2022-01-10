package components.task.compilation.result;

import Engine.Enums.State;
import components.task.compilation.compilationController;
import components.task.simulation.simulationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.Set;

public class compResultController {

    @FXML private Label totalTargetsLB;
    @FXML private Label successLB;
    @FXML private TextField successTB;
    @FXML private Label failLB;
    @FXML private TextField failTB;
    @FXML private Label skippedLB;
    @FXML private TextField skippedTB;
    @FXML private Button OKBt;

    private compilationController parentController;

    public void setParentController(compilationController parentController) {this.parentController = parentController;}

    @FXML void okPr(ActionEvent event) { parentController.closeResult(); }

    public void setupData(Map<State, Set<String>> simResData) {

        Integer total = 0;
        Set<String> tempSet = simResData.get(State.FINISHED_SUCCESS);
        Integer tempNum = tempSet.size();
        total += tempSet.size();
        successLB.setText(tempNum.toString());
        successTB.setText(tempSet.toString());

        tempSet = simResData.get(State.FINISHED_FAILURE);
        tempNum = tempSet.size();
        total += tempSet.size();
        failLB.setText(tempNum.toString());
        failTB.setText(tempSet.toString());

        tempSet = simResData.get(State.SKIPPED);
        tempNum = tempSet.size();
        total += tempSet.size();
        skippedLB.setText(tempNum.toString());
        skippedTB.setText(tempSet.toString());

        totalTargetsLB.setText(total.toString());
    }
}
