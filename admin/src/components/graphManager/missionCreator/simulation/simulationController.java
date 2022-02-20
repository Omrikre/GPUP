package components.graphManager.missionCreator.simulation;


import Engine.Enums.Bond;
import Exceptions.FileException;
import components.graphManager.missionCreator.taskController;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;


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

        useWhatIfBT.setOnAction((event) -> {
            if (useWhatIfBT.isSelected()) {
                useWhatIfBT.setDisable(false);
            } else {
                useWhatIfBT.setDisable(true);
            }
        });
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
        //newRunBt.setDisable(true);


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
        //parentController.setDisableTaskType(true);
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
        firstRun = false;

    }

    @FXML void selectAllTargetsPr(ActionEvent event) {parentController.selectAllCB();}
    @FXML void unselectAllTargetsPr(ActionEvent event) {parentController.unselectAllCB();}
    @FXML void useWhatIfPr(ActionEvent event) {
        if(!useWhatIfBT.isSelected()){
            parentController.setAllCBDisable(false);
            depOnBT.setSelected(false);
            reqForBT.setSelected(false);
        }
        else {
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


