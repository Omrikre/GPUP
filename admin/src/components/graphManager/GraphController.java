package components.graphManager;

import components.app.AppController;
import components.graphManager.graphHeader.GraphHeaderController;
import components.graphManager.xmlLoader.LoadXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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




    @FXML public void initialize() {
        //setMainInSubComponents();
        //loadBackComponents();
    }

    private void setMainInSubComponents() {
        if (headerCompController != null && XMLCompController != null) {
            headerCompController.setGraphParentController(this);
            XMLCompController.setGraphParentController(this);
        }
    }

    private void loadBackComponents() {
        try {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

}
