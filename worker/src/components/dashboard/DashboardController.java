package components.dashboard;

import components.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardController {

    @FXML private TableView<?> userListTV;
    @FXML private TableColumn<?, ?> userNameCOL;
    @FXML private TableColumn<?, ?> userTypeCOL;
    @FXML private Label totalUsersLB;
    @FXML private Label adminsLB;
    @FXML private Label workersLB;
    @FXML private TableView<?> missionListTV;
    @FXML private TableColumn<?, ?> missionNameCOL;
    @FXML private TableColumn<?, ?> missionStatusCOL;
    @FXML private Label totalMissionsLB;
    @FXML private Label executionLB;
    @FXML private Label waitingLB;
    @FXML private Label finishedLB;

    private AppController mainController;

    public void setMainController(AppController appController) { this.mainController = appController;}
}
