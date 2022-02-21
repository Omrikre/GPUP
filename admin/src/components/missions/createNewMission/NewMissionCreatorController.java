package components.missions.createNewMission;

import com.google.gson.Gson;
import components.app.AppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static components.app.HttpResourcesPaths.ADD_MISSION;

public class NewMissionCreatorController {

    @FXML private Label MsgLB;
    @FXML private Button createBT;
    @FXML private TextField missionNameTF;

    private boolean createNewMissionSucceed;
    private AppController mainController;

    private String targetsArr;
    private String amountOfTargets;
    private String graphName;
    private String missionName;
    private String isIncremental;
    private String isFromScratch;

    // for compilation
    private String src;
    private String compFolder;

    // for simulation
    private String randomRunTime;
    private String success;
    private String successWithWarnings;
    private String runTime;

    private String oldName;
    private String httpUrl;

    private boolean isItDuplicate;
    private boolean isSimulation;





    @FXML public void initialize() {
        cleanup();
    }

    public void setMainController(AppController controller) { mainController = controller; }

    private void sendNewMissionRequest() {
        if(createNewMissionSucceed)
            this.mainController.closeCreateNewMissionWin();

        MsgLB.setText("");
        createBT.setDisable(true);

        if(isItDuplicate)
            createDupHttpCall();
        else
            createHttpCall();

        sendRequest();
    }

    public void cleanup() {
        changeBTToOK(false);
        MsgLB.setText("");
        missionNameTF.clear();


        this.targetsArr = "";
        this.amountOfTargets = "";
        this.src = "";
        this.compFolder = "";
        this.graphName = "";
        this.isFromScratch = "";
        this.isIncremental = "";
        this.oldName = "";;
        isItDuplicate = false;
        this.httpUrl = "";
    }

    private void changeBTToOK(boolean bool) {
        if (bool) {
            missionNameTF.setDisable(true);
            createBT.setText("OK");
            createBT.setDefaultButton(true);
            createBT.setDisable(false);
            createNewMissionSucceed = true;
        }
        else {
            missionNameTF.setDisable(false);
            createBT.setText("Create");
            createBT.setDefaultButton(false);
            createBT.setDisable(false);
            createNewMissionSucceed = false;
        }
        missionNameTF.requestFocus();
        missionNameTF.clear();
    }

    @FXML void createPR(ActionEvent event) {
        missionName = missionNameTF.getText();
        sendNewMissionRequest();
    }

    private void sendRequest() {

        HttpClientUtil.runAsync(httpUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                        MsgLB.setText("Error: can't sent the request");
                createNewMissionSucceed = false;
                            changeBTToOK(false);
            }
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                            MsgLB.setText(responseBody);
                            createNewMissionSucceed = false;
                                changeBTToOK(false);
                }
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            MsgLB.setText("The mission was created successfully");
                            createNewMissionSucceed = true;
                            changeBTToOK(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void createHttpCall() {
        String finalUrl;
        if(!isSimulation) {
            finalUrl = HttpUrl
                    .parse(ADD_MISSION)
                    .newBuilder()
                    .addQueryParameter("targets-array", targetsArr)
                    .addQueryParameter("amount-of-targets", amountOfTargets)
                    .addQueryParameter("src", src)
                    .addQueryParameter("compilation-folder", compFolder) //for compilation task, else null
                    //the rest are for the display:
                    .addQueryParameter("name", missionName)
                    .addQueryParameter("graph-name", graphName)
                    .addQueryParameter("from-scratch", "false")
                    .addQueryParameter("incremental", "false")
                    .build()
                    .toString();
        }
        else {
            finalUrl = HttpUrl
                    .parse(ADD_MISSION)
                    .newBuilder()
                    .addQueryParameter("targets-array", targetsArr)
                    .addQueryParameter("amount-of-targets", amountOfTargets)
                    .addQueryParameter("runtime", runTime)
                    .addQueryParameter("random-runtime", randomRunTime)
                    .addQueryParameter("success", success)
                    .addQueryParameter("success-warnings", successWithWarnings)
                    //the rest are for the display:
                    .addQueryParameter("name", missionName)
                    .addQueryParameter("graph-name", graphName)
                    .addQueryParameter("from-scratch", "false")
                    .addQueryParameter("incremental", "false")
                    .build()
                    .toString();

        }

        this.httpUrl = finalUrl;
    }

    private void createDupHttpCall() {
        String finalUrl = HttpUrl
                .parse(ADD_MISSION)
                .newBuilder()
                .addQueryParameter("name", oldName)
                .addQueryParameter("from-scratch",isFromScratch)
                .addQueryParameter("incremental",isIncremental)
                .addQueryParameter("new-name", missionName)
                .build()
                .toString();
        this.httpUrl = finalUrl;
    }

    public void setupDataCompilation(boolean isFromScratch, String oldName) {
        if (isFromScratch) {
            this.isFromScratch = "true";
            this.isIncremental = "false";
        }
        else {
            this.isFromScratch = "false";
            this.isIncremental = "true";
        }
        this.oldName = oldName;
        isItDuplicate = true;
    }

    public void setupDataCompilation(String targetsArr, String amountOfTargets, String src, String compFolder, String graphName) {
        this.targetsArr = targetsArr;
        this.amountOfTargets = amountOfTargets;
        this.src = src;
        this.compFolder = compFolder;
        this.graphName = graphName;

        isItDuplicate = false;
        isSimulation = false;
    }

    public void setupDataSimulation(String targetsArr, String amountOfTargets,
                                    String runTime, String randomRunTime, String success,
                                    String successWithWarnings, String graphName) {
        this.targetsArr = targetsArr;
        this.amountOfTargets = amountOfTargets;

        this.runTime = runTime;
        this.randomRunTime = randomRunTime;
        this.success = success;
        this.successWithWarnings = successWithWarnings;

        this.graphName = graphName;

        isItDuplicate = false;
        isSimulation = true;
    }

}

