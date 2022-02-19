package components.dashboard;

import Engine.DTO.MissionDTO;
import Engine.users.User;
import components.app.AppController;
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
    @FXML private TableView<MissionDTO> missionListTV;
    @FXML private TableColumn<String, MissionDTO> missionNameCOL;
    @FXML private TableColumn<String, MissionDTO> missionStatusCOL;
    @FXML private Label totalMissionsLB;
    @FXML private Label executionLB;
    @FXML private Label waitingLB;
    @FXML private Label finishedLB;

    private DashboardRefresher dashboardRefresher;
    private AppController mainController;
    private BooleanProperty autoUpdate;
    private Timer timer;


    public void setMainController(AppController appController) {
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
        //userListTV.setItems(newItems); //TODO
    }

    private void updateUsersList(List<User> userNames) {
        Platform.runLater(() -> {
            ObservableList<User> usersTV = userListTV.getItems();
            usersTV.clear();
            usersTV.addAll(userNames);
            updateUsersLabels(userNames);
        });
    }
    private void updateMissionsList(List<MissionDTO> missions) {
        Platform.runLater(() -> {
            ObservableList<MissionDTO> missionsTV = missionListTV.getItems();
            missionsTV.clear();
            missionsTV.addAll(missions);
            updateMissionLabels(missions);
        });
    }

    private void updateMissionLabels(List<MissionDTO> missions) {
        Platform.runLater(() -> {
            Integer numOfExecution = 0, numberOfPaused = 0, numOfStopped = 0, numOfFinished = 0, numOfReady = 0, totalCount = 0;
            for (MissionDTO mission : missions) {
                MissionState tempMissionStatus = mission.getStatus();
                if (tempMissionStatus == PAUSED || tempMissionStatus == READY)
                    numOfReady++;
                else if (tempMissionStatus == STOPPED || tempMissionStatus == FINISHED)
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

    public void startListRefresher() {
        dashboardRefresher = new DashboardRefresher(
                autoUpdate, this::updateUsersList, this::updateMissionsList);
        timer = new Timer();
        timer.schedule(dashboardRefresher, REFRESH_RATE, REFRESH_RATE);

    }
    public void close() {
        if (dashboardRefresher != null && timer != null) {
            dashboardRefresher.cancel();
            timer.cancel();
        }
        userListTV.getItems().clear();
        missionListTV.getItems().clear();
    }

    public void UsersListController() {autoUpdate = new SimpleBooleanProperty(true);}
    public BooleanProperty autoUpdatesProperty() {return autoUpdate;}

}


