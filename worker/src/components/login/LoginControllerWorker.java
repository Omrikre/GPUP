package components.login;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginControllerWorker {

    @FXML private Spinner<Integer> numThreadsSP;
    @FXML private Label loginMsgLB;
    @FXML private Button loginBT;
    @FXML private TextField userNameTF;

    private AppController mainController;
    private Stage mainAppStage;
    private String userName;


    @FXML public void initialize() {
        loginMsgLB.setText("");
        numThreadsSP.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,5));
    }

    @FXML void loginPR(ActionEvent event) throws IOException {
        //check username b
        // if()
        userName = userNameTF.getText();
        System.out.println("LoginController worker: " + userName );
        mainController.closeLogin(userName, numThreadsSP.getValue());
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


}
