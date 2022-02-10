package components.home.login;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginController {

    @FXML private Label loginMsgLB;
    @FXML private Button loginBT;
    @FXML private TextField userNameTF;

    private AppController mainController;
    private Stage mainAppStage;
    private String userName;

    @FXML void loginPR(ActionEvent event) throws IOException {
        //check username b
        // if()
        userName = userNameTF.getText();
        System.out.println("LoginController: " + userName );
        mainController.closeLogin(userName);
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
