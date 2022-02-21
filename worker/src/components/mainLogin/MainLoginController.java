package components.mainLogin;

import components.app.AppController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static components.app.HttpResourcesPaths.LOGOUT_PAGE;


public class MainLoginController {

    @FXML private Button loginBT;
    @FXML private Label userNameLB;
    @FXML private Label userNameNameLB;
    @FXML private Label loadResponseLB;
    @FXML private Label userNameLB1;
    @FXML private Label numThreadsLB;
    @FXML private Label userNameLB11;
    @FXML private Label creditsLB;
    private AppController mainController;
    private boolean loggedIn;






    @FXML public void initialize() {
        userNameNameLB.setText("-");
        numThreadsLB.setText("-");
        creditsLB.setText("-");
        loadResponseLB.setText("");
        loggedIn = false;
        loginBT.setDefaultButton(true);
    }



    @FXML void loginPR(ActionEvent event) {
        if(!loggedIn)
            mainController.openLoginWin();
        else {
            loggedOut();
        }
    }

    public void loggedOut() {
        String finalUrl = HttpUrl
                .parse(LOGOUT_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        userNameNameLB.setText("logout Fail")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            userNameNameLB.setText("logout Fail code: " + response.code())
                    );
                } else {
                    Platform.runLater(() -> {
                        mainController.setInActive();
                        loggedIn = false;
                        userNameNameLB.setText("-");
                        numThreadsLB.setText("-");
                        creditsLB.setText("-");
                        mainController.logout();
                        loginBT.setDefaultButton(true);
                        loginBT.setText("Login");
                    });
                }
            }
        });
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void loggedIn(String userName, String numOfThreads) {
        loggedIn = true;
        userNameNameLB.setText(userName);
        numThreadsLB.setText(numOfThreads);
        creditsLB.setText("0");
        loginBT.setText("Logout");
        loginBT.setDefaultButton(false);
    }
}
