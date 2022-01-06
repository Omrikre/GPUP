package components.info.targetInfo;

import Engine.DTO.TargetDTO;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;

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

    private List<TargetDTO> targetDTOList;

    @FXML private void initialize() {
        nameLabel.setText(" - ");
        typeLabel.setText(" - ");
        infoLabel.setText(" - ");
        dependsNumLabel.setText(" - ");
        dependsNamesLabel.setText(" - ");
        requiredNumLabel.setText(" - ");
        requiredNamesLabel.setText(" - ");
    }

    public void setChoiceTargetBox(List<TargetDTO> targetDTOS) {
        targetDTOList = targetDTOS;
        for(TargetDTO target :  targetDTOS) {
            choiceTargetBox.getItems().add(target.getTargetName());
        }
    }

    @FXML void choiceTargetBoxGetChoice(ContextMenuEvent event) {
        event.toString();
    }


    public void setDataInLabels(TargetDTO targetInfo) {
        nameLabel.setText(targetInfo.getTargetName());
        typeLabel.setText(targetInfo.getTargetLocation().toString());
        infoLabel.setText(targetInfo.getTargetInfo());
        dependsNumLabel.setText(String.valueOf(targetInfo.getTargetDependsOn().size()));
        dependsNamesLabel.setText(targetInfo.getTargetDependsOn().toString());
        requiredNumLabel.setText(String.valueOf(targetInfo.getTargetRequiredFor().size()));
        requiredNamesLabel.setText(targetInfo.getTargetRequiredFor().toString());

    }
}
