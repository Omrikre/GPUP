package components.info.generalInfo;

import Engine.Enums.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class generalInfoController {

    @FXML private GridPane generalInfoPane;
    @FXML private TableColumn<targetsInfoBox, String> typeCol;
    @FXML private TableColumn<targetsInfoBox, Integer> quantityCol;
    @FXML private Label numOfTargets;
    @FXML private TableView<targetsInfoBox> tableViewMain;

    ObservableList<targetsInfoBox> lst;


    @FXML private void initialize() {
        numOfTargets.setText("-");
        typeCol.setStyle( "-fx-alignment: CENTER;");
        quantityCol.setStyle( "-fx-alignment: CENTER;");
    }


    public void setNumOfTargets(int numOfTargets) {
        String numInString = String.valueOf(numOfTargets);
        this.numOfTargets.setText(numInString);
    }

    public void setTable(Map<Location, Integer> infoMap) {

        lst = FXCollections.observableArrayList(
                new targetsInfoBox("leafs",(int)infoMap.get(Location.LEAF)),
                new targetsInfoBox("middles",(int)infoMap.get(Location.MIDDLE)),
                new targetsInfoBox("roots",(int)infoMap.get(Location.ROOT)),
                new targetsInfoBox("independents",(int)infoMap.get(Location.INDEPENDENT))
        );
        typeCol.setCellValueFactory(new PropertyValueFactory<targetsInfoBox, String>("type"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<targetsInfoBox, Integer>("quantity"));

        tableViewMain.setItems(lst);

    }
}
