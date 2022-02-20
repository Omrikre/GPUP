package components.graphManager.info.generalInfo;

import Engine.DTO.GraphDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class generalInfoController {

    @FXML private TableView<targetsInfoBox> targetsTypeTV;
    @FXML private TableColumn<targetsInfoBox, String> AtargetTypeCol;
    @FXML private TableColumn<targetsInfoBox, Integer> AquantityCol;

    @FXML private Label fileNameLabel;
    @FXML private Label numOfTargetsLabel;
    @FXML private Label containsCycleLabel;

    @FXML private GridPane generalInfoPane;



    ObservableList<targetsInfoBox> lst;
    ObservableList<serialSetBox> lstSS;

    @FXML private void initialize() {
        fileNameLabel.setText("-");
        numOfTargetsLabel.setText("-");
        containsCycleLabel.setText("-");

        AtargetTypeCol.setStyle( "-fx-alignment: CENTER;");
        AquantityCol.setStyle( "-fx-alignment: CENTER;");
    }


    public void setupDataInTable(Map<Location, Integer> TV_A_Map, int numOfTargets, boolean containsCycle, String fileName, int numOfSets, Map<String, Set<String>> serialSets) {
        setTableA(TV_A_Map);
        setLabels(numOfTargets, containsCycle, fileName, numOfSets);
    }


    private void setLabels(int numOfTargets, boolean containsCycle, String fileName, Integer numOfSets) {
        numOfTargetsLabel.setText(String.valueOf(numOfTargets));
        fileNameLabel.setText(fileName);
        String cycle = "no";
        if(containsCycle)
            cycle = "yes";
        containsCycleLabel.setText(cycle);
    }


    private void setTableA(Map<Location, Integer> infoMap) {

        lst = FXCollections.observableArrayList(
                new targetsInfoBox("leafs",(int)infoMap.get(Location.LEAF)),
                new targetsInfoBox("middles",(int)infoMap.get(Location.MIDDLE)),
                new targetsInfoBox("roots",(int)infoMap.get(Location.ROOT)),
                new targetsInfoBox("independents",(int)infoMap.get(Location.INDEPENDENT))
        );
        AtargetTypeCol.setCellValueFactory(new PropertyValueFactory<targetsInfoBox, String>("type"));
        AquantityCol.setCellValueFactory(new PropertyValueFactory<targetsInfoBox, Integer>("quantity"));

        targetsTypeTV.setItems(lst);

    }


    public void setupData(GraphDTO graph, List<TargetDTOWithoutCB> targets) {
        Map<Location, Integer> TV_A_Map = new HashMap();
        TV_A_Map.put(Location.LEAF, graph.getLeafCount());
        TV_A_Map.put(Location.INDEPENDENT, graph.getIndependenceCount());
        TV_A_Map.put(Location.ROOT, graph.getRootCount());
        TV_A_Map.put(Location.MIDDLE, graph.getMiddleCount());

        setupDataInTable(TV_A_Map, targets.size(), graph.isContainsCycle(), graph.getGraphName(), 0, null);
    }
}



