package components.graphManager.xmlLoader;

import Engine.DTO.GraphDTO;
import components.app.AppController;
import components.graphManager.GraphController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class LoadXMLController {

    @FXML private Label loadResponseLB;
    @FXML private Button loginBT;
    @FXML private Label userNameLB;
    @FXML private Label userNameNameLB;

    @FXML private TableView<GraphDTO> graphTable;
    @FXML private TableColumn<GraphDTO, CheckBox> checkBoxCOL;
    @FXML private TableColumn<GraphDTO, String> nameCOL;
    @FXML private TableColumn<GraphDTO, String> uploadByCol;
    @FXML private TableColumn<GraphDTO, Integer> priceCol;
    @FXML private TableColumn<GraphDTO, Integer> independCOL;
    @FXML private TableColumn<GraphDTO, Integer> leafCOL;
    @FXML private TableColumn<GraphDTO, Integer> middleCOL;
    @FXML private TableColumn<GraphDTO, Integer> rootCol;
    @FXML private Label selectedGraphLB;
    @FXML private Label uploadInfo1LB;
    @FXML private Label uploadInfo2LB;
    @FXML private Button uploadXMLBT;


    private GraphController graphParentController;
    private AppController  mainController;

    private IntegerBinding numCheckBoxesSelected;

    private ObservableList<GraphDTO> OLGraphs;
    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;




    @FXML public void initialize() {
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        checkBoxCOL.setStyle( "-fx-alignment: CENTER;");
        priceCol.setStyle( "-fx-alignment: CENTER;");
        independCOL.setStyle( "-fx-alignment: CENTER;");
        leafCOL.setStyle( "-fx-alignment: CENTER;");
        middleCOL.setStyle( "-fx-alignment: CENTER;");
        rootCol.setStyle( "-fx-alignment: CENTER;");

        selectedGraphLB.setText("-");

        //setupData();
    }


    public void setupData(List<GraphDTO> graphs) {

        CheckBox tempCB;
        for(GraphDTO graph : graphs) {
            tempCB = new CheckBox();
            graph.setSelectedState(tempCB);
            configureCheckBox(tempCB, graph.getGraphName());
        }

        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() == 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
            }
            else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
            }
        });

        OLGraphs = FXCollections.observableArrayList(graphs);
        setTable();

    }



    private void configureCheckBox(CheckBox checkBox, String targetName) {
        if (checkBox.isSelected())
            selectedCheckBoxes.add(checkBox);
        else
            unselectedCheckBoxes.add(checkBox);

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
                setupNames(targetName);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                clearLastName();
            }
        });
    }
    private void clearLastName() { selectedGraphLB.setText("-");}
    private void setupNames(String graphName) {  selectedGraphLB.setText(graphName); }
    public void setTable() {

        nameCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, String>("graphName"));
        uploadByCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, String>("uploadByAdminName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("pricePerTarget"));
        independCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("independenceCount"));
        leafCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("leafCount"));
        middleCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("middleCount"));
        rootCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("rootCount"));

        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, CheckBox>("selectedState"));

        graphTable.setItems(OLGraphs);
    }





    @FXML
//    void uploadXMLPR(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
//        Stage chooser = new Stage();
//        chooser.initModality(Modality.APPLICATION_MODAL);
//        File file = fileChooser.showOpenDialog(chooser);
//        try {
//            if (file == null || !mainController.checkFileIsValid(file.getAbsolutePath())) {
//                uploadInfo1LB.setText("Please select file");
//                return;
//            }
//        } catch (Exception e) {
//            uploadInfo1LB.setText("Unable to load the file: ");
//            return;
//        }
//        uploadInfo1LB.setText("The XML file has been loaded");
//    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
    public void setGraphParentController(GraphController graphController) { this.graphParentController = graphController;}




}
