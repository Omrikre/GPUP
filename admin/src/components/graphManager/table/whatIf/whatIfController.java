package components.graphManager.table.whatIf;

import Engine.Enums.Bond;
import components.graphManager.table.tableController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

import static components.app.HttpResourcesPaths.GRAPH_LIST;
import static components.app.HttpResourcesPaths.GSON;

public class whatIfController {

    @FXML private VBox whatIfVBPane;
    // what if tab
    @FXML private Label WHATselectedTargetLabel;
    @FXML private Button WHATclearTextBt;
    @FXML private Button WHATgetWhatBt;
    @FXML private TextArea WHATdepTextBox;
    @FXML private TextArea WHATreqTextBox;

    private tableController parentController;
    private String selectedTarget;
    private String reqUrl;
    private String depUrl;
    private String graphName;


    @FXML
    void whatIfGetWhatIfPr(ActionEvent event) {
        if(selectedTarget == "")
            return;
        createHttpCall();
        sendCallToServer();

    }

    private void createHttpCall() {
        this.graphName = parentController.getGraphName();
        reqUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .addQueryParameter("target-a", selectedTarget)
                .addQueryParameter("graphname", graphName)
                .addQueryParameter("bond", "req") //"dep" or "req"
                .build()
                .toString();

        depUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .addQueryParameter("target-a", selectedTarget)
                .addQueryParameter("graphname", graphName)
                .addQueryParameter("bond", "dep") //"dep" or "req"
                .build()
                .toString();
    }
    private void sendCallToServer() {
        HttpClientUtil.runAsync(reqUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    System.out.println(responseBody); //TODO - Delete
                    Platform.runLater(() -> {
                            }
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            Set<String> res = GSON.fromJson(response.body().string(), Set.class);
                            printDataFromServer(res, Bond.REQUIRED_FOR);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                            }
                    );
                }
            }
        });

        HttpClientUtil.runAsync(depUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    System.out.println(responseBody); //TODO - Delete
                    Platform.runLater(() -> {
                            }
                    );
                } else {
                    Platform.runLater(() -> {
                                try {
                                    Set<String> res = GSON.fromJson(response.body().string(), Set.class);
                                    printDataFromServer(res, Bond.DEPENDS_ON);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                    );
                }
            }
        });
    }
    private void printDataFromServer(Set<String> res, Bond bond) {
        if (bond == Bond.DEPENDS_ON) {
            if (res.isEmpty())
                WHATdepTextBox.setText(" -- No Dependency --");
            else
                WHATdepTextBox.setText(res.toString());
        }
        else if (bond == Bond.REQUIRED_FOR) {
            if (res.isEmpty())
                WHATreqTextBox.setText(" -- No Requirements --");
            else
                WHATreqTextBox.setText(res.toString());
        }
    }

    public void clearSelectedTargetLabel() {
        WHATselectedTargetLabel.setText(" -");
        selectedTarget = null;
    }

    public void setSelectedTargetLabel(String targetName) {
        WHATselectedTargetLabel.setText(targetName);
        selectedTarget = targetName;
    }


    public void setParentController(tableController parent) { parentController = parent; }

    // what if tab
    public void whatIfSetup() {}
    public void setWhatBtDisable(boolean disable) { WHATgetWhatBt.setDisable(disable);}
    private void whatIfClearTextBoxes() {

    }
    @FXML void whatIfClearTextPr(ActionEvent event) {
        WHATdepTextBox.clear();
        WHATreqTextBox.clear();
    }

}

