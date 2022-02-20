package components.graphManager.info.generalInfo;

import Engine.DTO.GraphDTO;
import Engine.Enums.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;
import java.util.Set;

public class generalInfoController {

    @FXML private TableView<targetsInfoBox> targetsTypeTV;
    @FXML private TableColumn<targetsInfoBox, String> AtargetTypeCol;
    @FXML private TableColumn<targetsInfoBox, Integer> AquantityCol;

    @FXML    private TableView<serialSetBox> serialSetTV;
    @FXML    private TableColumn<serialSetBox, String> BsetNameCol;
    @FXML    private TableColumn<serialSetBox, Integer> BquantityCol;
    @FXML    private TableColumn<serialSetBox, String> BtargetsNamesCol;

    @FXML private Label numOfSetsLabel;
    @FXML private Label fileNameLabel;
    @FXML private Label numOfTargetsLabel;
    @FXML private Label containsCycleLabel;

    ObservableList<targetsInfoBox> lst;
    ObservableList<serialSetBox> lstSS;

    @FXML private void initialize() {
        numOfSetsLabel.setText("-");
        fileNameLabel.setText("-");
        numOfTargetsLabel.setText("-");
        containsCycleLabel.setText("-");

        AtargetTypeCol.setStyle( "-fx-alignment: CENTER;");
        AquantityCol.setStyle( "-fx-alignment: CENTER;");

        BquantityCol.setStyle( "-fx-alignment: CENTER;");
        BtargetsNamesCol.setStyle( "-fx-alignment: CENTER;");
        BsetNameCol.setStyle( "-fx-alignment: CENTER;");
    }


    public void setupData(Map<Location, Integer> TV_A_Map, int numOfTargets, boolean containsCycle, String fileName, int numOfSets, Map<String, Set<String>> serialSets) {
        setTableA(TV_A_Map);
        setTableB(serialSets);
        setLabels(numOfTargets, containsCycle, fileName, numOfSets);
    }

    private void setTableB(Map<String, Set<String>> serialSets) {
        lstSS = FXCollections.observableArrayList();
        for(String s : serialSets.keySet()){
            lstSS.add(new serialSetBox(s,serialSets.get(s).size(),serialSets.get(s).toString()));
        }

        BquantityCol.setCellValueFactory(new PropertyValueFactory<serialSetBox, Integer>("membersNum"));
        BtargetsNamesCol.setCellValueFactory(new PropertyValueFactory<serialSetBox, String>("membersNames"));
        BsetNameCol.setCellValueFactory(new PropertyValueFactory<serialSetBox, String>("setName"));


        serialSetTV.setItems(lstSS);
    }

    private void setLabels(int numOfTargets, boolean containsCycle, String fileName, Integer numOfSets) {
        numOfTargetsLabel.setText(String.valueOf(numOfTargets));
        numOfSetsLabel.setText(numOfSets.toString());
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


    public void setupData(GraphDTO graph) {
    }
}



