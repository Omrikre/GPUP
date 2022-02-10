package components.app;

import Engine.DTO.TargetDTO;
import Engine.Engine;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Exceptions.FileException;
import components.header.HeaderButtonsController;
import components.graphManager.info.InfoController;
import components.graphManager.info.cycleWarningInfo.cycleWarningInfoController;
import components.settings.settingsController;
import components.graphManager.table.tableController;
import components.graphManager.task.taskController;
import components.graphManager.xmlLoader.LoadXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static components.app.CommonResourcesPaths.*;

public class AppController {

    // components:
    @FXML private BorderPane maimBorderPaneComp;
    // Header
    @FXML private VBox headerComponent;
    @FXML private HeaderButtonsController headerComponentController;
    // XML Loader
    @FXML private ScrollPane FXMLComponent;
    @FXML private LoadXMLController FXMLComponentController;
    // Info
    private BorderPane infoComponent;
    private InfoController infoComponentController;
    private BorderPane cycleMsgComponent;
    private cycleWarningInfoController cycleMsgComponentController;
    private Stage cycleMsgWin;
    // Task
    private BorderPane taskComponent;
    private taskController taskComponentController;
    // Settings
    private BorderPane settingsComponent;
    private settingsController settingsComponentController;
    private Stage settingsWin;
    // Table
    private GridPane tableComponent;
    private tableController tableComponentController;

    // members
    public static final Engine engine = new Engine();
    private String xmlPath;
    public boolean xmlFileIsLoaded;
    private boolean cycleMsgShownAlready = false;
    private boolean graphContainsCycle;
    private boolean darkModeOn = false;   //TODO dark mode
    private boolean animationsOn = false; //TODO animations


