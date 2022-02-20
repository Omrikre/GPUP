package components.missions;

import Engine.DTO.MissionDTO;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

import static components.app.HttpResourcesPaths.ADD_MISSION;

public class MissionsController {

    @FXML private TableView<?> missionTV;
    @FXML private TableColumn<MissionDTO, Checkbox> checkboxCOL;
    @FXML private TableColumn<MissionDTO, String> missionNameCOL;
    @FXML private TableColumn<MissionDTO, String> missionStatusCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionProgressCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionWorkersCOL;
    @FXML private TableColumn<MissionDTO, Integer> missionPriceCOL;
    @FXML private TableColumn<MissionDTO, String> missionCreatorCOL;
    @FXML private TableColumn<MissionDTO, String> graphNameCOL;
    @FXML private TableColumn<MissionDTO, Integer> targetStatFinishedCOL;
    @FXML private TableColumn<MissionDTO, Integer> targetStatWaitingCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeIndepenCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeLeafCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeMiddleCOL;
    @FXML private TableColumn<MissionDTO, Integer> typeRootCOL;
    @FXML private Button startBT;
    @FXML private Button pauseBT;
    @FXML private Button resumeBT;
    @FXML private Button stopBT;
    @FXML private Button dupScratchBT;
    @FXML private Button dupIncrementalBT;

    @FXML void dupIncrementalPR(ActionEvent event) {}
    @FXML void dupScratchPR(ActionEvent event) {}
    @FXML void pausePR(ActionEvent event) {}
    @FXML void resumePR(ActionEvent event) {}
    @FXML void startPR(ActionEvent event) {}
    @FXML void stopPR(ActionEvent event) {}

    //Engine e, minigraph of chosen targets, get the numbers, send 4 new params - ind, roots, leaves, middles.

//    String finalUrl = HttpUrl
//            .parse(ADD_MISSION)
//            .newBuilder()
//            .addQueryParameter("amount-of-targets", amountOfTargets)
//            .addQueryParameter("compilation-folder", compilationFolder) //for compilation task, else null
//    .addQueryParameter("runtime", runTime)
//    .addQueryParameter("random-runtime",randomRunTime)
//    .addQueryParameter("success", success)
//    .addQueryParameter("success-warnings", successWithWarnings)
//    //the rest are for the display:
//    .addQueryParameter("name",missionName)
//    .addQueryParameter("creator",creatorName)
//    .addQueryParameter("graph-name",graphName)
//            .build()
//            .toString();

//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//        @Override
//        public void onFailure(@NotNull Call call, @NotNull IOException e) {
//            Platform.runLater(() ->
//                    .setText("Something went wrong: " + e.getMessage())
//            );
//        }
//
//        @Override
//        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//            if (response.code() != 200) {
//                String responseBody = response.body().string();
//                Platform.runLater(() ->
//                        .setText("Something went wrong: " + responseBody)
//                );
//            } else {
//                Platform.runLater(() -> {
//
//                    try {
//                        String responseBody = response.body().string();
//                        System.out.println(responseBody);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//    });


}
