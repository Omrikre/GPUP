package components.graphManager.table.cycle;

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

public class cycleController {

    @FXML private VBox cycleVBPane;
    // find cycle tab
    @FXML private Label CYCLEselectedTargetLabel;
    @FXML private Button CYCLEclearTextBt;
    @FXML private Button CYCLEgetCycleBt;
    @FXML private Label CYCLEifInCycleLabel;
    @FXML private TextArea CYCLETextBox;

    private tableController parentController;
    private String selectedTarget;

    public void clearSelectedTargetLabel() {
        CYCLEselectedTargetLabel.setText(" -");
        selectedTarget = null;
    }

    public void setSelectedTargetLabel(String targetName) {
        CYCLEselectedTargetLabel.setText(targetName);
        selectedTarget = targetName;
    }


    public void setParentController(tableController parent) { parentController = parent; }

    // cycle tab
    public void cycleSetup() {}
    private void CycleClearTextBoxes() {
        System.out.println("1");
    }
    @FXML void cycleClearTextPr(ActionEvent event) {
        CYCLETextBox.clear();
    }
    @FXML void cycleGetCyclePr(ActionEvent event) {
        CYCLETextBox.setDisable(false);
        CYCLETextBox.clear();
        String finalUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .addQueryParameter("graphname",parentController.getGraphName())
                .addQueryParameter("cycle", "true")
                .addQueryParameter("target-a",selectedTarget)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            Set<String> res = GSON.fromJson(responseBody, Set.class);
                            if(res.isEmpty())
                                CYCLETextBox.setText(" -- No Cycle -- ");
                            else
                                CYCLETextBox.setText(res.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }


    public void setCycleDisable(boolean disable) { CYCLEgetCycleBt.setDisable(disable); }






}
