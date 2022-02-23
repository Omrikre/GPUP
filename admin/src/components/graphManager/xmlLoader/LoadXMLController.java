package components.graphManager.xmlLoader;

import Engine.DTO.GraphDTO;
import Engine.DTO.GraphDTOWithoutCB;
import components.app.AppController;
import components.graphManager.GraphController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
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
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static components.app.CommonResourcesPaths.REFRESH_RATE;
import static components.app.HttpResourcesPaths.GRAPH;
import static components.app.HttpResourcesPaths.LOAD_XML_FILE;

public class LoadXMLController {

    //TODO - DELETE
    @FXML private Label loadResponseLB;
    @FXML private Button loginBT;
    @FXML private Label userNameLB;
    @FXML private Label userNameNameLB;

    @FXML private TableView<GraphDTO> graphTable;
    @FXML private TableColumn<GraphDTO, CheckBox> checkBoxCOL;
    @FXML private TableColumn<GraphDTO, String> nameCOL;
    @FXML private TableColumn<GraphDTO, String> uploadByCol;
    @FXML private TableColumn<GraphDTO, Integer> simPriceCol;
    @FXML private TableColumn<GraphDTO, Integer> compPriceCol;
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

    private String selectedGraphName;
    private GraphDTO selectedGraphDTO;
    private Timer timer;
    private BooleanProperty autoUpdate;
    private LoadXMLRefresher xmlRefresher;
    private int numOfGraphs;

    @FXML
    public void initialize() {
        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        uploadInfo1LB.setText("");
        uploadInfo2LB.setText("");
        selectedGraphName = "-";
        selectedGraphLB.setText(selectedGraphName);
        numOfGraphs = 0;
        setupData();
    }

    public void startXMLGraphTableRefresher(BooleanProperty autoUpdate) {
        this.autoUpdate = autoUpdate;
        xmlRefresher = new LoadXMLRefresher(
                this.autoUpdate, this::updateGraphList);
        timer = new Timer();
        timer.schedule(xmlRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void updateGraphList(List<GraphDTOWithoutCB> lst) {
        Platform.runLater(() -> {
            if ((lst.size() == numOfGraphs))
                return;
            numOfGraphs = lst.size();

            List<GraphDTO> newGraphList = new ArrayList();
            ObservableList<GraphDTO> graphTV = graphTable.getItems();
            GraphDTO tempDTO;
            CheckBox tempCheckBox;

            for (GraphDTOWithoutCB graph : lst) {
                tempCheckBox = new CheckBox();
                if (selectedGraphName == graph.getGraphName()) {
                    tempCheckBox.setSelected(true);
                }
                configureCheckBox(tempCheckBox, graph.getGraphName());
                tempDTO = new GraphDTO(graph.getGraphName(), graph.getUploadByAdminName(), graph.getSimPricePerTarget(), graph.getCompPricePerTarget(), graph.getIndependenceCount(), graph.getLeafCount(), graph.getMiddleCount(), graph.getMiddleCount(), tempCheckBox, graph.isContainsCycle());
                newGraphList.add(tempDTO);
            }
            ObservableList<GraphDTO> OLGraph = FXCollections.observableArrayList(newGraphList);
            OLGraphs = OLGraph;
            graphTable.setItems(OLGraphs);
            graphTable.refresh();
        });
    }

    public void setupData() {

        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() == 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
            } else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
            }
        });


        setTable();

    }

    private void setGraphSelected(String graphName, boolean bool) {
        if (graphName == selectedGraphName)
            return;

        selectedGraphName = graphName;
        selectedGraphLB.setText(selectedGraphName);
        if(bool) {
            graphParentController.setData(graphName , selectedGraphDTO);
            graphParentController.disableAllHeaderBt(false);
        }
        else {
            graphParentController.disableAllHeaderBt(true);
        }
    }

    private void configureCheckBox(CheckBox checkBox, String GraphName) {
        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
            for (GraphDTO g : OLGraphs) {
                if (g.getGraphName().equals(GraphName))
                    selectedGraphDTO = g;
            }
            setGraphSelected(GraphName, true);
        }
        else {
            unselectedCheckBoxes.add(checkBox);
            setGraphSelected("-", false);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                uploadXMLBT.setDisable(true);
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
                setupNames(GraphName);
                for (GraphDTO g : OLGraphs) {
                    if (g.getGraphName().equals(GraphName))
                        selectedGraphDTO = g;
                }
                setGraphSelected(GraphName, true);
            } else {
                uploadXMLBT.setDisable(false);
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
                clearLastName();
                selectedGraphDTO = null;
                setGraphSelected("", false);
            }
        });
    }

    private void clearLastName() {selectedGraphLB.setText("-");}

    private void setupNames(String graphName) {selectedGraphLB.setText(graphName);}

    public void setTable() {

        checkBoxCOL.setStyle("-fx-alignment: CENTER;");
        simPriceCol.setStyle("-fx-alignment: CENTER;");
        compPriceCol.setStyle("-fx-alignment: CENTER;");
        independCOL.setStyle("-fx-alignment: CENTER;");
        leafCOL.setStyle("-fx-alignment: CENTER;");
        middleCOL.setStyle("-fx-alignment: CENTER;");
        rootCol.setStyle("-fx-alignment: CENTER;");
        nameCOL.setStyle("-fx-alignment: CENTER;");
        uploadByCol.setStyle("-fx-alignment: CENTER;");

        nameCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, String>("graphName"));
        uploadByCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, String>("uploadByAdminName"));
        simPriceCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("simPricePerTarget"));
        compPriceCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("compPricePerTarget"));
        independCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("independenceCount"));
        leafCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("leafCount"));
        middleCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("middleCount"));
        rootCol.setCellValueFactory(new PropertyValueFactory<GraphDTO, Integer>("rootCount"));
        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<GraphDTO, CheckBox>("selectedState"));
    }


    @FXML
    void uploadXMLPR(ActionEvent event) throws MalformedURLException { //TODO - send the file to the server

        clearFileUploadLB();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
        Stage chooser = new Stage();
        chooser.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showOpenDialog(chooser);

        if (file == null)
            return;
        String finalUrl = HttpUrl
                .parse(GRAPH)
                .newBuilder()
                .build()
                .toString();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("error send th file: " + file.getName()));



            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    System.out.println("error send the file: " + file.getName());
                    System.out.println(responseBody);


                }
                else {
                    //uploadInfo1LB.setText("upload successfully");
                    //uploadInfo2LB.setText("file name: " + file.getName()); //todo
                    System.out.println("ok");

                }
            }
        });
    }

    private void clearFileUploadLB() {
        uploadInfo1LB.setText("");
        uploadInfo2LB.setText("");
    }

    public void setMainController(AppController mainController) {this.mainController = mainController;}

    public void setGraphParentController(GraphController graphController) {this.graphParentController = graphController;}


    public void closeXMLLoader() {
        if (timer != null) {
            xmlRefresher.cancel();
            timer.cancel();
        }
        graphTable.getItems().clear();
        graphTable.refresh();
    }

    public void clear() {
        if(selectedCheckBoxes != null) {
            if (!selectedCheckBoxes.isEmpty()) {
                selectedCheckBoxes.forEach(cb -> cb.setSelected(false));
            }
        }
    }
}

/*
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
 */
