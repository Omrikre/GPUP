package components.graphManager;

import components.app.AppController;
import components.graphManager.graphHeader.GraphHeaderController;
import components.graphManager.info.InfoController;
import components.graphManager.info.cycleWarningInfo.cycleWarningInfoController;
import components.graphManager.missionCreator.taskController;
import components.graphManager.table.tableController;
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

import static components.app.CommonResourcesPathsWorker.*;



public class GraphController {

    private AppController mainController;

    // components:
    @FXML private BorderPane graphBP;
    // Header
    @FXML private VBox headerComp;
    @FXML private GraphHeaderController headerCompController;
    // XML Loader
    @FXML private ScrollPane XMLComp;
    @FXML private LoadXMLController XMLCompController;

    // graph info
    private BorderPane infoComponent;
    private InfoController infoComponentController;
    private BorderPane cycleMsgComponent;
    private cycleWarningInfoController cycleMsgComponentController;
    private Stage cycleMsgWin;

    private boolean cycleMsgShownAlready = false;
    private boolean graphContainsCycle;

    // targets info
    private GridPane tableComponent;
    private tableController tableComponentController;

    // mission create info
    private BorderPane missionCreateComponent;
    private taskController missionCreateComponentController;






    @FXML public void initialize() {
        setMainInSubComponents();
        loadBackComponents();
    }

    private void setMainInSubComponents() {
        if (headerCompController != null && XMLCompController != null) {
            headerCompController.setGraphParentController(this);
            XMLCompController.setGraphParentController(this);
        }
    }

    private void loadBackComponents() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            // info pane
            fxmlLoader.setLocation(getClass().getResource(INFO_XML_RESOURCE));
            infoComponent = fxmlLoader.load();
            infoComponentController = fxmlLoader.getController();
            infoComponentController.setParentController(this);
            // info - cycle warning
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(INFO_CYCLE_MSG_fXML_RESOURCE));
            cycleMsgComponent = fxmlLoader.load();
            cycleMsgComponentController = fxmlLoader.getController();
            cycleMsgComponentController.setParentController(this);
            cycleMsgWin = new Stage();
            cycleMsgWin.setTitle("Graph Contains Cycle");
            cycleMsgWin.getIcons().add(new Image("/images/appIcon.png"));
            cycleMsgWin.setScene(new Scene(cycleMsgComponent));
            cycleMsgWin.initModality(Modality.APPLICATION_MODAL);
            cycleMsgWin.setResizable(false);
            System.out.println(" -- (graph manager) info done --");

            // targets info - table pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TABLE_fXML_RESOURCE));
            tableComponent = fxmlLoader.load();
            tableComponentController = fxmlLoader.getController();
            tableComponentController.setParentController(this);
            System.out.println(" -- (graph manager) table done --");

            // targets info - table pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(MISSION_CREATOR_fXML_RESOURCE));
            missionCreateComponent = fxmlLoader.load();
            missionCreateComponentController = fxmlLoader.getController();
            missionCreateComponentController.setParentController(this);
            System.out.println(" -- (graph manager) mission creator done --");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void showXMLManagerPane() { graphBP.setCenter(XMLComp); }
    public void showGraphInfoPane() {
        //infoComponentController.setupData();
        graphBP.setCenter(infoComponent);
        if(!cycleMsgShownAlready && graphContainsCycle) {
            cycleMsgWin.show();
            cycleMsgShownAlready = true;
        }
    }
    public void showTargetsInfoPane() { graphBP.setCenter(tableComponent); }
    public void showMissionCreatorPane() { graphBP.setCenter(missionCreateComponent); }

    public void closeCycleWarning() { cycleMsgWin.close(); }

}
