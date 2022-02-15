package components.missions;

import Engine.DTO.MissionDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;

public class MissionsController {

    @FXML private TableView<?> missionTV;
    @FXML private TableColumn<MissionDTO, Checkbox> checkboxCOL;
    @FXML private TableColumn<MissionDTO, String> missionNameCOL;
    @FXML private TableColumn<MissionDTO, String> missionStatusCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionProgressCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionWorkersCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionPriceCOL;
    @FXML private TableColumn<MissionDTO, String> missionCreatorCOL;
    @FXML private TableColumn<MissionDTO, String> graphNameCOL;
    @FXML private TableColumn<MissionDTO, Integer> targetStatFinishedCOL;
    @FXML private TableColumn<MissionDTO, Integer> targetStatWaitingCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeIndepenCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeLeafCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeMiddleCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeRootCOL;
    @FXML private Button startBT;
    @FXML private Button pauseBT;
    @FXML private Button resumeBT;
    @FXML private Button stopBT;
    @FXML private Button dupScratchBT;
    @FXML private Button dupIncrementalBT;

    @FXML void dupIncrementalPR(ActionEvent event) {}
    @FXML void dupScratchPR(ActionEvent event) {}
    @FXML void pausePR(ActionEvent event) {}
    @FXML void resumePR(ActionEvent event) {}
    @FXML void startPR(ActionEvent event) {}
    @FXML void stopPR(ActionEvent event) {}

}
