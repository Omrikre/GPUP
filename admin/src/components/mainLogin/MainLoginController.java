package components.mainLogin;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainLoginController {

    @FXML private Button loginBT;
    @FXML private Label userNameLB;
    @FXML private Label userNameNameLB;
    @FXML private Label loadResponseLB;
    private AppController mainController;

    @FXML public void initialize() {
        userNameNameLB.setText("-");
        loadResponseLB.setText("");
    }



    @FXML void loginPR(ActionEvent event) { mainController.openLoginWin(); }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void loggedIn(String userName) {
        userNameNameLB.setText(userName);
    }
}
