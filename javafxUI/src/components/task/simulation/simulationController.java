package components.task.simulation;


import Engine.Enums.Bond;
import Exceptions.FileException;
import components.task.simulation.incrementalError.incrementalErrorController;
import components.task.simulation.result.simulationResultController;
import components.task.taskController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static components.app.CommonResourcesPaths.TASK_INC_MSG_fXML_RESOURCE;
import static components.app.CommonResourcesPaths.TASK_SIM_RES_MSG_fXML_RESOURCE;


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
    @FXML private ToggleButton incrementalBT;//
    @FXML private Spinner<Integer> threadsNumSpinner;//
    @FXML private ProgressBar progressBar;//
    @FXML private Label progressPresentLabel;//
    @FXML private Button pauseBT;
    @FXML private Button runBT;
    @FXML private GridPane whatIfGP;

    private BorderPane incErrorComponent;
    private incrementalErrorController incErrorComponentController;
    private Stage incErrorWin;

    private BorderPane simulationResultComponent;
    private simulationResultController simulationResultComponentController;
    private Stage simulationResultWin;

    private taskController parentController;

    private ArrayList<String> runTargetsArray;
    private ArrayList<String> lastRunTargetsArray;
    private int SelectedNum;

    private boolean fromScratch;


    // initializers
    @FXML public void initialize() {
        runTargetsArray = new ArrayList<String>();
        lastRunTargetsArray = new ArrayList<String>();
        cleanup();
        loadBackComponents();
        fromScratch = true;
        pauseBT.setDisable(true);

        useWhatIfBT.setOnAction((event) -> {
            if (useWhatIfBT.isSelected()) {
                whatIfGP.setDisable(false);
            } else {
                whatIfGP.setDisable(true);
            }
        });
        incrementalBT.setOnAction((event) -> {
            if (incrementalBT.isSelected()) {
                if (!IfIncrementalPossible()) {
                    incrementalBT.setSelected(false);
                    popupErrorMessage();
                } else {
                    fromScratch = false;
                }
            }
        });
    }
    private void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // simulation - incremental not possible warning
            fxmlLoader.setLocation(getClass().getResource(TASK_INC_MSG_fXML_RESOURCE));
            incErrorComponent = fxmlLoader.load();
            incErrorComponentController = fxmlLoader.getController();
            incErrorComponentController.setParentController(this);
            incErrorWin = new Stage();
            incErrorWin.setTitle("Incremental Isn't Possible");
            incErrorWin.getIcons().add(new Image("/images/appIcon.png"));
            incErrorWin.setScene(new Scene(incErrorComponent));
            incErrorWin.initModality(Modality.APPLICATION_MODAL);
            incErrorWin.setResizable(false);


            // simulation - end result
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TASK_SIM_RES_MSG_fXML_RESOURCE));
            simulationResultComponent = fxmlLoader.load();
            simulationResultComponentController = fxmlLoader.getController();
            simulationResultComponentController.setParentController(this);
            simulationResultWin = new Stage();
            simulationResultWin.setTitle("Simulation Result");
            simulationResultWin.getIcons().add(new Image("/images/appIcon.png"));
            simulationResultWin.setScene(new Scene(simulationResultComponent));
            simulationResultWin.initModality(Modality.APPLICATION_MODAL);
            simulationResultWin.setResizable(false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("BIG Problem (inc. error)");
        }
    }

    public void closeError() {incErrorWin.close();}
    private void popupErrorMessage() {
        incErrorComponentController.setupData();
        incErrorWin.show();
    }
    private boolean IfIncrementalPossible() {
        return false;
        //return runTargetsArray.equals(lastRunTargetsArray); //TODO
    }

    public void setParentController(taskController parent) {parentController = parent;}
    private void cleanup() {
        // buttons
        randomCB.setSelected(false);
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(true);
        incrementalBT.setSelected(false);
        depOnBT.setSelected(false);
        reqForBT.setSelected(false);

        incrementalBT.setDisable(true);
        whatIfGP.setDisable(true);

        // text boxes
        selectedTargetsTB.setEditable(false);

        // progress bar
        progressBar.setDisable(true);
        progressBar.setProgress(0);
        progressPresentLabel.setText("0 %");

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
        timeSpinner.setEditable(true);

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 100, 1);
        successSpinner.setValueFactory(valueFactory);
        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0, 1);
        warningsSpinner.setValueFactory(valueFactory);

        valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, parentController.getMaxThreads(), 1, 1);
        threadsNumSpinner.setValueFactory(valueFactory);
        setupArray();
    }
    public void setSelectedTargetsTB() {selectedTargetsTB.setText(runTargetsArray.toString());}


    // buttons
    @FXML void pauseBTPr(ActionEvent event) {
        parentController.setPause();
        pauseBT.setDisable(true);
        runBT.setDisable(false);
        parentController.setDisableTaskType(false);

        upVB.setDisable(false);
        downVB.setDisable(false);
        runBT.setDisable(false);
    }
    @FXML void runBTPr(ActionEvent event) throws FileException, InterruptedException {
        pauseBT.setDisable(false);
        runBT.setDisable(true);
        parentController.setDisableTaskType(true);
        upVB.setDisable(true);
        downVB.setDisable(true);
        incrementalBT.setDisable(false);
        fromScratch = !incrementalBT.isSelected();
        lastRunTargetsArray = (ArrayList<String>) runTargetsArray.clone();
        parentController.runSimulation(
                timeSpinner.getValue(), randomCB.isSelected(), successSpinner.getValue(),
                warningsSpinner.getValue(), threadsNumSpinner.getValue(), runTargetsArray, fromScratch);
    }
    @FXML void incrementalPr(ActionEvent event) {}
    @FXML void selectAllTargetsPr(ActionEvent event) {
        parentController.selectAllCB();
    }
    @FXML void unselectAllTargetsPr(ActionEvent event) {
        parentController.unselectAllCB();
    }
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
        whatIfGP.setDisable(true);
        useWhatIfBT.setSelected(false);
        //useWhatIfBT.setDisable(true);
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }
    private void whatIfMakeEnable() {
        whatIfGP.setDisable(true);
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
        this.SelectedNum = numCheckBoxesSelected.intValue();
        if (SelectedNum != 1)
            whatIfMakeDisable();
        else
            whatIfMakeEnable();
    }
    public void closeResult() {
        simulationResultWin.close();
    }
    private void openResult() {
        simulationResultComponentController.setupData(parentController.getSimResData());
        simulationResultWin.show();
    }
    public ArrayList<String> getRunTargetsArray() {return runTargetsArray;}
    public ArrayList<String> getLastRunTargetsArray() {return lastRunTargetsArray;}
}


