package components.task;

import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import Engine.Enums.State;
import Exceptions.FileException;
import components.app.AppController;
import components.task.compilation.compilationController;
import components.task.simulation.simulationController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.*;

import static components.app.CommonResourcesPaths.*;


public class taskController {

    // task settings
    @FXML private ChoiceBox<String> taskTypeCB;
    @FXML private BorderPane prefByTaskBPane;
    @FXML private GridPane upGP;
    // simulation
    private BorderPane simulationComponent;
    private simulationController simulationComponentController;
    // compilation
    private BorderPane compilationComponent;
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

    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;
    private IntegerBinding numCheckBoxesSelected;

    private List<TargetDTO> targetsList;
    private Map<String, TargetDTO> targetsMap;



    // initializers
    @FXML public void initialize() {
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        targetsMap = new HashMap<String, TargetDTO>();

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
    public void setMainController(AppController mainController) {this.mainController = mainController;}

    // data setup
    public void setupData(List<TargetDTO> targets) {
        CheckBox tempCB;
        this.targetsList = targets;

        for(TargetDTO target : targets) {
            tempCB = new CheckBox();
            target.setSelectedState(tempCB);
            configureCheckBox(tempCB, target.getTargetName());
            this.targetsMap.put(target.getTargetName(), target);
        }
        OLTargets = FXCollections.observableArrayList(targets);
        setTable();
        loadBackComponents();
        setChoiceBoxListener();
        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> { simulationComponentController.setSelectedNum(numCheckBoxesSelected); });

    }

    private void configureCheckBox(CheckBox checkBox, String targetName) {
        if (checkBox.isSelected())
            selectedCheckBoxes.add(checkBox);
        else
            unselectedCheckBoxes.add(checkBox);

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
                simulationComponentController.addSelectedTargetsTB(targetName);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                simulationComponentController.removeSelectedTargetsTB(targetName);
            }
        });
    }
    public void setChoiceBoxListener() {
        taskTypeCB.setOnAction((event) -> {
            setPaneInSettings(taskTypeCB.getSelectionModel().getSelectedItem());
            cleanUpData();
            mainController.setAllTargetsFrozen();
        });
    }

    private void cleanUpData() {
        unselectAllCB();
        //TODO - reset all target's status
    }

    public void setTable() {
        targetNameCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetName"));
        depOnCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum"));
        reqForCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetRequiredForNum"));
        serialSetCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("serialSetsBelongs"));
        targetInfoCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetInfo"));
        statusCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetStateString"));
        targetTypeCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetLocationString"));

        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, CheckBox>("selectedState"));

        targetDataTable.setItems(OLTargets);
    }

    public void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // compilation pane
            fxmlLoader.setLocation(getClass().getResource(TASK_COMPILATION_fXML_RESOURCE));
            System.out.println("1");
            compilationComponent = fxmlLoader.load();
            System.out.println("1");
            compilationComponentController = fxmlLoader.getController();
            compilationComponentController.setParentController(this);
            System.out.println(" -- task (compilation) done --");

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
        cleanUpData();
        if(Objects.equals(selectedItem, "Simulation")) {
            simulationComponentController.setupData();
            prefByTaskBPane.setCenter(simulationComponent);
        }
        else {
            compilationComponentController.setupData();
            prefByTaskBPane.setCenter(compilationComponent);
        }
    }


    public Integer getMaxThreads() { return mainController.getMaxThreads(); }
    public void runSimulation(
            int runTime, boolean randomRunTime, int success, int successWithWarnings,
            int threadsNum, ArrayList<String> runTargetsArray, boolean fromScratch) throws FileException, InterruptedException {
        mainController.runSimulation(runTime, randomRunTime, success, successWithWarnings, threadsNum, runTargetsArray, fromScratch);

    }

    public void selectAllCB() { targetsList.forEach(dto -> dto.getSelectedState().setSelected(true)); }
    public void unselectAllCB() { targetsList.forEach(dto -> dto.getSelectedState().setSelected(false)); }

    public void setPause() { mainController.setPause(); }
    public void setDisableTaskType(boolean bool) { upGP.setDisable(bool); }

    public void whenFinishedSimulation() {} //TODO
    public Map<State, Set<String>> getSimResData() { return mainController.getSimulationResult(); }
    public Set<String> getWhatIf(String TargetName, Bond bond) { return mainController.getWhatIf(TargetName, bond); }
    public void setAllCBDisable(boolean bool) {targetsList.forEach(dto -> dto.getSelectedState().setDisable(bool));}
    public void setWhatIfSelections(Set<String> targetSet) {targetSet.forEach(tarName -> targetsMap.get(tarName).getSelectedState().setSelected(true));}



}
