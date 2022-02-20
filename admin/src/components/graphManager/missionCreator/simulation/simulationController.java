package components.graphManager.missionCreator.simulation;


import Engine.Enums.Bond;
import Exceptions.FileException;
import com.google.gson.Gson;
import components.graphManager.missionCreator.taskController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static components.app.HttpResourcesPaths.ADD_MISSION;


public class simulationController {


    @FXML private VBox upVB;
    @FXML private VBox downVB;

    @FXML private BorderPane simulationBP;
    @FXML private Spinner<Integer> timeSpinner;//
    @FXML private Spinner<Integer> successSpinner;//
    @FXML private CheckBox randomCB;//
    @FXML private Spinner<Integer> warningsSpinner;//
    @FXML private ToggleButton useWhatIfBT;//
    @FXML private ToggleButton depOnBT;//
    @FXML private ToggleButton reqForBT;//
    @FXML private Button selectAllTargetsBT;//
    @FXML private Button unselectAllTargetsBT;
    @FXML private TextField selectedTargetsTB;//
    @FXML private Button runBT;


    private taskController parentController;

    private ArrayList<String> runTargetsArray;
    private ArrayList<String> lastRunTargetsArray;

    private boolean fromScratch;
    private boolean runningSimulation;
    private boolean firstRun;
    private boolean runIsDone;
    private String name;


    // initializers
    @FXML public void initialize() {
        runTargetsArray = new ArrayList<String>();
        lastRunTargetsArray = new ArrayList<String>();
        cleanup();
        loadBackComponents();
        fromScratch = true;
        runningSimulation = false;
        firstRun = true;
        runIsDone = false;
        runBT.setDisable(true);
        depOnBT.setDisable(true);
        reqForBT.setDisable(true);

        useWhatIfBT.setOnAction((event) -> {
            if (useWhatIfBT.isSelected()) {
                useWhatIfBT.setDisable(false);
                depOnBT.setDisable(false);
                reqForBT.setDisable(false);
            } else {
                useWhatIfBT.setDisable(true);
                depOnBT.setDisable(true);
                reqForBT.setDisable(true);
            }
        });

        setupData();
    }
    private void loadBackComponents() {;
        try {

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("BIG Problem (inc. error)");
        }
    }


    private boolean IfIncrementalPossible() {

        Collections.sort(runTargetsArray);
        Collections.sort(lastRunTargetsArray);
        return runTargetsArray.equals(lastRunTargetsArray);
        /*
        for (String name : runTargetsArray) {
            if(!lastRunTargetsArray.contains(name))
                return false;
        }
        return true;

         */
    }

