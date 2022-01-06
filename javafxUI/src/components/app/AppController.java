package components.app;

import Engine.DTO.TargetDTO;
import Engine.Enums.Location;
import Exceptions.FileException;
import components.header.HeaderButtonsController;
import components.info.InfoController;
import components.task.taskController;
import components.xmlLoader.LoadXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import Engine.Engine;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static components.app.CommonResourcesPaths.*;

public class AppController {

    // components
    @FXML private BorderPane maimBorderPaneComp;
    @FXML private GridPane headerComponent;
    @FXML private HeaderButtonsController headerComponentController;
    @FXML private BorderPane FXMLComponent;
    @FXML private LoadXMLController FXMLComponentController;

    private BorderPane infoComponent;
    private InfoController infoComponentController;
    private GridPane taskComponent;
    private taskController taskComponentController;
    private BorderPane settingsComponent;
    private InfoController settingsComponentController;

    // members
    private static final Engine engine = new Engine();
    private String xmlPath;
    public boolean xmlFileIsLoaded;


    // initializers
    @FXML public void initialize() {
        xmlFileIsLoaded = false;
        setMainInSubComponents();
        loadBackComponents();
    }
    public void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // info pane
            fxmlLoader.setLocation(getClass().getResource(INFO_fXML_RESOURCE));
            infoComponent = fxmlLoader.load();
            infoComponentController = fxmlLoader.getController();
            infoComponentController.setMainController(this);
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
            System.out.println(" -- settings done --");

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

    private void setAllDataInPanes() {
        FXMLComponentController.setupData(xmlPath);
        headerComponentController.makeAllButtonsAble();
        infoComponentController.setupData();
        taskComponentController.setupData();
    }

    // change pane by button press
    public void showInfoPane() { maimBorderPaneComp.setCenter(infoComponent); }
    public void showXMLLoadPane() {
        FXMLComponentController.whenGetToPane();
        maimBorderPaneComp.setCenter(FXMLComponent);
    }
    public void showTaskPane() { maimBorderPaneComp.setCenter(taskComponent); }
    public void showSettingsPane() { maimBorderPaneComp.setCenter(settingsComponent); }

    // support methods
    public boolean checkFileIsValid(String path) throws FileException, JAXBException, IOException {
            engine.loadFile(path);
        return true;
    }
    public int getNumOfTargets() { return engine.getAmountOfTargets(); }
    public void setXmlPath(String path) {
        xmlPath = path;
        System.out.println(xmlPath);
        xmlFileIsLoaded = true;
        if (true) { //if xml file is good
            setAllDataInPanes();
        }
    }

    public Map<Location, Integer> getGeneralInfoTable() {
        Map<Location, Integer> numOfTypes = engine.howManyTargetsInEachLocation();
        return numOfTypes;
    }

    public List<TargetDTO> getTargetList() {
       return engine.getListOfAllTargetsDTOsInGraph();
    }

    public TargetDTO getTargetDTO(String targetName) {
        return engine.getTargetDataTransferObjectByName(targetName);
    }
}


