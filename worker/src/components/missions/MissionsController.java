package components.missions;

import Engine.DTO.MissionDTO;
import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.DTO.TargetForWorkerDTO;
import Engine.Enums.MissionState;
import Engine.Enums.State;
import Engine.Tasks.CompilationTask;
import Engine.Tasks.SimulationTask;
import com.google.gson.Gson;
import components.app.AppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<MissionDTO, String> yourStatusCOL;
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

    private IntegerProperty threadsLeft; // integer property threads --- //TODO start at numthreads from maincontroller
    private Timer targetTimer;
    private RunnableTargetRefresher runnableTargetRefresher;
    private int numOfMissionsInTable;
    private String selectedMission;
    private String missionStatus;
    private IntegerBinding numCheckBoxesSelected;
    private ObservableList<MissionDTO> OLMissions;
    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;
    private boolean pause = false;
    private int creds;

    private String selectedGraph;
    private Timer timer;
    private BooleanProperty autoUpdate;
    private int numOfGraphs;
    private AppController mainController;
    private MissionDTOWithoutCB missionM;
    private boolean stopRefrash;
    private Set<String> regSet = new HashSet<>();
    private Set<String> pausedSet = new HashSet<>();
    private Set<String> runningSet = new HashSet<>();
    private Set<String> finSet = new HashSet<>();


    @FXML
    public void initialize() {
        numOfMissionsInTable = 0;
        setAllButtonsDisable(true, "", "");
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);
        setupData();
        selectedMission = "";
        stopRefrash = false;

        checkboxCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Checkbox>("selectedState"));
        missionNameCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, String>("missionName"));
        missionStatusCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, String>("status"));
        missionProgressCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("progress"));
        missionWorkersCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("workers"));
        missionPriceCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("totalPrice"));
        missionCreatorCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, String>("creatorName"));
        graphNameCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, String>("graphName"));
        targetStatFinishedCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("executedTargets"));
        targetStatWaitingCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("waitingTargets"));
        typeIndepenCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("independenceCount"));
        typeLeafCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("leafCount"));
        typeMiddleCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("middleCount"));
        typeRootCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, Integer>("rootCount"));
        yourStatusCOL.setCellValueFactory(new PropertyValueFactory<MissionDTO, String>("myStatus"));

        yourStatusCOL.setStyle("-fx-alignment: CENTER;");
        checkboxCOL.setStyle("-fx-alignment: CENTER;");
        missionNameCOL.setStyle("-fx-alignment: CENTER;");
        missionStatusCOL.setStyle("-fx-alignment: CENTER;");
        missionProgressCOL.setStyle("-fx-alignment: CENTER;");
        missionWorkersCOL.setStyle("-fx-alignment: CENTER;");
        missionPriceCOL.setStyle("-fx-alignment: CENTER;");
        missionCreatorCOL.setStyle("-fx-alignment: CENTER;");
        graphNameCOL.setStyle("-fx-alignment: CENTER;");
        targetStatFinishedCOL.setStyle("-fx-alignment: CENTER;");
        targetStatWaitingCOL.setStyle("-fx-alignment: CENTER;");
        typeIndepenCOL.setStyle("-fx-alignment: CENTER;");
        typeLeafCOL.setStyle("-fx-alignment: CENTER;");
        typeMiddleCOL.setStyle("-fx-alignment: CENTER;");
        typeRootCOL.setStyle("-fx-alignment: CENTER;");
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }


    @FXML
    void singupPR(ActionEvent event) { //send to server
        ifContainsDelete();
        regSet.add(selectedMission);
        unselectAll();
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
                        regSet.add(selectedMission);
                    });
                }
            }
        });
    }

    public IntegerProperty getThreadsLeft() {
        return threadsLeft;
    }

    @FXML
    void pausePR(ActionEvent event) {
        pause = true;
        ifContainsDelete();
        pausedSet.add(selectedMission);
        unselectAll();
    }

    @FXML
    void resumePR(ActionEvent event) {
        pause = false;
        ifContainsDelete();
        runningSet.add(selectedMission);
        unselectAll();
    }

    @FXML
    void startPR(ActionEvent event) {
        for (MissionDTO m : OLMissions) {
            if (m.getMissionName().equals(selectedMission))
                missionStatus = m.getStatus();
        }
        if (missionStatus.equals(MissionState.EXECUTION.toString())) {
            pause = false;
            ifContainsDelete();
            runningSet.add(selectedMission);
            String n = selectedMission;
            System.out.println("MISSION: " + selectedMission);
            unselectAll();
            selectedMission = n;
            String finalUrl = HttpUrl
                    .parse(MISSION_LIST)
                    .newBuilder()
                    .addQueryParameter("name", selectedMission)
                    .addQueryParameter("add-worker", "true")
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
                            //TODO - HERE
                        });
                    }
                }
            });
        }
    }

    @FXML
    void stopPR(ActionEvent event) { //send to server
        ifContainsDelete();
        unselectAll();
        pause = true;
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

    public void getRunnableTarget(BooleanProperty autoUpdate) {
        this.autoUpdate = autoUpdate;
        runnableTargetRefresher = new RunnableTargetRefresher(
                threadsLeft, selectedMission, this.autoUpdate, this::runTask);
        targetTimer = new Timer();
        targetTimer.schedule(runnableTargetRefresher, TARGET_REFRESH_RATE, TARGET_REFRESH_RATE);
    }

    public void runMission(TargetForWorkerDTO t, boolean isComp, String folder) {
        new Thread(() -> {
            System.out.println("RUN MISSION");
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                    60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
            System.out.println("THREADS LEFT BEFORE TASK " + threadsLeft.getValue());
            if (t.getCompilationFolder() == null) {
                //run sim
                //t.getT().setTargetState(State.IN_PROCESS);
                threadPoolExecutor.execute(new SimulationTask(folder, t.getAmountOfTargets(), t.getRunTime(), t.isRandomRunTime(), t,
                        t.getSuccess(), t.getSuccessWithWarnings()));
                threadsLeft.setValue(threadsLeft.getValue() - 1);
            } else {
                //run comp
                //t.getT().setTargetState(State.IN_PROCESS);
                threadPoolExecutor.execute(new CompilationTask(folder, t.getAmountOfTargets(), t.getSrc(), t.getCompilationFolder(), t));
                threadsLeft.setValue(threadsLeft.getValue() - 1);
            }
            threadPoolExecutor.shutdown();
            while (!threadPoolExecutor.isTerminated()) {
                System.out.println("NOT TERMINATED");
            }
            System.out.println("TARGET STATE: " + t.getT().getTargetState());
            threadsLeft.setValue(threadsLeft.getValue() + 1);
            System.out.println("THREADS LEFT AFTER TASK " + threadsLeft.getValue());
            if (t.getT().getTargetState().equals(State.FINISHED_WARNINGS) || t.getT().getTargetState().equals(State.FINISHED_SUCCESS) || t.getT().getTargetState().equals(State.FINISHED_FAILURE)) {
                if (isComp) {
                    creds = (t.getT().getCompCreds());

                    //add comp credits to worker
                } else {

                    creds = (t.getT().getSimCreds());

                    //add sim credits to worker
                }
                //upload updated target to server
                String json = GSON.toJson(t.getT());
                System.out.println("Target: " + json);
                //first to task server!
                String taskUrl = HttpUrl
                        .parse(MISSION_LIST)
                        .newBuilder()
                        .addQueryParameter("upload", "true")
                        .addQueryParameter("name", t.getMissionName())
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
                                System.out.println("CREDITS: " + creds);
                                mainController.setCredits(creds);
                                System.out.println("uploaded successfully.");
                            });
                        }
                    }
                });
            }
        }
        ).start();
    }

    public void closeChat() {
        if (targetTimer != null && runnableTargetRefresher != null) {
            runnableTargetRefresher.cancel();
            targetTimer.cancel();
        }
    }
