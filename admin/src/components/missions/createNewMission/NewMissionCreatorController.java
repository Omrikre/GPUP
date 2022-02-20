package components.missions.createNewMission;

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
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class NewMissionCreatorController {

    @FXML private Label MsgLB;
    @FXML private Button createBT;
    @FXML private TextField missionNameTF;

    private boolean createNewMissionSucceed;
    private AppController mainController;
    private String httpUrl;


    @FXML public void initialize() {
        cleanup();
    }

    public void setMainController(AppController controller) { mainController = controller; }

    private void sendNewMissionRequest() {
        MsgLB.setText("");
        createBT.setDisable(true);
        sendRequest();
        if(createNewMissionSucceed)
            this.mainController.closeCreateNewMissionWin();


        //TODO - make the request
        changeBTToOK(true);
    }

    public void cleanup() {
        changeBTToOK(false);
        MsgLB.setText("");
    }

    private void changeBTToOK(boolean bool) {
        if (bool) {
            createBT.setText("OK");
            createBT.setDefaultButton(true);
            createBT.setDisable(false);
            createNewMissionSucceed = true;
        }
        else {
            createBT.setText("Create");
            createBT.setDefaultButton(false);
            createBT.setDisable(false);
            createNewMissionSucceed = false;
        }
        missionNameTF.requestFocus();
        missionNameTF.clear();
    }

    @FXML void createPR(ActionEvent event) {
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

    public void setupData(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public void setupData(boolean isDup, boolean isFromScratch, String oldMission, String newName) {
        //TODO - dup request
    }
}

