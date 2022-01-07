package components.task;

import Engine.DTO.TargetDTO;
import components.app.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import java.util.List;


public class taskController {

    @FXML private ChoiceBox<String> taskTypeCB;
    @FXML private BorderPane prefByTaskBPane;

    @FXML private TableView<TargetDTO> targetDataTable;
    @FXML private TableColumn<TargetDTO, CheckBox> checkBoxCOL;
    @FXML private TableColumn<TargetDTO, String> targetNameCOL;
    @FXML private TableColumn<TargetDTO, Integer> depOnCOL;
    @FXML private TableColumn<TargetDTO, Integer> reqForCOL;
    @FXML private TableColumn<TargetDTO, String> serialSetCOL;
    @FXML private TableColumn<TargetDTO, String> targetInfoCOL;
    @FXML private TableColumn<TargetDTO, String> statusCOL;
    @FXML private TableColumn<TargetDTO, String> targetTypeCOL;

    private AppController mainController;
    private ObservableList<TargetDTO> OLTargets;


    @FXML public void initialize() {
        taskTypeCB.getItems().add("Simulation");
        taskTypeCB.getItems().add("Compilation");

        targetNameCOL.setStyle( "-fx-alignment: CENTER;");
        depOnCOL.setStyle( "-fx-alignment: CENTER;");
        reqForCOL.setStyle( "-fx-alignment: CENTER;");
        serialSetCOL.setStyle( "-fx-alignment: CENTER;");
        targetInfoCOL.setStyle( "-fx-alignment: CENTER;");
        statusCOL.setStyle( "-fx-alignment: CENTER;");
        checkBoxCOL.setStyle( "-fx-alignment: CENTER;");
        targetTypeCOL.setStyle( "-fx-alignment: CENTER;");
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setupData(List<TargetDTO> targets) {
        for(TargetDTO target : targets)
            target.setSelectedState(new CheckBox());
        OLTargets = FXCollections.observableArrayList(targets);
        setTable();
    }

    public void setTable() {
        targetNameCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetName"));
        depOnCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum"));
        reqForCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetRequiredForNum"));
        //serialSetCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetDependsOnNum"));
        targetInfoCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetInfo"));
        statusCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetStateString"));
        targetTypeCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetLocationString"));

        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, CheckBox>("selectedState"));

        targetDataTable.setItems(OLTargets);
    }

}
