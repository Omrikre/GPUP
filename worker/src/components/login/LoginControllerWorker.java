package components.login;

import components.app.AppController;

import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static components.app.HttpResourcesPaths.LOGIN_PAGE;


public class LoginControllerWorker {

    @FXML private Spinner<Integer> numThreadsSP;
    @FXML private Label loginMsgLB;
    @FXML private Button loginBT;
    @FXML private TextField userNameTF;

    private AppController mainController;
    private Stage mainAppStage;
    private String userName;


    @FXML
    public void initialize() {
        userNameTF.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginSetUp();
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1, 1);
        numThreadsSP.setValueFactory(valueFactory);
        numThreadsSP.setEditable(false);
        TextFormatter format = new TextFormatter(numThreadsSP.getValueFactory().getConverter(), numThreadsSP.getValueFactory().getValue());
        numThreadsSP.getEditor().setTextFormatter(format);
        numThreadsSP.getValueFactory().valueProperty().bindBidirectional(format.valueProperty());
        userNameTF.requestFocus();
    }

    public String getUserName() {
        return userName;
    }

    @FXML
    void loginPR(ActionEvent event) throws IOException {
        loginSetUp();
    }

    private void loginSetUp() {
        userName = "";
        loginMsgLB.setText("");
        loginBT.setDisable(true);

        userName = userNameTF.getText();
        if (userName.isEmpty()) {
            loginMsgLB.setText("Please enter user name");
            loginBT.setDisable(false);
            return;
        }
        System.out.println("LoginController: " + userName);

        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("role", "Worker")
                .addQueryParameter("threadSize",numThreadsSP.getValue().toString())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                            loginMsgLB.setText("Something went wrong: server down");
                            loginBT.setDisable(false);
                        }
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                                loginMsgLB.setText("Something went wrong: " + responseBody);
                                loginBT.setDisable(false);
                            }
                    );
                } else {
                    Platform.runLater(() -> {
                                mainController.closeLogin(userName, numThreadsSP.getValue());
                                loginBT.setDisable(false);
                            }
                    );
                }
            }
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void cleanUsernameText() {
        userNameTF.clear();
        userNameTF.requestFocus();
    }

}
