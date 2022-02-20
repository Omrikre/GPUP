package components.missions;

import Engine.DTO.GraphDTO;
import Engine.DTO.MissionDTO;

import Engine.DTO.MissionDTOWithoutCB;
import components.app.AppController;
import components.graphManager.xmlLoader.LoadXMLRefresher;
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
import okhttp3.HttpUrl;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static components.app.HttpResourcesPaths.ADD_MISSION;

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
    private LoadXMLRefresher xmlRefresher;
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
        dupScratchBT.setDisable(false); //TODO 123
        dupIncrementalBT.setDisable(false);
    }

    private void setAllButtonsDisable(boolean bool, String missionStatus, String missionName) {
        if (bool) {
            selectedMission = "";

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
        selectedMission = missionName;
        dupScratchBT.setDisable(false);
        dupIncrementalBT.setDisable(false);
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
                        , mission.getStatus(), mission.getProgress(), mission.getWorkers(), mission.getTotalPrice(), mission.getCreatorName(), mission.getGraphName(),
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


    @FXML
    void dupIncrementalPR(ActionEvent event) {
        mainController.openCreateNewMissionWin(false, selectedMission);
    }

    @FXML
    void dupScratchPR(ActionEvent event) {
        mainController.openCreateNewMissionWin(true, selectedMission);
    }

    @FXML
    void pausePR(ActionEvent event) {
    }

    @FXML
    void resumePR(ActionEvent event) {
    }

    @FXML
    void startPR(ActionEvent event) {
    }

    @FXML
    void stopPR(ActionEvent event) {
    }

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    //Engine e, minigraph of chosen targets, get the numbers, send 4 new params - ind, roots, leaves, middles.

//    String finalUrl = HttpUrl
//            .parse(ADD_MISSION)
//            .newBuilder()
//            .addQueryParameter("amount-of-targets", amountOfTargets)
//            .addQueryParameter("compilation-folder", compilationFolder) //for compilation task, else null
//    .addQueryParameter("runtime", runTime)
//    .addQueryParameter("random-runtime",randomRunTime)
//    .addQueryParameter("success", success)
//    .addQueryParameter("success-warnings", successWithWarnings)
//    //the rest are for the display:
//    .addQueryParameter("name",missionName)
//    .addQueryParameter("creator",creatorName)
//    .addQueryParameter("graph-name",graphName)
//            .build()
//            .toString();

//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//        @Override
//        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            Platform.runLater(() ->
//                    .setText("Something went wrong: " + e.getMessage())
//            );
//        }
//
//        @Override
//        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            if (response.code() != 200) {
//                String responseBody = response.body().string();
//                Platform.runLater(() ->
//                        .setText("Something went wrong: " + responseBody)
//                );
//            } else {
//                Platform.runLater(() -> {
//
//                    try {
//                        String responseBody = response.body().string();
//                        System.out.println(responseBody);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//    });


}
