package components.task;

import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import Engine.Enums.State;
import Exceptions.FileException;
import components.app.AppController;
import components.task.compilation.compilationController;
import components.task.simulation.simulationController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    private int taskProgress;
    boolean pause;
    boolean simTab;
    boolean firstCallForResult;



    // initializers
    @FXML public void initialize() {
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        targetsMap = new HashMap<String, TargetDTO>();
        taskProgress = 0;
        pause= false;

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
        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> { compilationComponentController.setSelectedNum(numCheckBoxesSelected); });
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
                compilationComponentController.addSelectedTargetsTB(targetName);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                simulationComponentController.removeSelectedTargetsTB(targetName);
                compilationComponentController.removeSelectedTargetsTB(targetName);
            }
        });
    }
    public void setChoiceBoxListener() {
        taskTypeCB.setOnAction((event) -> {
            setPaneInSettings(taskTypeCB.getSelectionModel().getSelectedItem());
            mainController.setAllTargetsFrozen();
        });
    }

    private void cleanUpData() {
        taskProgress = 0;
        resetTargetsStatus();
        unselectAllCB();
        compilationComponentController.cleanup();
        simulationComponentController.cleanup();
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
            simTab = true;
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
        mainController.resetProgress();
        taskProgress = 0;
        mainController.runSimulation(runTime, randomRunTime, success, successWithWarnings, threadsNum, runTargetsArray, fromScratch);
        pause = false;
        firstCallForResult = true;
        startRandomGenerator();
    }

    public void selectAllCB() { targetsList.forEach(dto -> dto.getSelectedState().setSelected(true)); }
    public void unselectAllCB() { targetsList.forEach(dto -> dto.getSelectedState().setSelected(false)); }

    public void setPause() {
        mainController.setPause();
        pause = true;
    }
    public void setDisableTaskType(boolean bool) { upGP.setDisable(bool); }

    public void whenFinishedSimulation() {
        if(simTab) {
            simulationComponentController.openResult();
        }
        else {
            compilationComponentController.openResult();
        }
    }
    public Map<State, Set<String>> getSimResData() { return mainController.getSimulationResult(); }
    public Set<String> getWhatIf(String TargetName, Bond bond) { return mainController.getWhatIf(TargetName, bond); }
    public void setAllCBDisable(boolean bool) {targetsList.forEach(dto -> dto.getSelectedState().setDisable(bool));}
    public void setWhatIfSelections(Set<String> targetSet) {targetSet.forEach(tarName -> targetsMap.get(tarName).getSelectedState().setSelected(true));}


    public int getSelectedNum() { return numCheckBoxesSelected.intValue();}

    private void startRandomGenerator() {
        Thread thread = new Thread(this::updateProgress);
        thread.setDaemon(true);
        thread.setName("progress bar thread");
        thread.start();
    }
    private void updateProgress() {
        while (taskProgress != 100 && !pause) {
            sleepForSomeTime();
            Platform.runLater(
                    () -> setupProgressByThread()
            );
        }

    }
    public void setupProgressByThread() {
        taskProgress = mainController.getProgress();
        updateTableStatus();
        compilationComponentController.setProgress(taskProgress);
        simulationComponentController.setProgress(taskProgress);
        if(taskProgress == 100) {
            firstCallForResult = false;
            whenFinishedSimulation();
            pause = false;
        }
    }

    private void updateTableStatus() {
        for(TargetDTO dto : targetDataTable.getItems() ) {
            State state = mainController.getStateByTargetName(dto.getTargetName());
            dto.setTargetState(state);
            dto.setTargetStateString(state.toString());
        }
        targetDataTable.refresh();
    }

    private void sleepForSomeTime() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}
    }

    public void setResume(int threadNum) {
        pause = false;
        mainController.setResume(threadNum);
        startRandomGenerator();
    }

    public void runCompilation(Integer threads, ArrayList<String> runTargetsArray,
                               boolean fromScratch, String inputPath, String outputPath) {
        mainController.resetProgress();
        taskProgress = 0;
        mainController.runCompilation(threads, runTargetsArray, fromScratch,
                inputPath, outputPath);
        pause = false;
        firstCallForResult = true;
        startRandomGenerator();
    }
    public void resetTargetsStatus() {
        for(TargetDTO dto : targetDataTable.getItems() ) {
            dto.setTargetState(State.FROZEN);
            dto.setTargetStateString(State.FROZEN.toString());
        }
        targetDataTable.refresh();
    }

    //TODO
    /*
    private void rowClickData() {
        mainController.getInRunTargetInfo()
        targetDataTable.setRowFactory(rowBig ->
        {
            TableRow<TargetDTO> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if(event.getClickCount() == 2 && (!row.isEmpty())) {
                    List<String> lst = new ArrayList<>();
                    synchronized (Lock) {
                        if(targetDataTable.getSelectionModel().getSelectedItem() == null)
                            return;
                        String nameTarget = targetDataTable.getSelectionModel().getSelectedItem().getTargetName();
                        TargetDTO t = mainController.getTargetDTO(nameTarget);
                        String name = t.getTargetName();
                        String type = t.getTargetLocationString();

                        Map<String, Set<String>> serialSets = mainController.getSerialSetByName(name);
                        String SetNames;
                        if (serialSets.size() == 0)
                            SetNames = "doesn't belong to any serial set";
                        else
                            SetNames = serialSets.toString() + "\n";

                        String process = "";
                        //if (t.getIsRunning())
                          //  process = t.();

                        new targetInfoMain (name, type, serialSets, process, t.getSet DependsOn (), t.getSetRequiredFor ());
                    }
                }
            });
            return row;
        });
    }

     */


}
