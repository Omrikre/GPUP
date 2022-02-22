package components.missions;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.State;
import Engine.Tasks.CompilationTask;
import Engine.Tasks.SimulationTask;
import components.app.AppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static Engine.Engine.makeMStoString;
import static components.app.CommonResourcesPaths.TARGET_REFRESH_RATE;
import static components.app.HttpResourcesPaths.*;

public class MissionsController {

    @FXML
    private TableView<MissionDTO> missionTV;
    @FXML
    private TableColumn<MissionDTO, Checkbox> checkboxCOL;
    @FXML
    private TableColumn<?, ?> yourStatusCOL; //TODO
    @FXML
    private TableColumn<MissionDTO, String> missionNameCOL;
    @FXML
    private TableColumn<MissionDTO, String> missionStatusCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> missionProgressCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> missionWorkersCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> missionPriceCOL;
    @FXML
    private TableColumn<MissionDTO, String> missionCreatorCOL;
    @FXML
    private TableColumn<MissionDTO, String> graphNameCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> targetStatFinishedCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> targetStatWaitingCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> typeIndepenCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> typeLeafCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> typeMiddleCOL;
    @FXML
    private TableColumn<MissionDTO, Integer> typeRootCOL;
    @FXML
    private Button startBT;
    @FXML
    private Button pauseBT;
    @FXML
    private Button resumeBT;
    @FXML
    private Button stopBT;
    @FXML
    private Button singupBT;