/*

    private void runOnMission(TargetDTOWithoutCB targetDTOWithoutCB) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
        threadsLeft.setValue(threadsLeft.getValue() - 1);
        if (missionM.getCompilationFolder() == null) {
            //run sim
            targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
            threadPoolExecutor.execute(new SimulationTask(missionM.getAmountOfTargets(), missionM.getRunTime(), missionM.isRandomRunTime(), targetDTOWithoutCB,
                    missionM.getSuccess(), missionM.getSuccessWithWarnings()));
        } else {
            //run comp
            targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
            threadPoolExecutor.execute(new CompilationTask(missionM.getAmountOfTargets(), missionM.getSrc(), missionM.getCompilationFolder(), targetDTOWithoutCB));
        }
        threadPoolExecutor.shutdown();
        threadsLeft.setValue(threadsLeft.getValue() + 1);
        missionM.setProgress();
        System.out.println("PROGRESS: " + missionM.getProgress());
        //upload updated target to server
        String json = GSON.toJson(targetDTOWithoutCB);
        System.out.println("Target: " +json);
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

*/

    public void runTask(TargetDTOWithoutCB targetDTOWithoutCB) {
        System.out.println("RUN TASK");
        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("json", GSON.toJson(runningSet))
                .addQueryParameter("special", "true")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("FAIL " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(("target Fail code: " + response.code()) + " " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            Gson gson = new Gson();
                            String responseBody = response.body().string();
                            System.out.println("RUNTASK JSON: " + responseBody);
                            TargetForWorkerDTO t = gson.fromJson(responseBody, TargetForWorkerDTO.class);
                            System.out.println("RUNTASK TARGET: " + t);
                            boolean isComp = false;
                            String directoryPath;
                            if (t != null) {
                                if (t.getCompilationFolder() != null) {
                                    isComp = true;
                                    //create comp folder
                                    directoryPath = "c:\\gpup-working-dir" + "\\" + "Compilation" + " - " + makeMStoString(System.currentTimeMillis()).replace(":", ".");
                                    File dir = new File(directoryPath);
                                    dir.mkdirs();
                                } else {//create sim folder
                                    directoryPath = "c:\\gpup-working-dir" + "\\" + "Simulation" + " - " + makeMStoString(System.currentTimeMillis()).replace(":", ".");
                                    File dir = new File(directoryPath);
                                    dir.mkdirs();
                                }
                                //send those in parameter
                                runMission(t, isComp, directoryPath);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });


        //TODO upload to server?
        //TODO - give price to worker. where is the price for each target, in the graphDTO? maybe add to missionDTO? (only has totalprice)


    }


    public void setUpThreads(int threads) {
        threadsLeft = new SimpleIntegerProperty(threads);
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

        selectedMission = missionName;

        if (regSet.contains(selectedMission)) {
            startBT.setDisable(false);
            pauseBT.setDisable(true);
            resumeBT.setDisable(true);
            stopBT.setDisable(true);
            singupBT.setDisable(true);
        } else if (runningSet.contains(selectedMission)) {
            startBT.setDisable(true);
            pauseBT.setDisable(false);
            resumeBT.setDisable(true);
            stopBT.setDisable(false);
            singupBT.setDisable(true);
        } else if (pausedSet.contains(selectedMission)) {
            startBT.setDisable(true);
            pauseBT.setDisable(true);
            resumeBT.setDisable(false);
            stopBT.setDisable(false);
            singupBT.setDisable(true);
        } else {
            startBT.setDisable(true);
            pauseBT.setDisable(true);
            resumeBT.setDisable(true);
            stopBT.setDisable(true);
            singupBT.setDisable(false);
        }

    }

    private void ifContainsDelete() {
        if (regSet.contains(selectedMission)) {
            regSet.remove(selectedMission);
        }
        if (pausedSet.contains(selectedMission)) {
            pausedSet.remove(selectedMission);
        }
        if (runningSet.contains(selectedMission)) {
            runningSet.remove(selectedMission);
        }
    }

    public void updateMissionsList(List<MissionDTOWithoutCB> missions) {

        if (stopRefrash)
            return;
        numOfMissionsInTable = missions.size();

        List<MissionDTO> newMissionList = new ArrayList();
        ObservableList<MissionDTO> MissionsTV = missionTV.getItems();
        MissionDTO tempDTO;
        CheckBox tempCheckBox;

        for (MissionDTOWithoutCB mission : missions) {
            tempCheckBox = new CheckBox();
            String myCurrStatus = "Unregistered";
            if (mission.getStatus() == "Finished") {
                ifContainsDelete();
                finSet.add(mission.getMissionName());
            } else if (regSet.contains(mission.getMissionName())) {
                myCurrStatus = "Registered";
            } else if (finSet.contains(mission.getMissionName())) {
                myCurrStatus = "Finished";
            } else if (pausedSet.contains(mission.getMissionName())) {
                myCurrStatus = "Paused";
            } else if (runningSet.contains(mission.getMissionName())) {
                myCurrStatus = "Running";
            }

            if (selectedMission == mission.getMissionName()) {
                tempCheckBox.setSelected(true);
            }
            configureCheckBox(tempCheckBox, mission.getMissionName(), myCurrStatus);
            tempDTO = new MissionDTO(mission.getAmountOfTargets(), null, mission.getSrc(), mission.getCompilationFolder(), mission.getRunTime(), mission.isRandomRunTime(), mission.getSuccess(), mission.getSuccessWithWarnings(), mission.getMissionName()
                    , mission.getStatus(), mission.getProgress().toString() + "%", mission.getWorkers(), mission.getTotalPrice(), mission.getCreatorName(), mission.getGraphName(),
                    mission.getExecutedTargets(), mission.getWaitingTargets(), mission.getRootCount(), mission.getIndependenceCount(), mission.getLeafCount(), mission.getMiddleCount(), myCurrStatus, tempCheckBox);

            newMissionList.add(tempDTO);
        }
        ObservableList<MissionDTO> OLMission = FXCollections.observableArrayList(newMissionList);
        //OLGraphs.clear();
        OLMissions = OLMission;
        missionTV.setItems(OLMissions);
        //missionTV.refresh();
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
                System.out.println("------------- selected: " + missionName);
                selectedMission = missionName;
                stopRefrash = true;
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                setMissionSelected("", "", false);
                System.out.println("------------- unselected: " + missionName);
                selectedMission = "";
                stopRefrash = false;
            }
        });
    }

    private void setMissionSelected(String missionStatus, String missionName, boolean bool) {
        if (!bool)
            setAllButtonsDisable(true, "", "");
        else {
            setAllButtonsDisable(false, missionStatus, missionName);
        }
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

    private void unselectAll() {
        selectedCheckBoxes.forEach(cb -> cb.setSelected(false));
        setMissionSelected("", "", false);
    }


/*

    public void updateMissionsList(List<MissionDTOWithoutCB> missions) {
        if (stopRefrash)
            return;

        numOfMissionsInTable = missions.size();

        List<MissionDTO> newMissionList = new ArrayList();
        //ObservableList<MissionDTO> MissionsTV = missionTV.getItems();
        MissionDTO tempDTO;
        CheckBox tempCheckBox;

        for (MissionDTOWithoutCB mission : missions) {
            tempCheckBox = new CheckBox();
            if (selectedMission.equals(mission.getMissionName())) {
                tempCheckBox.setSelected(true);
            }
            configureCheckBox(tempCheckBox, mission.getMissionName(), mission.getStatus());
            tempDTO = new MissionDTO(mission.getAmountOfTargets(), new ArrayList<>(), mission.getSrc(), mission.getCompilationFolder(), mission.getRunTime(), mission.isRandomRunTime(), mission.getSuccess(), mission.getSuccessWithWarnings(), mission.getMissionName()
                    , mission.getStatus(), mission.getProgress().toString() + "%", mission.getWorkers(), mission.getTotalPrice(), mission.getCreatorName(), mission.getGraphName(),
                    mission.getExecutedTargets(), mission.getWaitingTargets(), tempCheckBox, mission.getIndependenceCount(), mission.getLeafCount(), mission.getMiddleCount(), mission.getRootCount());

            newMissionList.add(tempDTO);
        }
        ObservableList<MissionDTO> OLMission = FXCollections.observableArrayList(newMissionList);
        OLMissions = OLMission;
        missionTV.setItems(OLMissions);
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
    }*/
}

