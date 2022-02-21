package components.targets;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TargetsListController {

    @FXML private TableView<?> missionTV;
    @FXML private TableColumn<?, ?> missionNameCOL;
    @FXML private TableColumn<?, ?> taskTypeCOL;
    @FXML private TableColumn<?, ?> targetNameCOL;
    @FXML private TableColumn<?, ?> missionStatusCOL;
    @FXML private TableColumn<?, ?> receivedCoinsCOL;
    @FXML private Label availableThreadsLB;
    @FXML private Label totalThreadsLB;
}


/*
//TODO for the worker to get the list of targets from the task he wants to work on when he starts to work.
String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name","")
                .addQueryParameter("targets","true")
                .build()
                .toString();


 */


/*
MAKE TASK AND RUN IT


 */