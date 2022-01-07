package components.table;

import components.table.cycle.cycleController;
import components.table.whatIf.whatIfController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import Engine.DTO.TargetDTO;
import Engine.Enums.Bond;
import components.app.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static components.app.CommonResourcesPaths.*;


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
    private AppController mainController;
    private ObservableList<TargetDTO> OLTargets;
    private int numOfCBSelected;
    private String firstTargetName;
    private String secondTargetName;
    private Map<String, CheckBox> CheckBoxMap;



    // initializers
    @FXML public void initialize() {
        CheckBoxMap = new HashMap<>();
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
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    // data setup
    public void setupData(List<TargetDTO> targets) {
        numOfCBSelected = 0;
        pathSetup();
        System.out.println("2");

        CheckBox tempCB;
        for(TargetDTO target : targets) {
            tempCB = new CheckBox();
            target.setSelectedState(tempCB);
            CheckBoxMap.put(target.getTargetName(),tempCB);
        }
        OLTargets = FXCollections.observableArrayList(targets);
        System.out.println("2");
        setTable();
        loadBackComponents();
        whatIfVBPaneController.whatIfSetup();
        cycleVBPaneController.cycleSetup();
    }
    public void loadBackComponents() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            // what if pane
            fxmlLoader.setLocation(getClass().getResource(TABLE_CYCLE_fXML_RESOURCE));
            System.out.println("1");
            cycleVBPane = fxmlLoader.load();
            System.out.println("1");
            cycleVBPaneController = fxmlLoader.getController();
            cycleVBPaneController.setParentController(this);
            System.out.println(" -- table (what if) done --");

            // task pane
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(TABLE_WHATIF_fXML_RESOURCE));
            whatIfVBPane = fxmlLoader.load();
            whatIfVBPaneController = fxmlLoader.getController();
            whatIfVBPaneController.setParentController(this);
            System.out.println(" -- table (find cycle) done --");

        } catch (Exception e) {
            System.out.println("BIG (table) Problem");
            System.out.println(e.getMessage());
        }
    }

    // table
    public void setTable() {
        targetNameCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetName"));
        depOnDirectCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum"));
        //depOnTotalCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum")); //TODO
        reqForDirectCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetRequiredForNum"));
        //reqForTotalCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, Integer>("targetDependsOnNum")); //TODO
        //serialSetCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetDependsOnNum")); //TODO
        targetInfoCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetInfo"));
        targetTypeCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, String>("targetLocationString"));

        checkBoxCOL.setCellValueFactory(new PropertyValueFactory<TargetDTO, CheckBox>("selectedState"));

        targetDataTable.setItems(OLTargets);
    }



    // change panes methods
    @FXML void CYCLEmenuPr(ActionEvent event) {
        clearTableCB();
        settingsBp.setCenter(cycleVBPane);
    }
    @FXML void PATHmenuPr(ActionEvent event) {
        clearTableCB();
        settingsBp.setCenter(pathVBPane);
    }
    @FXML void WHATmenuPr(ActionEvent event) {
        clearTableCB();
        settingsBp.setCenter(whatIfVBPane);
    }

    private void clearTableCB() {
    }




    //
    // path tab
    private void pathSetup() {
        pathClearTextBoxes();
        PATHfirstTargetLabel.setText(" -");
        PATHsecondTargetLabel.setText(" -");
        PATHgetPathBt.setDisable(true);
    }
    private void pathSetTextBoxes(String a, String b) {
        String lst;
        String noPath = "-- There is no path --";

        PATHAreqBLabel.setText(a + " required for " + b);
        PATHAdepBLabel.setText(a + " depends for " + b);

        lst = createListOfTargets(mainController.getPathRequired(a,b), Bond.REQUIRED_FOR);
        if(lst == null || lst.isEmpty())
            PATHreqTextBox.appendText(noPath);
        else
            PATHreqTextBox.appendText(lst);

        lst = createListOfTargets(mainController.getPathDepends(a,b), Bond.DEPENDS_ON);
        if(lst == null || lst.isEmpty())
            PATHdepTextBox.appendText(noPath);
        else
            PATHdepTextBox.appendText(lst);
    }
    private String createListOfTargets(Set<List<String>> lst, Bond whichWay) {
        StringBuilder stringBuild = new StringBuilder("");
        boolean firstTarget = true;
        int lineCount = 0;

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
            firstRow = " There is 1 path\n\n";
        else
            firstRow = " There are " + lineCount + " paths\n\n";

        return  firstRow + stringBuild;
    }
    private void pathClearTextBoxes() {
        PATHdepTextBox.clear();
        PATHreqTextBox.clear();
    }
    @FXML void pathClearTextPr(ActionEvent event) {
        System.out.println("1");
    }
    @FXML void pathGetPathPr(ActionEvent event) {
        System.out.println("1");
    }










}