    @FXML
    void singupPR(ActionEvent event) { //send to server
        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("add-worker", "true")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println(e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(("signup Fail code: " + response.code()) + " " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        System.out.println("successfully added worker to mission");
                    });
                }
            }
        });
    }

    @FXML
    void pausePR(ActionEvent event) {
        //pause=true
        pause = true;
    }

    @FXML
    void resumePR(ActionEvent event) {
        //pause=false
        pause = false;
    }

    @FXML
    void startPR(ActionEvent event) {
        pause = false;
        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("sign-worker", "true")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println(e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(("signup Fail code: " + response.code()) + " " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {

                        System.out.println("successfully added worker to mission");
                    });
                }
            }
        });
    }

    @FXML
    void stopPR(ActionEvent event) { //send to server
        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("add-worker", "false")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println(e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(("signup Fail code: " + response.code()) + " " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        //stop the mission.
                        System.out.println("successfully removed worker from mission!");
                    });
                }
            }
        });
    }

    private void getRunnableTarget(BooleanProperty autoUpdate) { //TODO - close
        this.autoUpdate = autoUpdate;
        runnableTargetRefresher = new RunnableTargetRefresher(
                threadsLeft, selectedMission, this.autoUpdate, this::runTask);
        targetTimer = new Timer();
        targetTimer.schedule(runnableTargetRefresher, TARGET_REFRESH_RATE, TARGET_REFRESH_RATE);
    }


    public void runTask(TargetDTOWithoutCB targetDTOWithoutCB) {
        if (targetDTOWithoutCB == null)
            return;
        if (pause)
            return;

        else {

            final MissionDTOWithoutCB[] m = new MissionDTOWithoutCB[1];
            String finalUrl = HttpUrl
                    .parse(MISSION_LIST)
                    .newBuilder()
                    .addQueryParameter("name", selectedMission)
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            System.out.println(e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                System.out.println(("mission Fail code: " + response.code()) + " " + responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            try {
                                m[0] = GSON.fromJson(response.body().string(), MissionDTOWithoutCB.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            });

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
            threadsLeft.setValue(threadsLeft.getValue() - 1);
            if (m[0].getCompilationFolder() == null) {
                //run sim
                targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
                threadPoolExecutor.execute(new SimulationTask(m[0].getAmountOfTargets(), m[0].getRunTime(), m[0].isRandomRunTime(), targetDTOWithoutCB,
                        m[0].getSuccess(), m[0].getSuccessWithWarnings()));
            } else {
                //run comp
                targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
                threadPoolExecutor.execute(new CompilationTask(m[0].getAmountOfTargets(), m[0].getSrc(), m[0].getCompilationFolder(), targetDTOWithoutCB));
            }
            threadsLeft.setValue(threadsLeft.getValue() + 1);
            threadPoolExecutor.shutdown();
            while (!threadPoolExecutor.isTerminated()) {
                System.out.println("NOT TERMINATED");
            }
            //update progress
            m[0].setProgress();
            //TODO - give price to worker. where is the price for each target, in the graphDTO? maybe add to missionDTO? (only has totalprice)

            //upload updated target to server
            String json = GSON.toJson(targetDTOWithoutCB);
            //first to task server!
            String taskUrl = HttpUrl
                    .parse(MISSION_LIST)
                    .newBuilder()
                    .addQueryParameter("upload", "true")
                    .addQueryParameter("name", selectedMission)
                    .addQueryParameter("json", json)
                    .build()
                    .toString();

            HttpClientUtil.runAsync(taskUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            System.out.println(e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                System.out.println(("upload Fail code: " + response.code()) + " " + responseBody)
                        );
                    } else {
                        Platform.runLater(() -> {
                            System.out.println("uploaded successfully.");
                        });
                    }
                }
            });
        }
    }

    private IntegerProperty threadsLeft; // integer property threads --- //TODO start at numthreads from maincontroller
    private Timer targetTimer;
    private RunnableTargetRefresher runnableTargetRefresher;
    private int numOfMissionsInTable;
    private String selectedMission;
    private IntegerBinding numCheckBoxesSelected;
    private ObservableList<MissionDTO> OLMissions;
    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;
    private boolean pause = false;

    private String selectedGraph;
    private Timer timer;
    private BooleanProperty autoUpdate;
    private int numOfGraphs;
    private AppController mainController;

    @FXML
    public void initialize() {
        numOfMissionsInTable = 0;
        setAllButtonsDisable(true, "", "");
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);
        setupData();
        selectedMission = "";
        singupBT.setDisable(true);
    }

    private void setAllButtonsDisable(boolean bool, String missionStatus, String missionName) {
        if (bool) {
            selectedMission = "";

            startBT.setDisable(true);
            pauseBT.setDisable(true);
            resumeBT.setDisable(true);
            stopBT.setDisable(true);
            singupBT.setDisable(true);
            return;
        }
        if (missionStatus.equals("Paused")) {
            resumeBT.setDisable(false);

        } else if (missionStatus.equals("Ready")) {
            startBT.setDisable(false);

        } else if (missionStatus.equals("Execution")) {
            pauseBT.setDisable(false);
            stopBT.setDisable(false);
        }
        selectedMission = missionName;
        singupBT.setDisable(false);
    }

    public void updateMissionsList(List<MissionDTOWithoutCB> missions) {
        Platform.runLater(() -> {

            if ((missions.size() == numOfMissionsInTable))
                return;
            numOfMissionsInTable = missions.size();

            List<MissionDTO> newMissionList = new ArrayList();
            //ObservableList<MissionDTO> MissionsTV = missionTV.getItems();
            MissionDTO tempDTO;
            CheckBox tempCheckBox;

            for (MissionDTOWithoutCB mission : missions) {
                tempCheckBox = new CheckBox();
                if (selectedMission == mission.getMissionName()) {
                    tempCheckBox.setSelected(true);
                }
                configureCheckBox(tempCheckBox, mission.getMissionName(), mission.getStatus());
                tempDTO = new MissionDTO(mission.getAmountOfTargets(), null, mission.getSrc(), mission.getCompilationFolder(), mission.getRunTime(), mission.isRandomRunTime(), mission.getSuccess(), mission.getSuccessWithWarnings(), mission.getMissionName()
                        , mission.getStatus(), mission.getProgress().toString() + "%", mission.getWorkers(), mission.getTotalPrice(), mission.getCreatorName(), mission.getGraphName(),
                        mission.getExecutedTargets(), mission.getWaitingTargets(), tempCheckBox, mission.getIndependenceCount(), mission.getLeafCount(), mission.getMiddleCount(), mission.getRootCount());

                newMissionList.add(tempDTO);
            }
            ObservableList<MissionDTO> OLMission = FXCollections.observableArrayList(newMissionList);
            //OLGraphs.clear();
            OLMissions = OLMission;
            missionTV.setItems(OLMissions);
            missionTV.refresh();
        });

    }


    private void setMissionSelected(String missionStatus, String missionName, boolean bool) {
        if (!bool)
            setAllButtonsDisable(true, "", "");
        else {
            setAllButtonsDisable(false, missionStatus, missionName);
        }
    }

    private void configureCheckBox(CheckBox checkBox, String missionName, String missionStatus) {
        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
            setMissionSelected(missionStatus, missionName, true);
        } else {
            unselectedCheckBoxes.add(checkBox);
            setMissionSelected("", "", false);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
                setMissionSelected(missionStatus, missionName, true);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                setMissionSelected("", "", false);

            }
        });
    }

    public void setupData() {
        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() == 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
            } else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
            }
        });
    }
}