    // methods:
    // initializers
    @FXML public void initialize() {
        xmlFileIsLoaded = false;
        setMainInSubComponents();
        loadBackComponents();
    }
    private void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // info pane
            fxmlLoader.setLocation(getClass().getResource(INFO_fXML_RESOURCE));
            infoComponent = fxmlLoader.load();
            infoComponentController = fxmlLoader.getController();
            infoComponentController.setMainController(this);
            // info - cycle warning
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(INFO_CYCLE_MSG_fXML_RESOURCE));
            cycleMsgComponent = fxmlLoader.load();
            cycleMsgComponentController = fxmlLoader.getController();
            cycleMsgComponentController.setMainController(this);
            cycleMsgWin = new Stage();
            cycleMsgWin.setTitle("Graph Contains Cycle");
            cycleMsgWin.getIcons().add(new Image("/images/appIcon.png"));
            cycleMsgWin.setScene(new Scene(cycleMsgComponent));
            cycleMsgWin.initModality(Modality.APPLICATION_MODAL);
            cycleMsgWin.setResizable(false);

            System.out.println(" -- info done --");

            // task pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TASK_fXML_RESOURCE));
            taskComponent = fxmlLoader.load();
            taskComponentController = fxmlLoader.getController();
            taskComponentController.setMainController(this);
            System.out.println(" -- task done --");

            // settings pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(SETTINGS_fXML_RESOURCE));
            settingsComponent = fxmlLoader.load();
            settingsComponentController = fxmlLoader.getController();
            settingsComponentController.setMainController(this);
            settingsWin = new Stage();
            settingsWin.setTitle("Settings");
            settingsWin.getIcons().add(new Image("/images/appIcon.png"));
            settingsWin.setScene(new Scene(settingsComponent));
            settingsWin.initModality(Modality.APPLICATION_MODAL);
            settingsWin.setResizable(false);
            System.out.println(" -- settings done --");

            // table pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TABLE_fXML_RESOURCE));
            tableComponent = fxmlLoader.load();
            tableComponentController = fxmlLoader.getController();
            tableComponentController.setMainController(this);
            System.out.println(" -- table done --");

        } catch (Exception e) {
            System.out.println("BIG Problem");
            System.out.println(e.getMessage());
        }
    }
    private void setMainInSubComponents() {
        if (headerComponentController != null && FXMLComponentController != null) {
            FXMLComponentController.setMainController(this);
            headerComponentController.setMainController(this);
        }
    }



    // support methods
    private void setAllDataInPanes() {
        setGraphContainsCycle();
        //FXMLComponentController.setupData(xmlPath);
        headerComponentController.makeAllButtonsAble();
        infoComponentController.setupData();
        taskComponentController.setupData(engine.getListOfAllTargetsDTOsInGraph());
        tableComponentController.setupData(engine.getListOfAllTargetsDTOsInGraph());
    }



    // change pane by button press
    public void showInfoPane() {
        infoComponentController.setupData();
        maimBorderPaneComp.setCenter(infoComponent);
        if(!cycleMsgShownAlready && graphContainsCycle) {
            cycleMsgWin.show();
            cycleMsgShownAlready = true;
        }
    }
    public void showXMLLoadPane() {
       // FXMLComponentController.whenGetToPane();
        maimBorderPaneComp.setCenter(FXMLComponent);
    }
    public void showTaskPane() { maimBorderPaneComp.setCenter(taskComponent); }
    public void showSettingsPane() {
        settingsComponentController.setupData(darkModeOn, animationsOn);
        settingsWin.show();
    }
    public void showTablePane() { maimBorderPaneComp.setCenter(tableComponent); }
    public void closeSettingsWin() { settingsWin.close(); }
    public void closeCycleWarning() { cycleMsgWin.close(); }




    // getters setters
    public boolean isDarkModeOn() { return darkModeOn; }
    public boolean isAnimationsOn() { return animationsOn; }
    public void setAnimationsOn(boolean animationsOn) { this.animationsOn = animationsOn; }
    public void setDarkModeOn(boolean darkModeOn) { this.darkModeOn = darkModeOn; }
    public void setXmlPath(String path) {
        xmlPath = path;
        System.out.println(xmlPath);
        xmlFileIsLoaded = true;
        initialize();
        setAllDataInPanes();
    }
    public int getNumOfTargets() { return engine.getAmountOfTargets(); }
    public boolean getGraphContainsCycle() { return graphContainsCycle; }
    private void setGraphContainsCycle() { graphContainsCycle = engine.checkIfTheGraphContainsCycle(); }



    // engine methods
    public List<TargetDTO> getTargetList() {
       return engine.getListOfAllTargetsDTOsInGraph();
    }
    public TargetDTO getTargetDTO(String targetName) { return engine.getTargetDataTransferObjectByName(targetName); }
    public Set<List<String>> getPathDepends(String a, String b) { return engine.getPathBetweenTargets(a,b, Bond.DEPENDS_ON); }
    public Set<List<String>> getPathRequired(String a, String b) { return engine.getPathBetweenTargets(a,b, Bond.REQUIRED_FOR); }
    public Map<Location, Integer> getGeneralInfoTable() { return engine.howManyTargetsInEachLocation(); }
    public boolean checkFileIsValid(String path) throws FileException, JAXBException, IOException {
        engine.loadFile(path);
        return true;
    }
    public int getMaxThreads() { return engine.getMaxThreads(); }
    public void runSimulation(int runTime, boolean randomRunTime, int success, int successWithWarnings,
                              int threadsNum, ArrayList<String> runTargetsArray, boolean fromScratch) throws FileException, InterruptedException {
        engine.runSimulation(runTargetsArray, runTime, randomRunTime, success, successWithWarnings, threadsNum, fromScratch);
    }
    public String getFileName() { return engine.getFileName(); }
    public int getNumOfSets() { return engine.getSerialSets().size(); }
    public Map<String, Set<String>> getSerialSets() { return engine.getSerialSets();}
    public Map<String, Set<String>> getSerialSetByName(String name) { return engine.getSerialSetsByTargetName(name);}
    public Set<List<String>> getIfInCycle(String selectedTarget) { return engine.isTargetInCircleByName(selectedTarget); }
    public Set<String> getWhatIf(String selectedTarget, Bond bond) { return engine.getSetOfAllAffectedTargetsByBond(selectedTarget, bond); }
    public void setPause() { engine.pause(); }
    public  Map<State, Set<String>> getSimulationResult() { return engine.getTargetsInEachState(); }
    public void setAllTargetsFrozen() { engine.setAllTargetsFrozen(); }
    public int getProgress() { return engine.getProgress(); }
    public void resetProgress() {
    //    engine.resetProgress();

    }

    public State getStateByTargetName(String targetName) {return engine.getStateByTargetName(targetName);}

    public void setResume(int threadNum) { engine.resume(threadNum); }
    public String getInRunTargetInfo(String targetName) { return engine.getTargetInfo(targetName); }

    public void runCompilation(Integer threads, ArrayList<String> targets, boolean fromScratch,
                               String inputPath, String outputPath) {
        engine.runCompilation(targets,inputPath,outputPath,threads,fromScratch);
    }

    public Engine getEngine() { return engine; }
}


