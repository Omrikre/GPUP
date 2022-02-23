package components.targets;

import Engine.DTO.MissionDTO;
import Engine.DTO.TargetForWorkerDTO;
import components.app.AppController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static components.app.CommonResourcesPaths.REFRESH_RATE;

public class TargetsListController {

    @FXML
    private TableView<TargetForWorkerDTO> missionTV;
    @FXML
    private TableColumn<TargetForWorkerDTO, String> missionNameCOL;
    @FXML
    private TableColumn<TargetForWorkerDTO, String> taskTypeCOL;
    @FXML
    private TableColumn<TargetForWorkerDTO, String> targetNameCOL;
    @FXML
    private TableColumn<TargetForWorkerDTO, String> missionStatusCOL;
    @FXML
    private TableColumn<TargetForWorkerDTO, Integer> receivedCoinsCOL;
    @FXML
    private Label availableThreadsLB;
    @FXML
    private Label totalThreadsLB;
    private BooleanProperty autoUpdate;
    private TargetRefresher targetRefresher;
    private Timer targetTimer;
    private ObservableList<TargetForWorkerDTO> targetsOL;
    private AppController mainController;
    private IntegerProperty threadsLeft;

    @FXML
    public void initialize() {
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

    public void startListRefresher(BooleanProperty autoUpdate) {
        this.autoUpdate = autoUpdate;
        targetRefresher = new TargetRefresher(
                this::updateTargetTable, this.autoUpdate);
        targetTimer = new Timer();
        targetTimer.schedule(targetRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void closeTargets() {
        if (targetRefresher != null && targetTimer != null) {
            targetTimer.cancel();
            targetRefresher.cancel();
        }
    }

    public void setThreads(IntegerProperty threadsLeft) {
        totalThreadsLB.setText(String.valueOf(mainController.getNumThreads()));
        this.threadsLeft = threadsLeft;
        availableThreadsLB.setText(threadsLeft.getValue().toString());
    }

    public void updateTargetTable(List<TargetForWorkerDTO> targetList) {
        if (targetList == null)
            return;
        List<TargetForWorkerDTO> newTargetList = new ArrayList();
        //ObservableList<TargetForWorkerDTO> MissionsTV = missionTV.getItems();
        MissionDTO tempDTO;

        newTargetList.addAll(targetList);
        ObservableList<TargetForWorkerDTO> targetsOL = FXCollections.observableArrayList(newTargetList);
        //missionTV.clear();
        this.targetsOL = targetsOL;
        missionTV.setItems(targetsOL);
        missionTV.refresh();
        availableThreadsLB.setText(threadsLeft.getValue().toString());

    }

    public void updateThreadsLeftLabel(int threadsLeft) {
        availableThreadsLB.setText(String.valueOf(threadsLeft));
    }


    public void setMainController(AppController appController) {
        this.mainController = appController;
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