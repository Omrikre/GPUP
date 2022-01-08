package components.app;

import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Exceptions.FileException;
import components.header.HeaderButtonsController;
import components.info.InfoController;
import components.info.cycleWarningInfo.cycleWarningInfoController;
import components.table.tableController;
import components.task.taskController;
import components.xmlLoader.LoadXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import Engine.Engine;
import components.settings.settingsController;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.IOException;
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
    @FXML private BorderPane FXMLComponent;
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
    private static final Engine engine = new Engine();
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
        FXMLComponentController.setupData(xmlPath);
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
        FXMLComponentController.whenGetToPane();
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
        if (true) { //if xml file is good
            setAllDataInPanes();
        }
    }
    public int getNumOfTargets() { return engine.getAmountOfTargets(); }
    public boolean getGraphContainsCycle() { return graphContainsCycle; }
    private void setGraphContainsCycle() { graphContainsCycle = false; //TODO engine.checkIfTheGraphContainsCycle();
        }



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


    public int getMaxThreads() {
        //TODO engine.getMaxThreads();
        return 5;
    }

    public void runSimulation(int runTime, boolean randomRunTime, int success, int successWithWarnings, int threadsNum) {
        //TODO - engine.runSimulation();
    }
}

