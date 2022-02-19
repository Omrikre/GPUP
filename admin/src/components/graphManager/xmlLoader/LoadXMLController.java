package components.graphManager.xmlLoader;

import Engine.DTO.GraphDTO;
import components.app.AppController;
import components.graphManager.GraphController;
import http.HttpClientUtil;
import javafx.application.Platform;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static components.app.HttpResourcesPaths.GRAPH;
import static components.app.HttpResourcesPaths.LOGIN_PAGE;

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
    private AppController mainController;

    private IntegerBinding numCheckBoxesSelected;

    private ObservableList<GraphDTO> OLGraphs;
    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;

    private String selectedGraph;


    @FXML
    public void initialize() {
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        checkBoxCOL.setStyle("-fx-alignment: CENTER;");
        priceCol.setStyle("-fx-alignment: CENTER;");
        independCOL.setStyle("-fx-alignment: CENTER;");
        leafCOL.setStyle("-fx-alignment: CENTER;");
        middleCOL.setStyle("-fx-alignment: CENTER;");
        rootCol.setStyle("-fx-alignment: CENTER;");

        selectedGraphLB.setText("-");

    }


    public void setupData(List<GraphDTO> graphs) {

        CheckBox tempCB;
        for (GraphDTO graph : graphs) {
            tempCB = new CheckBox();
            graph.setSelectedState(tempCB);
            configureCheckBox(tempCB, graph.getGraphName());
        }

        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() == 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
            } else {
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

    private void clearLastName() {
        selectedGraphLB.setText("-");
    }

    private void setupNames(String graphName) {
        selectedGraphLB.setText(graphName);
    }

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
    void uploadXMLPR(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
        Stage chooser = new Stage();
        chooser.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showOpenDialog(chooser);
        try {
            if (file == null || !mainController.checkFileIsValid(file.getAbsolutePath(), mainController.getUsername())) {
                uploadInfo1LB.setText("Please select file");
                return;
            }
        } catch (Exception e) {
            uploadInfo1LB.setText("Unable to load the file: ");
            return;
        }
        uploadInfo1LB.setText("The XML file has been loaded");
        String finalUrl = HttpUrl
                .parse(GRAPH)
                .newBuilder()
                .addQueryParameter("filepath", file.getAbsolutePath().replace('\\', '('))
                .addQueryParameter("username", mainController.getUsername())
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        uploadInfo1LB.setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            uploadInfo1LB.setText("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            System.out.println(responseBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setGraphParentController(GraphController graphController) {
        this.graphParentController = graphController;
    }

    public void setupData() {
        //TODO get graph data from server
    }


}
