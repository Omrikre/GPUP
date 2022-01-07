package components.info.generalInfo;

import Engine.Enums.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class generalInfoController {

    @FXML private TableView<targetsInfoBox> targetsTypeTV;
    @FXML private TableColumn<targetsInfoBox, String> AtargetTypeCol;
    @FXML private TableColumn<targetsInfoBox, Integer> AquantityCol;

    @FXML    private TableView<?> serialSetTV;
    @FXML    private TableColumn<?, ?> BsetNameCol;
    @FXML    private TableColumn<?, ?> BquantityCol;
    @FXML    private TableColumn<?, ?> BtargetsNamesCol;

    @FXML private Label numOfSetsLabel;
    @FXML private Label fileNameLabel;
    @FXML private Label numOfTargetsLabel;
    @FXML private Label containsCycleLabel;

    ObservableList<targetsInfoBox> lst;

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


    public void setupData(Map<Location, Integer> TV_A_Map, int numOfTargets, boolean containsCycle) {
        setTableA(TV_A_Map);
        //TODO - setTableB(); - need serialSet info
        setLabels(numOfTargets, containsCycle);
    }

    private void setLabels(int numOfTargets, boolean containsCycle) {
        numOfTargetsLabel.setText(String.valueOf(numOfTargets));
        //TODO - num of sets, file name
        //numOfSetsLabel.setText();
        //fileNameLabel.setText();
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

    //TODO setTableB - get all serial set info
    //private void setTableA(Map<Location, Integer> infoMap)
}



