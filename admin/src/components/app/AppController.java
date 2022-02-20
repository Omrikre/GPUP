package components.app;

import Engine.DTO.TargetDTO;
import Engine.Engine;
import Engine.Enums.Bond;
import Engine.Enums.Location;
import Engine.Enums.State;
import Exceptions.FileException;
import components.chat.chatroom.ChatRoomMainController;
import components.dashboard.DashboardController;
import components.graphManager.GraphController;
import components.header.HeaderButtonsController;
import components.home.login.LoginController;
import components.mainLogin.MainLoginController;
import components.missions.MissionsController;
import components.missions.createNewMission.NewMissionCreatorController;
import components.settings.settingsController;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static components.app.CommonResourcesPaths.*;


public class AppController implements Closeable {

    // components:
    @FXML private BorderPane maimBorderPaneComp;
    // Header
    @FXML private VBox headerComponent;
    @FXML private HeaderButtonsController headerComponentController;
    // Settings
    private BorderPane settingsComponent;
    private settingsController settingsComponentController;
    private Stage settingsWin;
    // Login
    private GridPane loginComponent;
    private LoginController loginComponentController;
    private Stage loginWin;

    // main components:
    // main - login Loader
    @FXML private ScrollPane mainLoginComp;
    @FXML private MainLoginController mainLoginCompController;
    // graph manager
    private BorderPane graphManagerComponent;
    private GraphController graphManagerComponentController;
    // dashboard
    private ScrollPane dashboardComponent;
    private DashboardController dashboardComponentController;
    // missions
    private ScrollPane missionsComponent;
    private MissionsController missionsComponentController;
    // create new mission
    private GridPane createMissionComponent;
    private NewMissionCreatorController createMissionComponentController;
    private Stage createMissionWin;

    // missions
    private ScrollPane chatComponent;
    private ChatRoomMainController chatComponentController;


    // members
    public static final Engine engine = new Engine();
    public boolean isLoggedIn;
    private boolean cycleMsgShownAlready = false;
    private boolean graphContainsCycle;
    private boolean darkModeOn = false;   //TODO dark mode
    private boolean animationsOn = false; //TODO animations
    private BooleanProperty autoUpdate;

    // methods:
    // initializers
    @FXML
    public void initialize() {
        isLoggedIn = false;
        autoUpdate = new SimpleBooleanProperty(true);
        setMainInSubComponents();
        loadBackComponents();
    }

    // Refreshers
    public void setRefreshersActive() {
        dashboardComponentController.startListRefresher(autoUpdate);
        chatComponentController.startChatRefresher(autoUpdate);
        graphManagerComponentController.startDataRefresher(autoUpdate);
/*        graphAdminComponentController.startGraphListRefresher();
        starChatRefresher();
        starMissionRefresher();*/
    }

    public void setInActive() {
        try {
            autoUpdate.setValue(false);
            //usersListComponentController.close();
            //graphAdminComponentController.close();
        } catch (Exception ignored) {}
    }

    public String getUsername() {
        return loginComponentController.getUserName();
    }
    private void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
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

