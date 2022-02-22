package components.missions;

import Engine.DTO.GraphDTO;
import Engine.DTO.MissionDTO;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.DTO.TargetDTO;
import components.app.AppController;
import components.graphManager.xmlLoader.LoadXMLRefresher;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static components.app.HttpResourcesPaths.ADD_MISSION;
import static components.app.HttpResourcesPaths.MISSION_LIST;

public class MissionsController {

    @FXML
    private TableView<MissionDTO> missionTV;
    @FXML
    private TableColumn<MissionDTO, Checkbox> checkboxCOL;
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
    private Button dupScratchBT;
    @FXML
    private Button dupIncrementalBT;

    private int numOfMissionsInTable;
    private String selectedMission;
    private IntegerBinding numCheckBoxesSelected;
    private ObservableList<MissionDTO> OLMissions;
    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;

    private String selectedGraph;
    private Timer timer;
    private BooleanProperty autoUpdate;
    private int numOfGraphs;
    private AppController mainController;
    private String httpUrl;
    private boolean stopRefrash;

    @FXML
    public void initialize() {
        numOfMissionsInTable = 0;
        setAllButtonsDisable(true, "", "");
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);
        setupData();
        selectedMission = "";
        dupScratchBT.setDisable(false); //TODO 123
        dupIncrementalBT.setDisable(false);
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

    private void setAllButtonsDisable(boolean bool, String missionStatus, String missionName) {
        if (bool) {
            //selectedMission = "";

            startBT.setDisable(true);
            pauseBT.setDisable(true);
            resumeBT.setDisable(true);
            stopBT.setDisable(true);
            dupScratchBT.setDisable(true);
            dupIncrementalBT.setDisable(true);
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
        //selectedMission = missionName;
        dupScratchBT.setDisable(false);
        dupIncrementalBT.setDisable(false);
    }

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
                System.out.println("------------- selected: " + missionName);
                selectedMission = missionName;
                stopRefrash = true;
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                setMissionSelected("", "", false);
                selectedMission = "";
                stopRefrash = false;
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


    @FXML
    void dupIncrementalPR(ActionEvent event) {
        mainController.openCreateNewMissionWin(false, selectedMission);
        unselectAll();
    }

    @FXML
    void dupScratchPR(ActionEvent event) {
        mainController.openCreateNewMissionWin(true, selectedMission);
        unselectAll();
    }

    @FXML
    void pausePR(ActionEvent event) {
        httpUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("status", "Paused")
                .build()
                .toString();
        sendRequest();
        unselectAll();
    }

    @FXML
    void resumePR(ActionEvent event) {
        httpUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("status", "Ready")
                .build()
                .toString();
        sendRequest();
        unselectAll();
    }

    @FXML
    void startPR(ActionEvent event) {
        httpUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("status", "Execution")
                .build()
                .toString();
        sendRequest();
        unselectAll();
    }

    @FXML
    void stopPR(ActionEvent event) {
        httpUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", selectedMission)
                .addQueryParameter("status", "Stopped")
                .build()
                .toString();
        sendRequest();
        unselectAll();
    }

    private void sendRequest() {

        HttpClientUtil.runAsync(httpUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        }});
    }

    private void unselectAll() {
        selectedCheckBoxes.forEach(cb -> cb.setSelected(false));
    }




    public void setMainController(AppController appController) {
        this.mainController = appController;
    }



}
