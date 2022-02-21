package components.graphManager.table;

import Engine.DTO.TargetDTO;
import Engine.DTO.TargetDTOWithoutCB;
import Engine.Enums.Bond;
import components.graphManager.GraphController;
import components.graphManager.table.cycle.cycleController;
import components.graphManager.table.whatIf.whatIfController;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static components.app.CommonResourcesPaths.TABLE_CYCLE_fXML_RESOURCE;
import static components.app.CommonResourcesPaths.TABLE_WHATIF_fXML_RESOURCE;
import static components.app.HttpResourcesPaths.GRAPH_LIST;
import static components.app.HttpResourcesPaths.GSON;


public class tableController {

    // panes
    @FXML private BorderPane settingsBp;

    // cycle
    private VBox cycleVBPane;
    private cycleController cycleVBPaneController;

    // what if
    private VBox whatIfVBPane;
    private whatIfController whatIfVBPaneController;

    // Path
    @FXML private VBox pathVBPane;
    //
    @FXML private Label PATHfirstTargetLabel;
    @FXML private Label PATHsecondTargetLabel;
    @FXML private Button PATHclearTextBt;
    @FXML private Button PATHgetPathBt;
    @FXML private TextArea PATHdepTextBox;
    @FXML  private TextArea PATHreqTextBox;
    @FXML private Label PATHAreqBLabel;
    @FXML private Label PATHAdepBLabel;



    // settings
    // menu buttons
    @FXML private Button PATHmenuBt;
    @FXML private Button WHATmenuBt;
    @FXML private Button CYCLEmenuBt;

    // table
    @FXML private TableView<TargetDTO> targetDataTable;
    @FXML private TableColumn<TargetDTO, CheckBox> checkBoxCOL;//
    @FXML private TableColumn<TargetDTO, String> targetNameCOL;//
    @FXML private TableColumn<TargetDTO, String> targetTypeCOL;//
    @FXML private TableColumn<TargetDTO, Integer> depOnDirectCOL;//
    @FXML private TableColumn<TargetDTO, Integer> depOnTotalCOL;//
    @FXML private TableColumn<TargetDTO, Integer> reqForDirectCOL;//
    @FXML private TableColumn<TargetDTO, Integer> reqForTotalCOL;//
    @FXML private TableColumn<TargetDTO, String> serialSetCOL;//
    @FXML private TableColumn<TargetDTO, String> targetInfoCOL;//

    // in class members
    private GraphController parentController;
    private ObservableList<TargetDTO> OLTargets;

    private String firstTargetName;
    private String secondTargetName;

    private ObservableSet<CheckBox> selectedCheckBoxes;
    private ObservableSet<CheckBox> unselectedCheckBoxes;
    private final int maxNumSelectedPath = 2;
    private final int maxNumSelectedElse = 1;
    private int tabNum;

    private IntegerBinding numCheckBoxesSelected;



    // initializers
    @FXML public void initialize() {
        tabNum = 1;

        selectedCheckBoxes = FXCollections.observableSet();
        unselectedCheckBoxes = FXCollections.observableSet();
        numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

        targetNameCOL.setStyle( "-fx-alignment: CENTER;");
        depOnDirectCOL.setStyle( "-fx-alignment: CENTER;");
        depOnTotalCOL.setStyle( "-fx-alignment: CENTER;");
        reqForDirectCOL.setStyle( "-fx-alignment: CENTER;");
        reqForTotalCOL.setStyle( "-fx-alignment: CENTER;");
        serialSetCOL.setStyle( "-fx-alignment: CENTER;");
        targetInfoCOL.setStyle( "-fx-alignment: CENTER;");
        checkBoxCOL.setStyle( "-fx-alignment: CENTER;");
        targetTypeCOL.setStyle( "-fx-alignment: CENTER;");
    }
    public void setParentController(GraphController parentController) {
        this.parentController = parentController;
    }