/* //TODO GET SPECIAL TARGET

        String finalUrl = HttpUrl
        .parse(MISSION_LIST)
        .newBuilder()
        .addQueryParameter("json", targetsSet)
        .addQueryParameter("special", true)
        .build()
        .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
@Override
public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Platform.runLater(() ->
        System.out.println("FAIL " + e.getMessage())
        );
        }

@Override
public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.code() != 200) {
        String responseBody = response.body().string();
        Platform.runLater(() ->
        System.out.println(("target Fail code: " + response.code()) + " " + responseBody)
        );
        } else {
        Platform.runLater(() -> {
        try {
        Gson gson = new Gson();
        String responseBody = response.body().string();
        TargetForWorkerDTO t = gson.fromJson(responseBody, TargetForWorkerDTO.class);
        runMission(t);
        } catch (IOException e) {
        e.printStackTrace();
        }
        });
        }
        }
        });

        */

/*
    public void runMission(TargetForWorkerDTO t){
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
//                60, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
//        threadsLeft.setValue(threadsLeft.getValue() - 1);
//        if (t.getCompilationFolder() == null) {
//            //run sim
//            targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
//            threadPoolExecutor.execute(new SimulationTask(missionM.getAmountOfTargets(), missionM.getRunTime(), missionM.isRandomRunTime(), targetDTOWithoutCB,
//                    missionM.getSuccess(), missionM.getSuccessWithWarnings()));
//        } else {
//            //run comp
//            targetDTOWithoutCB.setTargetState(State.IN_PROCESS);
//            threadPoolExecutor.execute(new CompilationTask(missionM.getAmountOfTargets(), missionM.getSrc(), missionM.getCompilationFolder(), targetDTOWithoutCB));
//        }
//        threadPoolExecutor.shutdown();
//        threadsLeft.setValue(threadsLeft.getValue() + 1);
//        missionM.setProgress();
//        System.out.println("PROGRESS: " + missionM.getProgress());
//        //upload updated target to server
//        String json = GSON.toJson(targetDTOWithoutCB);
//        System.out.println("Target: " +json);
//        //first to task server!
//        String taskUrl = HttpUrl
//                .parse(MISSION_LIST)
//                .newBuilder()
//                .addQueryParameter("upload", "true")
//                .addQueryParameter("name", selectedMission)
//                .addQueryParameter("json", json)
//                .build()
//                .toString();
//
//        HttpClientUtil.runAsync(taskUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() ->
//                        System.out.println(e.getMessage())
//                );
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() ->
//                            System.out.println(("upload Fail code: " + response.code()) + " " + responseBody)
//                    );
//                } else {
//                    Platform.runLater(() -> {
//                        System.out.println("uploaded successfully.");
//                    });
//                }
//            }
//        });
//    }

    }



 */