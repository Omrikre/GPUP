package components.task;

import components.app.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class taskController {

    @FXML private TableView<String> targetDataTable;
    @FXML private ChoiceBox<String> taskTypeCB;
    @FXML private BorderPane prefByTaskBPane;

    private AppController mainController;

    @FXML public void initialize() {
        taskTypeCB.getItems().add("Simulation");
        taskTypeCB.getItems().add("Compilation");
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setupData() {

    }

}
