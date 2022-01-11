package components.settings;

import components.app.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class settingsController {

    private AppController mainController;
    @FXML private CheckBox darkModeCB;
    @FXML private Button cancelBt;
    @FXML private Button saveBt;
    @FXML private CheckBox animationsCB;


    @FXML public void initialize() {
        darkModeCB.setDisable(true);
        animationsCB.setDisable(true);
    }

    @FXML void savePr(ActionEvent event) {
        mainController.setDarkModeOn(darkModeCB.isSelected());
        mainController.setAnimationsOn(animationsCB.isSelected());
        mainController.closeSettingsWin();
    }
    @FXML void cancelPr(ActionEvent event) { mainController.closeSettingsWin(); }


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setupData(boolean darkModeOn, boolean animationsOn) {
        darkModeCB.setSelected(darkModeOn);
        animationsCB.setSelected(animationsOn);
    }
}
