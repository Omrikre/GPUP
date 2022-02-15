package components.chat.chatroom;


import components.app.AppController;
import components.chat.chatarea.ChatAreaController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.Closeable;
import java.io.IOException;

public class ChatRoomMainController implements Closeable {
       // , HttpStatusUpdate, ChatCommands {

    @FXML private GridPane chatAreaComponent;
    @FXML private ChatAreaController chatAreaComponentController;

    private AppController appMainController;

    @FXML
    public void initialize() {
        //chatAreaComponentController.setHttpStatusUpdate(this);
        //chatAreaComponentController.autoUpdatesProperty().bind(actionCommandsComponentController.autoUpdatesProperty());
    }

/*    @Override
    public void updateHttpLine(String line) {
        appMainController.updateHttpLine(line);
    }*/

    @Override
    public void close() throws IOException { chatAreaComponentController.close();}

    //public void setActive() {chatAreaComponentController.startListRefresher();}

    public void setInActive() {
        try {
            chatAreaComponentController.close();
        } catch (Exception ignored) {}
    }
}