    // data setup
    public void setupData(List<TargetDTOWithoutCB> targets) {
        CheckBox tempCB;
        TargetDTO tempTDTO;
        List<TargetDTO> newTargetsLst = new ArrayList<>();
        for (TargetDTOWithoutCB tWithout : targets) {
            tempCB = new CheckBox();
            tempTDTO = new TargetDTO(tWithout, tempCB);
            configureCheckBox(tempCB, tempTDTO.getTargetName());
            newTargetsLst.add(tempTDTO);
        }

        pathSetup();


        numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
            if (newSelectedCount.intValue() == maxNumSelectedElse && getTabNum() != 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
                PATHgetPathBt.setDisable(true);
                whatIfVBPaneController.setWhatBtDisable(false);
                cycleVBPaneController.setCycleDisable(false);
            }
            else if (newSelectedCount.intValue() == maxNumSelectedPath && getTabNum() == 1) {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));
                PATHgetPathBt.setDisable(false);
                WHATmenuBt.setDisable(true);
                CYCLEmenuBt.setDisable(true);
                PATHmenuBt.setDisable(true);

            }
            else {
                unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));
                PATHgetPathBt.setDisable(true);
                whatIfVBPaneController.setWhatBtDisable(true);
                cycleVBPaneController.setCycleDisable(true);
                WHATmenuBt.setDisable(false);
                CYCLEmenuBt.setDisable(false);
                PATHmenuBt.setDisable(false);
            }
        });

        OLTargets = FXCollections.observableArrayList(newTargetsLst);
        setTable();
        loadBackComponents();
        whatIfVBPaneController.whatIfSetup();
        cycleVBPaneController.cycleSetup();
    }
    private int getTabNum() { return tabNum; }

    public void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // cycle
            fxmlLoader.setLocation(getClass().getResource(TABLE_CYCLE_fXML_RESOURCE));
            cycleVBPane = fxmlLoader.load();
            cycleVBPaneController = fxmlLoader.getController();
            cycleVBPaneController.setParentController(this);
            System.out.println(" -- table (cycle) done --");

            // what if
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TABLE_WHATIF_fXML_RESOURCE));
            whatIfVBPane = fxmlLoader.load();
            whatIfVBPaneController = fxmlLoader.getController();
            whatIfVBPaneController.setParentController(this);
            System.out.println(" -- table (what if) done --");

        } catch (Exception e) {
            System.out.println("BIG (table) Problem");
            System.out.println(e.getMessage());
        }
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
        if(tabNum == 2)
            whatIfVBPaneController.clearSelectedTargetLabel();
        else if(tabNum == 3)
            cycleVBPaneController.clearSelectedTargetLabel();
        else {
            if (numCheckBoxesSelected.get() == 0) {
                PATHfirstTargetLabel.setText(" -");
                PATHsecondTargetLabel.setText(" -");
                firstTargetName = null;
                secondTargetName = null;
            } else {
                PATHsecondTargetLabel.setText(" -");
                secondTargetName = null;
            }
        }
    }
    private void setupNames(String targetName) {
        if(tabNum == 2)
            whatIfVBPaneController.setSelectedTargetLabel(targetName);
        else if(tabNum == 3)
            cycleVBPaneController.setSelectedTargetLabel(targetName);
        else {
            if (numCheckBoxesSelected.get() == 1) {
                PATHfirstTargetLabel.setText(targetName);
                firstTargetName = targetName;
                PATHsecondTargetLabel.setText(" -");
                secondTargetName = null;
            } else {
                PATHsecondTargetLabel.setText(targetName);
                secondTargetName = targetName;
            }
        }

    }

    // table
    public void setTable() {
        targetNameCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetName"));
        depOnDirectCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum"));
        depOnTotalCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("totalDependencies"));
        reqForDirectCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetRequiredForNum"));
        reqForTotalCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("totalRequierments"));
        serialSetCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("serialSetsBelongs"));
        targetInfoCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetInfo"));
        targetTypeCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetLocationString"));

        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, CheckBox>("selectedState"));

        targetDataTable.setItems(OLTargets);
    }

    // change panes methods
    @FXML void CYCLEmenuPr(ActionEvent event) {
        clearTableCB(3);

        settingsBp.setCenter(cycleVBPane);
    }
    @FXML void PATHmenuPr(ActionEvent event) {
        clearTableCB(1);
        settingsBp.setCenter(pathVBPane);
    }
    @FXML void WHATmenuPr(ActionEvent event) {
        clearTableCB(2);
        settingsBp.setCenter(whatIfVBPane);
    }

    private void clearTableCB(int tabNum) {
        selectedCheckBoxes.forEach(checkBox -> checkBox.setSelected(false));
        this.tabNum = tabNum;
    }

    // path tab
    private void pathSetup() {
        pathClearTextBoxes();
        PATHfirstTargetLabel.setText(" -");
        PATHsecondTargetLabel.setText(" -");
        PATHgetPathBt.setDisable(true);
    }

    private void pathSetTextBoxes(String a, String b , Bond bond, String lst) {
        String noPath = "-- There is no path --";
        Set<String> check = new HashSet();

        PATHAreqBLabel.setText(a + " required for " + b);
        PATHAdepBLabel.setText(a + " depends for " + b);

        if(bond == Bond.REQUIRED_FOR) {
            if (lst == null || lst.isEmpty() || lst.equals(check.toString()))
                PATHreqTextBox.appendText(noPath);
            else
                PATHreqTextBox.appendText(lst);
        }
        else {
            if (lst == null || lst.isEmpty() || lst.equals(check.toString()))
                PATHdepTextBox.appendText(noPath);
            else
                PATHdepTextBox.appendText(lst);
        }
    }

    private String createListOfTargets(Set<List<String>> lst, Bond whichWay) {
        StringBuilder stringBuild = new StringBuilder("");
        boolean firstTarget = true;
        int lineCount = 0;

        if(lst.isEmpty())
            return " -- There is no path --";

        for (List<String> line : lst) {
            lineCount++;
            stringBuild.append(" ").append(lineCount).append(")   ");
            for (String targetName : line) {
                if (firstTarget) {
                    stringBuild.append(targetName);
                    firstTarget = false;
                } else {
                    if (whichWay == Bond.DEPENDS_ON)
                        stringBuild.append(" >> ").append(targetName);
                    else
                        stringBuild.append(" << ").append(targetName);
                }
            }
            firstTarget = true;
            stringBuild.append("\n");
        }
        String firstRow;
        if(lineCount == 0)
            return null;
        else if(lineCount == 1)
            firstRow = " -- There is 1 path --\n\n";
        else
            firstRow = " -- There are " + lineCount + " paths --\n\n";

        return  firstRow + stringBuild;
    }
    private void pathClearTextBoxes() {
        PATHdepTextBox.clear();
        PATHreqTextBox.clear();
    }
    @FXML void pathClearTextPr(ActionEvent event) {
        PATHdepTextBox.clear();
        PATHreqTextBox.clear();
    }
    @FXML void pathGetPathPr(ActionEvent event) {
        pathClearTextBoxes();
        String depUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .addQueryParameter("graphname", getGraphName())
                .addQueryParameter("target-a",firstTargetName)
                .addQueryParameter("target-b",secondTargetName)
                .addQueryParameter("bond","dep")
                .build()
                .toString();

        String reqUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .addQueryParameter("graphname", getGraphName())
                .addQueryParameter("target-a",firstTargetName)
                .addQueryParameter("target-b",secondTargetName)
                .addQueryParameter("bond","req")
                .build()
                .toString();

        HttpClientUtil.runAsync(reqUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            Set<String> res = GSON.fromJson(responseBody, Set.class);
                            pathSetTextBoxes(firstTargetName, secondTargetName , Bond.REQUIRED_FOR, res.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        HttpClientUtil.runAsync(depUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            Set<String> res = GSON.fromJson(responseBody, Set.class);
                            pathSetTextBoxes(firstTargetName, secondTargetName , Bond.DEPENDS_ON, res.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public String getGraphName() { return parentController.getSelectedGraphName(); }

/*

    public Set<List<String>> getIfInCycle(String selectedTarget) {
        return mainController.getIfInCycle(selectedTarget);
    }

    public Set<String> getWhatIf(String selectedTarget, Bond bond) { return mainController.getWhatIf(selectedTarget, bond); }
*/

}







