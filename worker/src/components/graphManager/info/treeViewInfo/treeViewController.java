package components.graphManager.info.treeViewInfo;

import components.graphManager.info.InfoController;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;


public class treeViewController {

    @FXML private TreeView<String> treeViewRtoL;
    @FXML private TreeView<String> treeViewLtoR;
    @FXML private TreeView<String> treeViewInd;

    private InfoController parentController;

    public void setParentController(InfoController parentController) { this.parentController = parentController;    }
    /*
        public void setTrees(List<TargetDTO> targetList) {
            treeViewRtoL.setDisable(false);
            treeViewLtoR.setDisable(false);
            treeViewInd.setDisable(false);
            TreeItem<String> tempItm;
            TreeItem<String> root;

            int counter = 0;
            root = new TreeItem<String>("Roots");
            for(TargetDTO target : targetList) {
                if(target.isRoot()) {
                    counter++;
                    tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                    setSmallTree(tempItm, target, Location.ROOT);
                    root.getChildren().add(tempItm);
                }
            }
            if(counter == 0) {
                tempItm = new TreeItem<String>(" -- No Roots --");
                root.getChildren().add(tempItm);
            }
            treeViewRtoL.setRoot(root);

            counter = 0;
            root = new TreeItem<String>("Leaves");
            for(TargetDTO target : targetList) {
                if(target.isLeaf()) {
                    counter++;
                    tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                    setSmallTree(tempItm, target, Location.LEAF);
                    root.getChildren().add(tempItm);
                }
            }
            if(counter == 0) {
                tempItm = new TreeItem<String>(" -- No Leaves --");
                root.getChildren().add(tempItm);
            }
            treeViewLtoR.setRoot(root);

            counter = 0;
            root = new TreeItem<String>("Independents");
            for(TargetDTO target : targetList) {
                if(target.isIndependent()) {
                    counter++;
                    tempItm = new TreeItem<String>(target.getTargetName() + " (" + target.getTargetStateString() + ")");
                    root.getChildren().add(tempItm);
                }
            }
          if(counter == 0) {
                tempItm = new TreeItem<String>(" -- No Independents --");
                root.getChildren().add(tempItm);
            }
            treeViewInd.setRoot(root);


        }
        /*
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
    */
    public void setCycle() {
        treeViewRtoL.setDisable(true);
        treeViewLtoR.setDisable(true);
    }
}
