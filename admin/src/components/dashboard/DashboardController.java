package components.dashboard;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.users.User;
import components.app.AppController;
import components.missions.MissionsController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import Engine.Enums.MissionState;
import java.io.Closeable;
import java.util.List;
import java.util.Timer;


import static Engine.Enums.MissionState.*;

import static components.app.CommonResourcesPaths.REFRESH_RATE;


public class DashboardController implements Closeable {

    @FXML private TableView<User> userListTV;
    @FXML private TableColumn<String, User> userNameCOL;
    @FXML private TableColumn<String, User> userTypeCOL;
    @FXML private Label totalUsersLB;
    @FXML private Label adminsLB;
    @FXML private Label workersLB;
    @FXML private TableView<MissionDTOWithoutCB> missionListTV;
    @FXML private TableColumn<String, MissionDTOWithoutCB> missionNameCOL;
    @FXML private TableColumn<String, MissionDTOWithoutCB> missionStatusCOL;
    @FXML private Label totalMissionsLB;
    @FXML private Label executionLB;
    @FXML private Label waitingLB;
    @FXML private Label finishedLB;

    private DashboardRefresher dashboardRefresher;
    private AppController mainController;
    private BooleanProperty autoUpdate;
    private Timer dashboardTimer;
    private MissionsController missionController;


    public void setMainController(AppController appController, MissionsController missionController) {
        this.missionController = missionController;
        this.mainController = appController;
    }

    @FXML public void initialize() {
        userNameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
        userTypeCOL.setCellValueFactory(new PropertyValueFactory<>("role"));
        userNameCOL.setStyle( "-fx-alignment: CENTER;");
        userTypeCOL.setStyle( "-fx-alignment: CENTER;");

        missionNameCOL.setCellValueFactory(new PropertyValueFactory<>("missionName"));
        missionStatusCOL.setCellValueFactory(new PropertyValueFactory<>("status"));
        missionNameCOL.setStyle( "-fx-alignment: CENTER;");
        missionStatusCOL.setStyle( "-fx-alignment: CENTER;");

        //autoUpdate = mainController.getAutoUpdate();
    }

    private void updateUsersList(List<User> userNames) {
        Platform.runLater(() -> {
            ObservableList<User> usersTV = userListTV.getItems();
            usersTV.clear();
            usersTV.addAll(userNames);
            updateUsersLabels(userNames);
        });
    }
    private void updateMissionsListInDashboard(List<MissionDTOWithoutCB> missions) {
        Platform.runLater(() -> {
            //System.out.println(missions);
            ObservableList<MissionDTOWithoutCB> missionsTV = missionListTV.getItems();
            missionsTV.clear();
            missionsTV.addAll(missions);
            //missionListTV.refresh();
            updateMissionLabels(missions);
        });
    }

    private void updateMissionsInMissionTab(List<MissionDTOWithoutCB> missions) {
        missionController.updateMissionsList(missions);
    }

    private void updateMissionLabels(List<MissionDTOWithoutCB> missions) {
        Platform.runLater(() -> {
            Integer numOfExecution = 0, numberOfPaused = 0, numOfStopped = 0, numOfFinished = 0, numOfReady = 0, totalCount = 0;
            for (MissionDTOWithoutCB mission : missions) {
                String tempMissionStatus = (mission.getStatus());
                if (tempMissionStatus.equals("Paused") || tempMissionStatus.equals("Ready"))
                    numOfReady++;
                else if (tempMissionStatus.equals("Stopped") || tempMissionStatus.equals("Finished"))
                    numOfFinished++;
                else
                    numOfExecution++;
                totalCount++;
            }
            totalMissionsLB.setText(totalCount.toString());
            executionLB.setText(numOfExecution.toString());
            waitingLB.setText(numOfReady.toString());
            finishedLB.setText((numOfFinished).toString());
        });
    }

    private void updateUsersLabels(List<User> usersNames) {
        Platform.runLater(() -> {
            Integer numberOfUsers = 0, numOfAdmins = 0, numOfWorkers = 0;
            for (User user : usersNames) {
                if (user.getRole().equals("Admin"))
                    numOfAdmins++;
                else
                    numOfWorkers++;
                numberOfUsers++;
            }
            totalUsersLB.setText(numberOfUsers.toString());
            adminsLB.setText(numOfAdmins.toString());
            workersLB.setText(numOfWorkers.toString());
        });
    }

    public void startListRefresher(BooleanProperty autoUpdate) {
        this.autoUpdate = autoUpdate;
        dashboardRefresher = new DashboardRefresher(
                this.autoUpdate, this::updateUsersList, this::updateMissionsListInDashboard, this::updateMissionsInMissionTab);
        dashboardTimer = new Timer();
        dashboardTimer.schedule(dashboardRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void close() {
        if (dashboardRefresher != null && dashboardTimer != null) {
            dashboardRefresher.cancel();
            dashboardTimer.cancel();
        }
        userListTV.getItems().clear();
        missionListTV.getItems().clear();
    }

    public void UsersListController() {autoUpdate = new SimpleBooleanProperty(true);}
    public BooleanProperty autoUpdatesProperty() {return autoUpdate;}

    public void closeDashboard() {
        if (dashboardTimer != null) {
            dashboardRefresher.cancel();
            dashboardTimer.cancel();
        }
        userListTV.getItems().clear();
        userListTV.refresh();
    }
}