    public void setParentController(taskController parent) {parentController = parent;}
    public void cleanup() {
        // buttons
        randomCB.setSelected(false);
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(true);
        depOnBT.setSelected(false);
        reqForBT.setSelected(false);


        // text boxes
        selectedTargetsTB.setEditable(false);

        setupArray();
    }
    private void setupArray() {
        runTargetsArray.clear();
        //TODO - for all check boxes add the targets name to the array

        selectedTargetsTB.setText(runTargetsArray.toString());
    }
    public void setupData() {
        // spinners
        SpinnerValueFactory<Integer> valueFactory;

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999999, 1000, 1);
        timeSpinner.setValueFactory(valueFactory);
        fixSpinnerBug(timeSpinner);

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 100, 1);
        successSpinner.setValueFactory(valueFactory);
        fixSpinnerBug(successSpinner);

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        warningsSpinner.setValueFactory(valueFactory);
        fixSpinnerBug(warningsSpinner);

        setupArray();
    }
    public void setSelectedTargetsTB() {selectedTargetsTB.setText(runTargetsArray.toString());}
    private void fixSpinnerBug(Spinner<Integer> spinner) {
        spinner.setEditable(true);
        TextFormatter format = new TextFormatter(spinner.getValueFactory().getConverter(), spinner.getValueFactory().getValue());
        spinner.getEditor().setTextFormatter(format);
        spinner.getValueFactory().valueProperty().bindBidirectional(format.valueProperty());
    }

    // buttons
    @FXML void runBTPr(ActionEvent event) throws FileException, InterruptedException {
        runIsDone = false;
        parentController.resetTargetsStatus();

        parentController.getGraphName(); //TODO - graph name

        runBT.setDisable(true);
        upVB.setDisable(true);
        downVB.setDisable(true);
        lastRunTargetsArray = (ArrayList<String>) runTargetsArray.clone();

        //TODO - delete
        System.out.println("\n\n-- Simulation Data --");
        System.out.println(timeSpinner.getValue());
        System.out.println(randomCB.isSelected());
        System.out.println(successSpinner.getValue());
        System.out.println(warningsSpinner.getValue());
        System.out.println(runTargetsArray);
        System.out.println(fromScratch);
        System.out.println("\n");

        runningSimulation = true;

        String graphName = parentController.getGraphName();
        Gson gson = new Gson();
        String targetsArr = gson.toJson(runTargetsArray);
        String amountOfTargets= String.valueOf(runTargetsArray.size());
        String runtime= String.valueOf(timeSpinner.getValue());
        String randomRunTime= String.valueOf(randomCB.isSelected());
        String success= String.valueOf(successSpinner.getValue());
        String successWithWarnings= String.valueOf(warningsSpinner.getValue());

        System.out.println("\n\n-- Simulation Data --");
        System.out.println(targetsArr);
        System.out.println(amountOfTargets);
        System.out.println(runtime);
        System.out.println(randomRunTime);
        System.out.println(success);
        System.out.println(successWithWarnings);
        System.out.println(graphName);
        System.out.println("\n");

        parentController.CreateNewMissionWin(true ,targetsArr, amountOfTargets,
                "", "", graphName, runtime, randomRunTime, success, successWithWarnings);
    }

    @FXML void selectAllTargetsPr(ActionEvent event) {parentController.selectAllCB();}
    @FXML void unselectAllTargetsPr(ActionEvent event) {parentController.unselectAllCB();}
    @FXML void useWhatIfPr(ActionEvent event) {
        if(!useWhatIfBT.isSelected()){
            parentController.setAllCBDisable(false);
            depOnBT.setSelected(false);
            reqForBT.setSelected(false);
            depOnBT.setSelected(false);
            reqForBT.setSelected(false);
        }
        else {
            depOnBT.setSelected(true);
            reqForBT.setSelected(true);
            parentController.setAllCBDisable(true);
        }
    }
    @FXML void reqForPr(ActionEvent event) {
        String name = runTargetsArray.get(0);
        System.out.println(parentController.getWhatIf(name, Bond.REQUIRED_FOR));
        parentController.setWhatIfSelections(parentController.getWhatIf(name, Bond.REQUIRED_FOR));
        whatIfMakeDisable();
    }
    @FXML void depOnPr(ActionEvent event) {
        String name = runTargetsArray.get(0);
        parentController.setWhatIfSelections(parentController.getWhatIf(name, Bond.DEPENDS_ON));
        whatIfMakeDisable();
    }


    // methods
    private void whatIfMakeDisable() {
        useWhatIfBT.setSelected(false);
        if(parentController.getSelectedNum() != 1) {
            useWhatIfBT.setDisable(true);
        }
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }
    private void whatIfMakeEnable() {
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(false);
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }
    public void addSelectedTargetsTB(String targetName) {
        runTargetsArray.add(targetName);
        setSelectedTargetsTB();
    }
    public void removeSelectedTargetsTB(String targetName) {
        runTargetsArray.remove(targetName);
        setSelectedTargetsTB();
    }
    public void setSelectedNum(IntegerBinding numCheckBoxesSelected) {
        if (numCheckBoxesSelected.intValue() != 1)
            whatIfMakeDisable();
        else
            whatIfMakeEnable();
        if(numCheckBoxesSelected.intValue() == 0)
            runBT.setDisable(true);
        else
            runBT.setDisable(false);
    }



    private void cleanupAfterFinish() {

        //parentController.setDisableTaskType(false);

        upVB.setDisable(false);
        downVB.setDisable(false);
        runBT.setDisable(true);
        cleanup();
        parentController.unselectAllCB();
    }

    public ArrayList<String> getRunTargetsArray() {return runTargetsArray;}
    public ArrayList<String> getLastRunTargetsArray() {return lastRunTargetsArray;}

}




