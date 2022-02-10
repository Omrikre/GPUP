package components.graphManager.task.compilation;

import Engine.Enums.Bond;
import components.graphManager.task.compilation.result.compResultController;
import components.graphManager.task.simulation.incrementalError.incrementalErrorController;
import components.graphManager.task.taskController;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import static components.app.CommonResourcesPaths.TASK_COMP_RES_MSG_fXML_RESOURCE;
import static components.app.CommonResourcesPaths.TASK_INC_MSG_fXML_RESOURCE;

public class compilationController {

    @FXML private BorderPane simulationBP;
    @FXML private VBox upVB;
    @FXML private ToggleButton useWhatIfBT;
    @FXML private GridPane whatIfGP;
    @FXML private ToggleButton depOnBT;
    @FXML private ToggleButton reqForBT;
    @FXML private Button selectAllTargetsBT;
    @FXML private Button unselectAllTargetsBT;
    @FXML private TextField selectedTargetsTB;
    @FXML private VBox downVB;
    @FXML private ToggleButton incrementalBT;
    @FXML private Spinner<Integer> threadsNumSpinner;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressPresentLabel;
    @FXML private Button pauseBT;
    @FXML private Button runBT;
    @FXML private Button inputPathBT;
    @FXML private Button outputPathBt;
    @FXML private CheckBox outputPathCB;
    @FXML private CheckBox inputPathCB;

    private BorderPane incErrorComponent;
    private incrementalErrorController incErrorComponentController;
    private Stage incErrorWin;

    private BorderPane compilationResultComponent;
    private compResultController compilationResultComponentController;
    private Stage compilationResultWin;

    private taskController parentController;
    private ArrayList<String> runTargetsArray;
    private ArrayList<String> lastRunTargetsArray;
    private boolean fromScratch;

    private String inputPath;
    private String outputPath;
    private int SelectedNum;

    private boolean runningCompilation;
    private boolean firstRun;


    public void setParentController(taskController parent) {
        parentController = parent;
    }

    @FXML
    public void initialize() {
        runTargetsArray = new ArrayList<String>();
        fromScratch = false;
        lastRunTargetsArray = new ArrayList<String>();
        cleanup();
        loadBackComponents();
        outputPathCB.setDisable(true);
        inputPathCB.setDisable(true);
        runningCompilation = false;
        firstRun = true;
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

    public void setupData() {
        runTargetsArray.clear();
        selectedTargetsTB.setText(runTargetsArray.toString());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, parentController.getMaxThreads(), 1, 1);
        threadsNumSpinner.setValueFactory(valueFactory);
    }


    private boolean IfIncrementalPossible() {
        return false;
        //return runTargetsArray.equals(lastRunTargetsArray); //TODO
    }

    private void popupErrorMessage() {
        incErrorComponentController.setupData();
        incErrorWin.show();
    }

    private void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // compilation - incremental not possible warning

            fxmlLoader.setLocation(getClass().getResource(TASK_INC_MSG_fXML_RESOURCE));
            incErrorComponent = fxmlLoader.load();
            incErrorComponentController = fxmlLoader.getController();
            incErrorComponentController.setParentCompController(this);
            incErrorWin = new Stage();
            incErrorWin.setTitle("Incremental Isn't Possible");
            incErrorWin.getIcons().add(new Image("/images/appIcon.png"));
            incErrorWin.setScene(new Scene(incErrorComponent));
            incErrorWin.initModality(Modality.APPLICATION_MODAL);
            incErrorWin.setResizable(false);


