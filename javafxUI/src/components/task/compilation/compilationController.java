package components.task.compilation;

import components.task.simulation.incrementalError.incrementalErrorController;
import components.task.taskController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

import static components.app.CommonResourcesPaths.TASK_INC_MSG_fXML_RESOURCE;
import static components.app.CommonResourcesPaths.TASK_SIM_RES_MSG_fXML_RESOURCE;

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
    @FXML private Spinner<?> threadsNumSpinner;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressPresentLabel;
    @FXML private Button pauseBT;
    @FXML private Button runBT;

    private BorderPane incErrorComponent;
    private incrementalErrorController incErrorComponentController;
    private Stage incErrorWin;

    private taskController parentController;
    private ArrayList<String> runTargetsArray;
    private ArrayList<String> lastRunTargetsArray;
    private boolean fromScratch;


    public void setParentController(taskController parent) { parentController = parent; }

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
    public void setupData() {
        runTargetsArray.clear();
        selectedTargetsTB.setText(runTargetsArray.toString());
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
            // simulation - incremental not possible warning

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


            // simulation - end result
            /*
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
            */


        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("BIG Problem (inc. error)");
        }
    }

    private void cleanup() {
        // buttons
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

    public void closeError() {incErrorWin.close();}
    private void setupArray() {
        runTargetsArray.clear();
        selectedTargetsTB.setText(runTargetsArray.toString());
    }


    @FXML void depOnPr(ActionEvent event) {}

    @FXML void incrementalPr(ActionEvent event) {}

    @FXML void pauseBTPr(ActionEvent event) {}

    @FXML void reqForPr(ActionEvent event) {}

    @FXML void runBTPr(ActionEvent event) {}

    @FXML void selectAllTargetsPr(ActionEvent event) {}

    @FXML void unselectAllTargetsPr(ActionEvent event) {}

    @FXML void useWhatIfPr(ActionEvent event) {}


}





