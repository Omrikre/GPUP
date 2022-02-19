package components.dashboard;

import Engine.DTO.MissionDTO;
import Engine.users.UserManager;
import components.app.AppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static components.app.CommonResourcesPaths.REFRESH_RATE;
import static components.app.HttpResourcesPaths.*;

public class DashboardController implements Closeable {

    @FXML private TableView<UserManager.User> userListTV;
    @FXML private TableColumn<String, UserManager.User> userNameCOL;
    @FXML private TableColumn<String, UserManager.User> userTypeCOL;
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
    private TimerTask timeRefresher;

    public void setMainController(AppController appController) {
        this.mainController = appController;
    }

    @FXML public void initialize() {
        userNameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
        userTypeCOL.setCellValueFactory(new PropertyValueFactory<>("role"));

        missionNameCOL.setCellValueFactory(new PropertyValueFactory<>("missionName"));
        missionStatusCOL.setCellValueFactory(new PropertyValueFactory<>("status"));

        //userListTV.setItems(newItems); //TODO
    }

    private void updateUsersList(List<UserManager.User> usersNames) {
        Platform.runLater(() -> {
            ObservableList<UserManager.User> items = userListTV.getItems();
            items.clear();
            items.addAll(usersNames);
            updateUsersLabels(usersNames);
        });
    }

    private void updateUsersLabels(List<UserManager.User> usersNames) {
        Integer numberOfUsers = 0, numOfAdmins = 0, numOfWorkers = 0;
        for(UserManager.User user : usersNames) {
            if(user.getRole().equals("Admin"))
                numOfAdmins++;
            else
                numOfWorkers++;
            numberOfUsers++;
        }
        totalUsersLB.setText(numberOfUsers.toString());
        adminsLB.setText(numOfAdmins.toString());
        workersLB.setText(numOfWorkers.toString());
    }

    public void startListRefresher() {
        dashboardRefresher = new DashboardRefresher(
                autoUpdate, this::updateUsersList);
        timer = new Timer();
        timer.schedule(timeRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void close() {
        if (timeRefresher != null && timer != null) {
            timeRefresher.cancel();
            timer.cancel();
        }
        userListTV.getItems().clear();
        missionListTV.getItems().clear();
    }

    public void UsersListController() {autoUpdate = new SimpleBooleanProperty(true);}
    public BooleanProperty autoUpdatesProperty() {return autoUpdate;}

}


