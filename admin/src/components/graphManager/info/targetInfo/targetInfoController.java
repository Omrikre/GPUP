package components.graphManager.info.targetInfo;

import Engine.DTO.TargetDTOWithoutCB;
import components.graphManager.info.InfoController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.List;

public class targetInfoController {

    @FXML private ChoiceBox<String> choiceTargetBox;
    @FXML private Label numOfDepShow;
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label infoLabel;
    @FXML private Label dependsNumLabel;
    @FXML private Label dependsNamesLabel;
    @FXML private Label requiredNumLabel;
    @FXML private Label requiredNamesLabel;

    private List<TargetDTOWithoutCB> targetDTOList;
    private InfoController parentController;

    public void setParentController(InfoController parentController) { this.parentController = parentController;    }


    @FXML private void initialize() {
        nameLabel.setText(" - ");
        typeLabel.setText(" - ");
        infoLabel.setText(" - ");
        dependsNumLabel.setText(" - ");
        dependsNamesLabel.setText(" - ");
        requiredNumLabel.setText(" - ");
        requiredNamesLabel.setText(" - ");

    }
    public void resetData() {
        nameLabel.setText(" - ");
        typeLabel.setText(" - ");
        infoLabel.setText(" - ");
        dependsNumLabel.setText(" - ");
        dependsNamesLabel.setText(" - ");
        requiredNumLabel.setText(" - ");
        requiredNamesLabel.setText(" - ");
        if(!choiceTargetBox.getItems().isEmpty())
            choiceTargetBox.getItems().clear();
    }

    public void setChoiceTargetBox(List<TargetDTOWithoutCB> targetDTOS) {
        targetDTOList = targetDTOS;
        if(!choiceTargetBox.getItems().isEmpty())
            choiceTargetBox.getItems().clear();
        for(TargetDTOWithoutCB target :  targetDTOS) {
            choiceTargetBox.getItems().add(target.getTargetName());
        }
        choiceTargetBox.setOnAction((event) -> {
            String selectedItem = choiceTargetBox.getSelectionModel().getSelectedItem();
            //TEST System.out.println("Selection target: " + selectedItem);
            for (TargetDTOWithoutCB t : targetDTOS) {
                if(t.getTargetName().equals(selectedItem))
                    setDataInLabels((t));
            }

        });
    }


    public void setDataInLabels(TargetDTOWithoutCB targetInfo) {
        nameLabel.setText(targetInfo.getTargetName());
        typeLabel.setText(targetInfo.getTargetLocation().toString());
        infoLabel.setText(targetInfo.getTargetInfo());
        dependsNumLabel.setText(String.valueOf(targetInfo.getTargetDependsOn().size()));
        dependsNamesLabel.setText(targetInfo.getTargetDependsOn().toString());
        requiredNumLabel.setText(String.valueOf(targetInfo.getTargetRequiredFor().size()));
        requiredNamesLabel.setText(targetInfo.getTargetRequiredFor().toString());
    }
}
