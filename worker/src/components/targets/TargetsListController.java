package components.targets;

import Engine.DTO.MissionDTO;
import Engine.DTO.TargetForWorkerDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TargetsListController {

    @FXML private TableView<TargetForWorkerDTO> missionTV;
    @FXML private TableColumn<TargetForWorkerDTO, String> missionNameCOL;
    @FXML private TableColumn<TargetForWorkerDTO, String> taskTypeCOL;
    @FXML private TableColumn<TargetForWorkerDTO, String> targetNameCOL;
    @FXML private TableColumn<TargetForWorkerDTO, String> missionStatusCOL;
    @FXML private TableColumn<TargetForWorkerDTO, Integer> receivedCoinsCOL;
    @FXML private Label availableThreadsLB;
    @FXML private Label totalThreadsLB;


    @FXML public void initialize() {
        missionNameCOL.setCellValueFactory(new PropertyValueFactory<TargetForWorkerDTO, String>("missionName"));
        taskTypeCOL.setCellValueFactory(new PropertyValueFactory<TargetForWorkerDTO, String>("taskType"));
        targetNameCOL.setCellValueFactory(new PropertyValueFactory<TargetForWorkerDTO, String>("targetName"));
        missionStatusCOL.setCellValueFactory(new PropertyValueFactory<TargetForWorkerDTO, String>("status"));
        receivedCoinsCOL.setCellValueFactory(new PropertyValueFactory<TargetForWorkerDTO, Integer>("credits"));

        missionNameCOL.setStyle("-fx-alignment: CENTER;");
        taskTypeCOL.setStyle("-fx-alignment: CENTER;");
        targetNameCOL.setStyle("-fx-alignment: CENTER;");
        missionStatusCOL.setStyle("-fx-alignment: CENTER;");
        receivedCoinsCOL.setStyle("-fx-alignment: CENTER;");
    }


}


/*
//TODO for the worker to get the list of targets from the task he wants to work on when he starts to work.
String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name","")
                .addQueryParameter("targets","true")
                .build()
                .toString();


 */


/*
MAKE TASK AND RUN IT


 */