            // login
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(LOGIN_RESOURCE));
            loginComponent = fxmlLoader.load();
            loginComponentController = fxmlLoader.getController();
            loginComponentController.setMainController(this);
            loginWin = new Stage();
            loginWin.setTitle("Admin Login");
            loginWin.getIcons().add(new Image("/images/appIcon.png"));
            loginWin.setScene(new Scene(loginComponent));
            loginWin.initModality(Modality.APPLICATION_MODAL);
            loginWin.setResizable(false);
            System.out.println(" -- login done --");

            // create new mission
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(CREATE_NEW_MISSION_fXML_RESOURCE));
            createMissionComponent = fxmlLoader.load();
            createMissionComponentController = fxmlLoader.getController();
            createMissionComponentController.setMainController(this);
            createMissionWin = new Stage();
            createMissionWin.setTitle("Create New Mission");
            createMissionWin.getIcons().add(new Image("/images/appIcon.png"));
            createMissionWin.setScene(new Scene(createMissionComponent));
            createMissionWin.initModality(Modality.APPLICATION_MODAL);
            createMissionWin.setResizable(false);
            System.out.println(" -- create new mission done --");

            // graph manager
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(GRAPH_MANAGER_fXML_RESOURCE));
            graphManagerComponent = fxmlLoader.load();
            graphManagerComponentController = fxmlLoader.getController();
            graphManagerComponentController.setMainController(this);
            System.out.println(" -- graph manager done --");

            // missions
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(MISSIONS_fXML_RESOURCE));
            missionsComponent = fxmlLoader.load();
            missionsComponentController = fxmlLoader.getController();
            missionsComponentController.setMainController(this);
            System.out.println(" -- missions done --");

            // dashboard
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(DASHBOARD_fXML_RESOURCE));
            dashboardComponent = fxmlLoader.load();
            dashboardComponentController = fxmlLoader.getController();
            dashboardComponentController.setMainController(this, missionsComponentController);
            System.out.println(" -- dashboard done --");

            // chat
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(CHAT_fXML_RESOURCE));
            chatComponent = fxmlLoader.load();
            chatComponentController = fxmlLoader.getController();
            //missionsComponentController.setMainController(this);
            System.out.println(" -- chat done --");


        } catch (Exception e) {
            System.out.println("BIG Problem");
            System.out.println(e.getMessage());
        }
    }
    private void setMainInSubComponents() {
        if (headerComponentController != null && mainLoginCompController != null) {
            mainLoginCompController.setMainController(this);
            headerComponentController.setMainController(this);
        }
    }


    // support methods
    public void setAllDataInPanes() {
        setGraphContainsCycle();
        headerComponentController.makeAllButtonsDisable(false);
        //TODO - start All refreshers
        setRefreshersActive();
    }


    // change pane by button press
    public void showHomePane() {
        maimBorderPaneComp.setCenter(mainLoginComp);
    }
    public void showDashboardPane() {maimBorderPaneComp.setCenter(dashboardComponent);}
    public void showMissionsPane() {
        maimBorderPaneComp.setCenter(missionsComponent);
    }
    public void showGraphPane() {
        maimBorderPaneComp.setCenter(graphManagerComponent);
        graphManagerComponentController.showXMLManagerPane();
    }
    public void showChatPane() {
        maimBorderPaneComp.setCenter(chatComponent);
    }
    public void showSettingsPane() {
        settingsComponentController.setupData(darkModeOn, animationsOn);
        settingsWin.show();
    }
    public void openLoginWin() {
        loginWin.show();
        loginComponentController.cleanUsernameText();
    }
    public void closeSettingsWin() {
        settingsWin.close();
    }

    public void openCreateNewMissionWin(boolean isSimulation ,String targetsArr, String amountOfTargets, String src, String compFolder, String graphName,
                                        String runTime, String randomRunTime, String success, String successWithWarnings) {
        createMissionComponentController.cleanup();
        if(isSimulation)
            createMissionComponentController.setupDataSimulation(targetsArr, amountOfTargets, runTime, randomRunTime, success ,successWithWarnings , graphName);
        else
            createMissionComponentController.setupDataCompilation(targetsArr, amountOfTargets, src, compFolder, graphName);
        createMissionWin.show();
    }
    public void openCreateNewMissionWin(boolean isFromScratch, String oldName) {
        createMissionComponentController.cleanup();
        createMissionComponentController.setupDataCompilation(isFromScratch, oldName);
        createMissionWin.show();
    }

    public void closeCreateNewMissionWin() { createMissionWin.close();
    }



    // getters setters
    public boolean isDarkModeOn() {
        return darkModeOn;
    }
    public boolean isAnimationsOn() {
        return animationsOn;
    }
    public void setAnimationsOn(boolean animationsOn) {
        this.animationsOn = animationsOn;
    }
    public void setDarkModeOn(boolean darkModeOn) {
        this.darkModeOn = darkModeOn;
    }
    public int getNumOfTargets() {return engine.getAmountOfTargets();}
    public boolean getGraphContainsCycle() {
        return graphContainsCycle;
    }
    private void setGraphContainsCycle() {
        graphContainsCycle = engine.checkIfTheGraphContainsCycle();
    }

    // engine methods
    public List<TargetDTO> getTargetList() {
        return engine.getListOfAllTargetsDTOsInGraph();
    }
    public TargetDTO getTargetDTO(String targetName) {
        return engine.getTargetDataTransferObjectByName(targetName);
    }
    public Set<List<String>> getPathDepends(String a, String b) {return engine.getPathBetweenTargets(a, b, Bond.DEPENDS_ON);}
    public Set<List<String>> getPathRequired(String a, String b) {return engine.getPathBetweenTargets(a, b, Bond.REQUIRED_FOR);}
    public Map<Location, Integer> getGeneralInfoTable() {
        return engine.howManyTargetsInEachLocation();
    }
    public boolean checkFileIsValid(String path, String username) throws FileException, JAXBException, IOException {
        engine.loadFile(path, username);
        return true;
    }
    public int getMaxThreads() {
        return engine.getMaxThreads();
    }
    public void runSimulation(int runTime, boolean randomRunTime, int success, int successWithWarnings,
                              int threadsNum, ArrayList<String> runTargetsArray, boolean fromScratch) throws FileException, InterruptedException {
        engine.runSimulation(runTargetsArray, runTime, randomRunTime, success, successWithWarnings, threadsNum, fromScratch);
    }
    public String getFileName() {
        return engine.getFileName();
    }

    public int getNumOfSets() {
        return engine.getSerialSets().size();
    }

    public Map<String, Set<String>> getSerialSets() {
        return engine.getSerialSets();
    }

    public Map<String, Set<String>> getSerialSetByName(String name) {
        return engine.getSerialSetsByTargetName(name);
    }

    public Set<List<String>> getIfInCycle(String selectedTarget) {
        return engine.isTargetInCircleByName(selectedTarget);
    }

    public Set<String> getWhatIf(String selectedTarget, Bond bond) {
        return engine.getSetOfAllAffectedTargetsByBond(selectedTarget, bond);
    }

    public void setPause() {
        engine.pause();
    }

    public Map<State, Set<String>> getSimulationResult() {
        return engine.getTargetsInEachState();
    }

    public void setAllTargetsFrozen() {
        engine.setAllTargetsFrozen();
    }

    public int getProgress() {
        return engine.getProgress();
    }

    public void resetProgress() {
        //    engine.resetProgress();

    }

    public State getStateByTargetName(String targetName) {
        return engine.getStateByTargetName(targetName);
    }

    public void setResume(int threadNum) {
        engine.resume(threadNum);
    }

    public String getInRunTargetInfo(String targetName) {
        return engine.getTargetInfo(targetName);
    }

    public void runCompilation(Integer threads, ArrayList<String> targets, boolean fromScratch,
                               String inputPath, String outputPath) {
        engine.runCompilation(targets, inputPath, outputPath, threads, fromScratch);
    }

    public Engine getEngine() {
        return engine;
    }

    public void closeLogin(String userName) {
        mainLoginCompController.loggedIn(userName);
        loginWin.close();
        headerComponentController.makeAllButtonsDisable(false);
        setAllDataInPanes();
        autoUpdate.setValue(true);
    }

    public void logout() {
        headerComponentController.makeAllButtonsDisable(true);
        graphManagerComponentController.clearAfterLogout();
    }


    @Override
    public void close() throws IOException {
        mainLoginCompController.loggedOut();
        HttpClientUtil.shutdown();
        chatComponentController.closeChat();
        dashboardComponentController.closeDashboard();
        graphManagerComponentController.closeGraphManager();
    }

    public BooleanProperty getAutoUpdate() { return autoUpdate; }
}


