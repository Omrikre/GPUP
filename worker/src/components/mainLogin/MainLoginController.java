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
    private boolean loggedIn;

    @FXML public void initialize() {
        userNameNameLB.setText("-");
        loadResponseLB.setText("");
        loggedIn = false;
        loginBT.setDefaultButton(true);
    }



    @FXML void loginPR(ActionEvent event) {
        if(!loggedIn)
            mainController.openLoginWin();
        else {
            loggedIn = false;
            userNameNameLB.setText("");
            mainController.logout();
            loginBT.setDefaultButton(true);
            loginBT.setText("Login");
            //TODO - server logout
        }
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void loggedIn(String userName) {
        loggedIn = true;
        userNameNameLB.setText(userName);
        loginBT.setText("Logout");
        loginBT.setDefaultButton(false);
    }
}