            // compilation - end result
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TASK_COMP_RES_MSG_fXML_RESOURCE));
            compilationResultComponent = fxmlLoader.load();
            compilationResultComponentController = fxmlLoader.getController();
            compilationResultComponentController.setParentController(this);
            compilationResultWin = new Stage();
            compilationResultWin.setTitle("Compilation Result");
            compilationResultWin.getIcons().add(new Image("/images/appIcon.png"));
            compilationResultWin.setScene(new Scene(compilationResultComponent));
            compilationResultWin.initModality(Modality.APPLICATION_MODAL);
            compilationResultWin.setResizable(false);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("BIG Problem (inc. error)");
        }
    }

    public void cleanup() {
        // buttons
        useWhatIfBT.setDisable(true);
        runBT.setDisable(true);
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(true);
        incrementalBT.setSelected(false);
        depOnBT.setSelected(false);
        reqForBT.setSelected(false);
        incrementalBT.setDisable(true);
        whatIfGP.setDisable(true);

        inputPath = null;
        outputPath = null;
        outputPathCB.setSelected(false);
        inputPathCB.setSelected(false);

        // text boxes
        selectedTargetsTB.setEditable(false);

        // progress bar
        progressBar.setDisable(true);
        progressBar.setProgress(0);
        progressPresentLabel.setText("0 %");

        setupArray();
    }

    public void closeResult() {
        compilationResultWin.close();
    }

    public void closeError() {incErrorWin.close();}
    private void setupArray() {
        runTargetsArray.clear();
        selectedTargetsTB.setText(runTargetsArray.toString());
    }
    public void removeSelectedTargetsTB(String targetName) {
        runTargetsArray.remove(targetName);
        setSelectedTargetsTB();
    }
    public void addSelectedTargetsTB(String targetName) {
        runTargetsArray.add(targetName);
        setSelectedTargetsTB();
    }
    public void setSelectedTargetsTB() {  selectedTargetsTB.setText(runTargetsArray.toString());}

    @FXML void incrementalPr(ActionEvent event) {fromScratch = !incrementalBT.isSelected();}
    @FXML void pauseBTPr(ActionEvent event) {
        if(runningCompilation) {
            parentController.setPause();
            pauseBT.setDisable(false);
            pauseBT.setText("Resume");
            runBT.setDisable(true);
            //newRunBt.setDisable(false);
            downVB.setDisable(false);
            incrementalBT.setDisable(true);
            runningCompilation = false;
        }
        else {
            parentController.setResume(threadsNumSpinner.getValue());
            //newRunBt.setDisable(true);
            pauseBT.setDisable(false);
            pauseBT.setText("Pause");
            runBT.setDisable(true);
            incrementalBT.setDisable(false);
            downVB.setDisable(true);
            runningCompilation = true;
        }
    }
    @FXML void runBTPr(ActionEvent event) {
        if (incrementalBT.isSelected()) {
            if (!IfIncrementalPossible()) {
                incrementalBT.setSelected(false);
                popupErrorMessage();
            } else {
                fromScratch = false;
            }
        }
        pauseBT.setDisable(false);
        runBT.setDisable(true);
        parentController.setDisableTaskType(true);
        upVB.setDisable(true);
        downVB.setDisable(true);
        incrementalBT.setDisable(false);
        fromScratch = !incrementalBT.isSelected();
        lastRunTargetsArray = (ArrayList<String>) runTargetsArray.clone();

        //TODO - delete
        System.out.println("\n\n-- Compilation Data --");
        System.out.println(threadsNumSpinner.getValue());
        System.out.println(runTargetsArray);
        System.out.println(fromScratch);
        System.out.println("\n");


        runningCompilation = true;
        firstRun = false;

        parentController.runCompilation(
                threadsNumSpinner.getValue(), runTargetsArray, fromScratch,
                inputPath, outputPath);
    }
    @FXML void selectAllTargetsPr(ActionEvent event) {parentController.selectAllCB();}
    @FXML void unselectAllTargetsPr(ActionEvent event) {parentController.unselectAllCB();}
    @FXML void useWhatIfPr(ActionEvent event) {
        if (!useWhatIfBT.isSelected()) {
            parentController.setAllCBDisable(false);
            depOnBT.setSelected(false);
            reqForBT.setSelected(false);
        } else {
            parentController.setAllCBDisable(true);
        }
    }
    @FXML void inputPathPr(ActionEvent event) {
        inputPath = pathGetter();
        if(inputPath != null)
            inputPathCB.setSelected(true);
        checkIfToOpenRunBT();
    }
    @FXML void outputPathPr(ActionEvent event) {
        outputPath = pathGetter();
        if(outputPath != null)
            outputPathCB.setSelected(true);
        checkIfToOpenRunBT();
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

    private void whatIfMakeDisable() {
        whatIfGP.setDisable(true);
        useWhatIfBT.setSelected(false);
        if (parentController.getSelectedNum() != 1) {
            useWhatIfBT.setDisable(true);
        }
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }
    private String pathGetter() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null)
            return selectedDirectory.getAbsolutePath();
        else
            return null;

    }
    private void checkIfToOpenRunBT() {runBT.setDisable(inputPath == null || outputPath == null);}
    public void setSelectedNum(IntegerBinding numCheckBoxesSelected) {
        this.SelectedNum = numCheckBoxesSelected.intValue();
        if (SelectedNum != 1)
            whatIfMakeDisable();
        else
            whatIfMakeEnable();
    }
    private void whatIfMakeEnable() {
        whatIfGP.setDisable(true);
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(false);
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }
    public void setProgress(int progress) {
        Integer temp = progress;
        progressPresentLabel.setText(temp.toString() + " %");
        double dProgress = progress;
        double pres = dProgress/100;
        progressBar.setProgress(pres);
    }
    public void openResult() {
        compilationResultComponentController.setupData(parentController.getSimResData());
        compilationResultWin.show();
        cleanupAfterFinish();
    }
    private void cleanupAfterFinish() {
        pauseBT.setDisable(true);
        parentController.setDisableTaskType(false);
        upVB.setDisable(false);
        downVB.setDisable(false);
        runBT.setDisable(false);
        cleanup();
        parentController.unselectAllCB();
        runBT.setDisable(true);
    }
    public ArrayList<String> getRunTargetsArray() {return runTargetsArray;}
    public ArrayList<String> getLastRunTargetsArray() {return lastRunTargetsArray;}



}




