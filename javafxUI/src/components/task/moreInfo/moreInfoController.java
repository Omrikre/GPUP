package components.task.moreInfo;

import Engine.DTO.TargetDTO;
import Engine.Enums.State;
import components.task.taskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class moreInfoController {


    private taskController parentController;

    @FXML private Label targetNameLB;
    @FXML private Label currStateLB;
    @FXML private Label targetTypeLB;
    @FXML private Label pTimeLB;
    @FXML private TextArea currStateTB;
    @FXML private TextArea javacTB;
    @FXML private Label depOnLB;
    @FXML private TextField depOnTB;
    @FXML private Label reqForLB;
    @FXML private TextField reqForTB;
    @FXML private Label setsLB;
    @FXML private TextField setsTB;
    @FXML private Button okBT;
    @FXML private TextField targetLogTB;

    @FXML public void initialize() {
        targetNameLB.setText("-"); //
        currStateLB.setText("-"); //
        targetTypeLB.setText("-");//
        pTimeLB.setText("-");//
        currStateTB.setText("");//
        javacTB.setText("");//
        depOnLB.setText("-");//
        depOnTB.setText("");//
        reqForLB.setText("-");//
        reqForTB.setText("");//
        setsLB.setText("-");//
        setsTB.setText("");//
        targetLogTB.setText("");
    }

    @FXML void okPr(ActionEvent event) { parentController.closeMoreInfoWindow(); }
    public void setParentController(taskController parent) {parentController = parent;}

    public void setupData(TargetDTO t) {
        String name = t.getTargetName();
        targetNameLB.setText(name);
        String StateString = getStateAffect(t, t.getTargetState());
        currStateLB.setText(StateString);
        targetTypeLB.setText(t.getTargetLocationString().toString());
        pTimeLB.setText(parentController.getMainController().getEngine().getProsTime(name));
        Integer tempSize = t.getSerialSetsBelongs();
        setsLB.setText(tempSize.toString());
        setsTB.setText(parentController.getMainController().getEngine().getSerialSetsByTargetName(name).toString());
        currStateTB.setText(t.getTargetState().toString());
        javacTB.setText(parentController.getMainController().getEngine().getJavacLog(name));
        tempSize = t.getTargetDependsOn().size();
        depOnLB.setText(tempSize.toString());
        depOnTB.setText(t.getTargetDependsOn().toString());
        tempSize = t.getTargetRequiredFor().size();
        reqForLB.setText(tempSize.toString());
        reqForTB.setText(t.getTargetRequiredFor().toString());
        targetLogTB.setText(parentController.getMainController().getEngine().getTargetLog(name));
    }

    private String getStateAffect(TargetDTO t, State targetState) {
        String resString = ""; //TODO - add waiting?
        switch (targetState) {
            case WAITING:
                resString = "Waiting for: " + t.getWhyWaiting();
                break;

            case SKIPPED:
                resString = "Skipped Because: " + t.getWhySkipped();
                break;

            case FINISHED_FAILURE:
                resString = "Failed Because: " + t.getWhyFailed();
                break;

            case FINISHED_SUCCESS:
                resString = "Success";
                break;

            case FINISHED_WARNINGS:
                resString = "Warnings";
                break;

            case FROZEN:
                resString = "Frozen Because: " + t.getWhyFrozen();
                break;
        }
        return resString;
    }
}
