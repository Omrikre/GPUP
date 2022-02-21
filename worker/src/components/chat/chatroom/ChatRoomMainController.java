package components.chat.chatroom;


import components.chat.chatarea.model.ChatLinesWithVersion;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.stream.Collectors;

import static components.app.CommonResourcesPaths.REFRESH_RATE;
import static components.app.HttpResourcesPaths.*;


public class ChatRoomMainController {

    private ChatAreaRefresher chatRefresh;

    @FXML private Button sendBT;
    @FXML private ToggleButton autoScrollButton;
    @FXML private Label chatVersionLabel;
    @FXML private TextArea mainChatLinesTextArea;
    @FXML private TextArea chatLineTextArea;

    private Timer timerChatRefresher;
    private IntegerProperty chatVersion;
    private Boolean autoScroll;
    private ChatAreaRefresher chatAreaRefresher;


    @FXML
    public void initialize() {
        chatVersionLabel.setText("Chat Version: 0");
        chatVersion = new SimpleIntegerProperty(0);
        autoScroll = true;
        mainChatLinesTextArea.setEditable(false);
        mainChatLinesTextArea.clear();
        autoScrollButton.setSelected(true);


    }

    public void startChatRefresher(BooleanProperty autoUpdate) {
        chatAreaRefresher = new ChatAreaRefresher(chatVersion, autoUpdate, this::updateChatArea);
        timerChatRefresher = new Timer();
        timerChatRefresher.schedule(chatAreaRefresher, REFRESH_RATE, REFRESH_RATE);

    }

    public void setInActive() {
        chatRefresh.cancel();
    }



    @FXML
    void sendPR(ActionEvent event) {
        String chatLine = chatLineTextArea.getText();
        String finalUrl = HttpUrl
                .parse(SEND_CHAT_LINE)
                .newBuilder()
                .addQueryParameter("userstring", chatLine)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("send fail");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("send successes " + chatLine);
                }
            }
        });

        chatLineTextArea.clear();
    }

    public void updateChatArea(ChatLinesWithVersion chatLog) {

        if (chatLog.getVersion() != chatVersion.get()) {
            String deltaChatLines = chatLog
                    .getEntries()
                    .stream()
                    .map(singleChatLine -> {
                        long time = singleChatLine.getTime();
                        return String.format(CHAT_LINE_FORMATTING, time, time, time, singleChatLine.getUsername(), singleChatLine.getChatString());
                    }).collect(Collectors.joining());

            Platform.runLater(() -> {
                chatVersion.set(chatLog.getVersion());
                if(chatVersion != null) {
                    chatVersionLabel.setText("Chat Version: " + chatVersion.get());
                }

                if (autoScroll) {
                    mainChatLinesTextArea.appendText(deltaChatLines);
                    mainChatLinesTextArea.selectPositionCaret(mainChatLinesTextArea.getLength());
                    mainChatLinesTextArea.deselect();
                } else {
                    int originalCaretPosition = mainChatLinesTextArea.getCaretPosition();
                    mainChatLinesTextArea.appendText(deltaChatLines);
                    mainChatLinesTextArea.positionCaret(originalCaretPosition);
                }
            });
        }
    }


    public void closeChat() {
        if (timerChatRefresher != null) {
            chatAreaRefresher.cancel();
            timerChatRefresher.cancel();
        }
        chatVersion.set(0);
        chatLineTextArea.clear();
        chatVersionLabel.setText("Chat Version: 0");
        mainChatLinesTextArea.clear();
    }
}






/*


    // @FXML private GridPane chatAreaComponent;
    private ChatAreaController chatAreaComponentController;

    private Timer timerMissionRefresher;
    private Timer timerChatRefresher;
    private ChatAreaRefresher chatAreaRefresher;
    private IntegerProperty chatVersion = new SimpleIntegerProperty();
    private BooleanProperty autoUpdate;

    public void starChatRefresher() {
        chatAreaRefresher = new ChatAreaRefresher(chatVersion, autoUpdate, this::updateChatLines);
        timerChatRefresher = new Timer();
        timerChatRefresher.schedule(chatAreaRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    public void updateHttpLine(String line) {
        //appMainController.updateHttpLine(line);
        System.out.println("in updateHttpLine");
    }

    @Override
    public void close() throws IOException { chatAreaComponentController.close();}

    public void setActive() {chatAreaComponentController.startListRefresher();}

    public void setInActive() {
        try {
            chatAreaComponentController.close();
        } catch (Exception ignored) {}
    }

    private synchronized void updateMissionLines(List<MissionInTable> missions) {
        Platform.runLater(() -> {
            unselectedCheckBoxes.clear();
            selectedCheckBoxes.clear();
            ObservableList<MissionInTable> items = tableViewMission.getItems();
            for (MissionInTable mission : missions) { /// update check box
                configureCheckBox(mission.getCheckBox());
            }

            for(int i = 0 ; i < items.size() ; ++i) { /// update check box
                for (int j = 0 ; j < missions.size() ; ++j)
                    if(missions.get(j).getNameOfMission().equals(items.get(i).getNameOfMission()))
                        missions.get(j).changeMissionInformationAdmin(items.get(i));
            }


            items.clear();
            items.addAll(missions);
        });
    }*/

