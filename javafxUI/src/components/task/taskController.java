package components.task;

import Engine.DTO.TargetDTO;
import components.app.AppController;
import components.task.compilation.compilationController;
import components.task.simulation.simulationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

import static components.app.CommonResourcesPaths.*;


public class taskController {

    // task settings
    @FXML private ChoiceBox<String> taskTypeCB;
    @FXML private BorderPane prefByTaskBPane;
    // simulation
    private BorderPane simulationComponent;
    private simulationController simulationComponentController;
    // compilation
    private VBox compilationComponent;
    private compilationController compilationComponentController;

    // table
    @FXML private TableView<TargetDTO> targetDataTable;
    @FXML private TableColumn<TargetDTO, CheckBox> checkBoxCOL;
    @FXML private TableColumn<TargetDTO, String> targetNameCOL;
    @FXML private TableColumn<TargetDTO, Integer> depOnCOL;
    @FXML private TableColumn<TargetDTO, Integer> reqForCOL;
    @FXML private TableColumn<TargetDTO, String> serialSetCOL;
    @FXML private TableColumn<TargetDTO, String> targetInfoCOL;
    @FXML private TableColumn<TargetDTO, String> statusCOL;
    @FXML private TableColumn<TargetDTO, String> targetTypeCOL;

    // members
    private AppController mainController;
    private ObservableList<TargetDTO> OLTargets;


    // initializers
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

    // data setup
    public void setupData(List<TargetDTO> targets) {
        for(TargetDTO target : targets)
            target.setSelectedState(new CheckBox());
        OLTargets = FXCollections.observableArrayList(targets);
        setTable();
        loadBackComponents();
        setChoiceBoxListener();
    }
    public void setChoiceBoxListener() {
        taskTypeCB.setOnAction((event) -> {
            System.out.println("Selection target: " + taskTypeCB.getSelectionModel().getSelectedItem());
            setPaneInSettings(taskTypeCB.getSelectionModel().getSelectedItem());
        });
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

    public void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // compilation pane //TODO
            /*
            fxmlLoader.setLocation(getClass().getResource(TASK_COMPILATION_fXML_RESOURCE));
            System.out.println("1");
            compilationComponent = fxmlLoader.load();
            System.out.println("1");
            compilationComponentController = fxmlLoader.getController();
            compilationComponentController.setParentController(this);
            System.out.println(" -- task (compilation) done --");
             */

            // simulation pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TASK_SIMULATION_fXML_RESOURCE));
            simulationComponent = fxmlLoader.load();
            simulationComponentController = fxmlLoader.getController();
            simulationComponentController.setParentController(this);
            System.out.println(" -- task (simulation) done --");

        } catch (Exception e) {
            System.out.println("BIG (task) Problem");
            System.out.println(e.getMessage());
        }
    }


    // set settings pane
    private void setPaneInSettings(String selectedItem) {
        if(selectedItem == "Simulation") {
            simulationComponentController.setupData();
            prefByTaskBPane.setCenter(simulationComponent);
        }
        else
            prefByTaskBPane.setCenter(compilationComponent);
    }


    public Integer getMaxThreads() { return mainController.getMaxThreads(); }
    public void runSimulation(int runTime, boolean randomRunTime, int success, int successWithWarnings, int threadsNum) {
        mainController.runSimulation(runTime, randomRunTime, success, successWithWarnings, threadsNum);

    }
}
