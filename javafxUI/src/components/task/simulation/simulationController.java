package components.task.simulation;


import components.task.simulation.incrementalError.incrementalErrorController;
import components.task.taskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

import static components.app.CommonResourcesPaths.TASK_INC_MSG_fXML_RESOURCE;


public class simulationController {

    @FXML private BorderPane simulationBP;
    @FXML private Spinner<Integer> timeSpinner;//
    @FXML private Spinner<Integer> successSpinner;//
    @FXML private CheckBox randomCB;//
    @FXML private Spinner<Integer> warningsSpinner;//
    @FXML private ToggleButton useWhatIfBT;//
    @FXML private ToggleButton depOnBT;//
    @FXML private ToggleButton reqForBT;//
    @FXML private Button selectAllTargetsBT;//
    @FXML private TextField selectedTargetsTB;//
    @FXML private ToggleButton incrementalBT;//
    @FXML private Spinner<Integer> threadsNumSpinner;//
    @FXML private ProgressBar progressBar;//
    @FXML private Label progressPresentLabel;//
    @FXML private Button pauseBT;
    @FXML private Button runBT;
    @FXML private GridPane whatIfGP;

    private taskController parentController;
    private ArrayList<String> runTargetsArray;

    public ArrayList<String> getRunTargetsArray() { return runTargetsArray; }
    public ArrayList<String> getLastRunTargetsArray() { return lastRunTargetsArray; }

    private ArrayList<String> lastRunTargetsArray;
    private boolean fromScratch;

    private BorderPane incErrorComponent;
    private incrementalErrorController incErrorComponentController;
    private Stage incErrorWin;



    // initializers
    @FXML public void initialize() {
        runTargetsArray = new ArrayList<String>();
        cleanup();
        loadBackComponents();
        fromScratch = true;

        useWhatIfBT.setOnAction((event) -> {
            if(useWhatIfBT.isSelected()) { whatIfGP.setDisable(false); }
            else { whatIfGP.setDisable(true); }
        });
        incrementalBT.setOnAction((event) -> {
            if(incrementalBT.isSelected()) {
                if(!IfIncrementalPossible()) {
                    incrementalBT.setSelected(false);
                    popupErrorMessage();
                } else {
                    fromScratch = false;
                }
            }});
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("BIG Problem (inc. error)");
        }
    }

    public void closeError() { incErrorWin.close(); }
    private void popupErrorMessage() {
        incErrorComponentController.setupData();
        incErrorWin.show(); }

    private boolean IfIncrementalPossible() {
        return false;
        //return runTargetsArray.equals(lastRunTargetsArray);
    }

    public void setParentController(taskController parent) { parentController = parent; }
    private void cleanup() {
        // buttons
        randomCB.setSelected(false);
        useWhatIfBT.setSelected(false);
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

        //TODO - check boxes mark all
    }
    private void setupArray() {
        runTargetsArray.clear();
        //TODO - for all check boxes add the targets name to the array

        selectedTargetsTB.setText(runTargetsArray.toString());
    }
    public void setupData() {
        cleanup();

        // spinners
        SpinnerValueFactory<Integer> valueFactory;
        valueFactory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999999, 100, 1);
        timeSpinner.setValueFactory(valueFactory);
        timeSpinner.setEditable(true);

        valueFactory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 100, 1);
        successSpinner.setValueFactory(valueFactory);
        valueFactory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 100, 1);
        warningsSpinner.setValueFactory(valueFactory);

        valueFactory =  new SpinnerValueFactory.IntegerSpinnerValueFactory(1, parentController.getMaxThreads(), 1, 1);
        threadsNumSpinner.setValueFactory(valueFactory);
        setupArray();
    }

    private void whatIfSettings() {

    }


    // buttons
    @FXML void pauseBTPr(ActionEvent event) {
        runBT.setDisable(false);
    }
    @FXML void runBTPr(ActionEvent event) {
        incrementalBT.setDisable(false);
        runBT.setDisable(true);
        lastRunTargetsArray = (ArrayList<String>)runTargetsArray.clone();
        parentController.runSimulation(
                timeSpinner.getValue(),
                randomCB.isSelected(),
                successSpinner.getValue(),
                warningsSpinner.getValue(),
                threadsNumSpinner.getValue());
    }




}

