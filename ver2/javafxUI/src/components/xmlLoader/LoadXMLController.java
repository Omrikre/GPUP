package components.xmlLoader;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;

public class LoadXMLController {

    @FXML private Button loadXMLFile;
    @FXML private CheckBox fileIsLoadedCB;
    @FXML private TextField filePathTB;
    @FXML private Label loadResponseLB;
    @FXML private Label loadResponseLB1;
    @FXML private Label loadResponse2LB;


    private AppController mainController;


    @FXML public void initialize() {
        fileIsLoadedCB.setDisable(true);
        fileIsLoadedCB.setIndeterminate(true);
        filePathTB.setText("-- file isn't loaded --");
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void loadXMLAction(ActionEvent event) {
        whenGetToPane();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
        Stage chooser = new Stage();
        chooser.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showOpenDialog(chooser);
        try {
            if (file == null || !mainController.checkFileIsValid(file.getAbsolutePath())) {
                loadResponseLB.setText("Please select file");
                if(mainController.xmlFileIsLoaded)
                    loadResponse2LB.setText("The last valid XML file is still loaded ");
                return;
            }
        } catch (Exception e) {
            loadResponseLB.setText("Unable to load the file: ");
            loadResponseLB1.setText(e.getMessage());
            if(mainController.xmlFileIsLoaded)
                loadResponse2LB.setText("The last valid XML file is still loaded ");
            return;
        }
        loadResponseLB.setText("The XML file has been loaded");
        mainController.setXmlPath(file.getAbsolutePath());
    }

    public void setupData(String path) {
        filePathTB.setText(path);
        fileIsLoadedCB.setIndeterminate(false);
        fileIsLoadedCB.setSelected(true);
    }

    public void whenGetToPane() {
        loadResponseLB.setText("");
        loadResponse2LB.setText("");
        loadResponseLB1.setText("");
    }
}
