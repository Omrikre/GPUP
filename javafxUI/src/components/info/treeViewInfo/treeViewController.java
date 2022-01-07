package components.info.treeViewInfo;

import Engine.DTO.TargetDTO;
import Engine.Enums.Location;
import components.info.InfoController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import java.util.List;


public class treeViewController {

    @FXML private TreeView<String> treeViewRtoL;
    @FXML private TreeView<String> treeViewLtoR;
    @FXML private TreeView<String> treeViewInd;

    private InfoController parentController;

    public void setParentController(InfoController parentController) { this.parentController = parentController;    }

    public void setTrees(List<TargetDTO> targetList) {
        treeViewRtoL.setDisable(false);
        treeViewLtoR.setDisable(false);
        treeViewInd.setDisable(false);
        TreeItem<String> tempItm;
        TreeItem<String> root;

        root = new TreeItem<String>("Roots");
        for(TargetDTO target : targetList) {
            if(target.isRoot()) {
                tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                setSmallTree(tempItm, target, Location.ROOT);
                root.getChildren().add(tempItm);
            }
        }
        treeViewRtoL.setRoot(root);

        root = new TreeItem<String>("Leaves");
        for(TargetDTO target : targetList) {
            if(target.isLeaf()) {
                tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                setSmallTree(tempItm, target, Location.LEAF);
                root.getChildren().add(tempItm);
            }
        }
        treeViewLtoR.setRoot(root);

        root = new TreeItem<String>("Independents");
        for(TargetDTO target : targetList) {
            if(target.isIndependent()) {
                tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                root.getChildren().add(tempItm);
            }
        }
        treeViewInd.setRoot(root);


    }
    private void setSmallTree(TreeItem<String> treeParent, TargetDTO targetParent, Location type) {
        if ((targetParent.getTargetDependsOn().isEmpty() && type == Location.ROOT) || (targetParent.getTargetRequiredFor().isEmpty() && type == Location.LEAF))
            return;

        TreeItem<String> tempTreeItem;

        if (type == Location.ROOT) {
            for (String depTarget : targetParent.getTargetDependsOn()) {
                tempTreeItem = new TreeItem<>(depTarget + " (" + parentController.getTargetDTO(depTarget).getTargetStateString() + ")");
                setSmallTree(tempTreeItem, parentController.getTargetDTO(depTarget), type);
                treeParent.getChildren().add(tempTreeItem);
            }
        }
        else if (type == Location.LEAF) {
            for (String reqTarget : targetParent.getTargetRequiredFor()) {
                tempTreeItem = new TreeItem<>(reqTarget + " (" + parentController.getTargetDTO(reqTarget).getTargetStateString() + ")");
                setSmallTree(tempTreeItem, parentController.getTargetDTO(reqTarget), type);
                treeParent.getChildren().add(tempTreeItem);
            }
        }
    }

    public void setCycle() {
        treeViewRtoL.setDisable(true);
        treeViewLtoR.setDisable(true);
    }
}
