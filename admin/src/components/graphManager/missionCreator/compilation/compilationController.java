package components.graphManager.missionCreator.compilation;

import Engine.Engine;
import Engine.Enums.Bond;
import com.google.gson.Gson;
import components.graphManager.missionCreator.taskController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static components.app.HttpResourcesPaths.ADD_MISSION;

public class compilationController {


    @FXML
    private BorderPane simulationBP;
    @FXML
    private VBox upVB;
    @FXML
    private ToggleButton useWhatIfBT;
    @FXML
    private ToggleButton depOnBT;
    @FXML
    private ToggleButton reqForBT;
    @FXML
    private Button selectAllTargetsBT;
    @FXML
    private Button unselectAllTargetsBT;
    @FXML
    private TextField selectedTargetsTB;
    @FXML
    private VBox downVB;
    @FXML
    private Button runBT;
    @FXML
    private Button inputPathBT;
    @FXML
    private Button outputPathBt;
    @FXML
    private CheckBox outputPathCB;
    @FXML
    private CheckBox inputPathCB;


    private taskController parentController;
    private ArrayList<String> runTargetsArray;
    private ArrayList<String> lastRunTargetsArray;
    private boolean fromScratch;

    private String inputPath;
    private String outputPath;
    private int SelectedNum;
    private String name;

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


        useWhatIfBT.setOnAction((event) -> {
            if (useWhatIfBT.isSelected()) {
                //whatIfGP.setDisable(false);
            } else {
                //whatIfGP.setDisable(true);
            }
        });
    }

    public void setupData() {
        runTargetsArray.clear();
        selectedTargetsTB.setText(runTargetsArray.toString());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, parentController.getMaxThreads(), 1, 1);
    }


    private boolean IfIncrementalPossible() {
        return false;
        //return runTargetsArray.equals(lastRunTargetsArray); //TODO
    }


    private void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {

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
        depOnBT.setSelected(false);
        reqForBT.setSelected(false);


        inputPath = null;
        outputPath = null;
        outputPathCB.setSelected(false);
        inputPathCB.setSelected(false);

        // text boxes
        selectedTargetsTB.setEditable(false);


        setupArray();
    }


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

    public void setSelectedTargetsTB() {
        selectedTargetsTB.setText(runTargetsArray.toString());
    }

    @FXML
    void runBTPr(ActionEvent event) {


        runBT.setDisable(true);
        //parentController.setDisableTaskType(true);
        upVB.setDisable(true);
        downVB.setDisable(true);
        lastRunTargetsArray = (ArrayList<String>) runTargetsArray.clone();

        //TODO - delete
        System.out.println("\n\n-- Compilation Data --");
        System.out.println(runTargetsArray);
        System.out.println(fromScratch);
        System.out.println("\n");


        runningCompilation = true;
        firstRun = false;


        String graphName = parentController.getGraphName();
        Gson gson = new Gson();
        String targetsArr = gson.toJson(runTargetsArray);
        String amountOfTargets= String.valueOf(runTargetsArray.size());
        String compilationFolder=outputPath;
        String src=inputPath;
        String missionName=name;
        String finalUrl = HttpUrl
                .parse(ADD_MISSION)
                .newBuilder()
                .addQueryParameter("targets-array", targetsArr)
                .addQueryParameter("amount-of-targets", amountOfTargets)
                .addQueryParameter("src",src)
                .addQueryParameter("compilation-folder", compilationFolder) //for compilation task, else null
                //the rest are for the display:
                .addQueryParameter("name", missionName)
                .addQueryParameter("graph-name", graphName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> System.out.println()
                        //.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> System.out.println()
                           // .setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {

                        try {
                            String responseBody = response.body().string();
                            System.out.println(responseBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    @FXML
    void selectAllTargetsPr(ActionEvent event) {
        parentController.selectAllCB();
    }

    @FXML
    void unselectAllTargetsPr(ActionEvent event) {
        parentController.unselectAllCB();
    }

    @FXML
    void useWhatIfPr(ActionEvent event) {
        if (!useWhatIfBT.isSelected()) {
            parentController.setAllCBDisable(false);
            depOnBT.setSelected(false);
            reqForBT.setSelected(false);
        } else {
            parentController.setAllCBDisable(true);
        }
    }

    @FXML
    void inputPathPr(ActionEvent event) {
        inputPath = pathGetter();
        if (inputPath != null)
            inputPathCB.setSelected(true);
        checkIfToOpenRunBT();
    }

    @FXML
    void outputPathPr(ActionEvent event) {
        outputPath = pathGetter();
        if (outputPath != null)
            outputPathCB.setSelected(true);
        checkIfToOpenRunBT();
    }

    @FXML
    void reqForPr(ActionEvent event) {
        String name = runTargetsArray.get(0);
        System.out.println(parentController.getWhatIf(name, Bond.REQUIRED_FOR));
        parentController.setWhatIfSelections(parentController.getWhatIf(name, Bond.REQUIRED_FOR));
        whatIfMakeDisable();
    }

    @FXML
    void depOnPr(ActionEvent event) {
        String name = runTargetsArray.get(0);
        parentController.setWhatIfSelections(parentController.getWhatIf(name, Bond.DEPENDS_ON));
        whatIfMakeDisable();
    }

    private void whatIfMakeDisable() {
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

    private void checkIfToOpenRunBT() {
        runBT.setDisable(inputPath == null || outputPath == null);
    }

    public void setSelectedNum(IntegerBinding numCheckBoxesSelected) {
        this.SelectedNum = numCheckBoxesSelected.intValue();
        if (SelectedNum != 1)
            whatIfMakeDisable();
        else
            whatIfMakeEnable();
    }

    private void whatIfMakeEnable() {
        useWhatIfBT.setSelected(false);
        useWhatIfBT.setDisable(false);
        reqForBT.setSelected(false);
        depOnBT.setSelected(false);
    }

    public void setProgress(int progress) {
        Integer temp = progress;
        double dProgress = progress;
        double pres = dProgress / 100;
    }

    public void openResult() {
        cleanupAfterFinish();
    }

    private void cleanupAfterFinish() {
        //parentController.setDisableTaskType(false);
        upVB.setDisable(false);
        downVB.setDisable(false);
        runBT.setDisable(false);
        cleanup();
        parentController.unselectAllCB();
        runBT.setDisable(true);
    }

    public ArrayList<String> getRunTargetsArray() {
        return runTargetsArray;
    }

    public ArrayList<String> getLastRunTargetsArray() {
        return lastRunTargetsArray;
    }


}




