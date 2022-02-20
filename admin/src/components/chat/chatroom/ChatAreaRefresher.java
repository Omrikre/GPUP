package components.chat.chatroom;


import components.chat.chatarea.model.ChatLinesWithVersion;
import http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.HttpResourcesPaths.*;
;


public class ChatAreaRefresher extends TimerTask {

    private final BooleanProperty shouldUpdate;
    private Consumer<ChatLinesWithVersion> chatlinesConsumer;
    private IntegerProperty chatVersion;




    public ChatAreaRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.chatVersion = chatVersion;
        this.shouldUpdate = shouldUpdate;
        this.chatlinesConsumer = chatlinesConsumer;
    }

    @Override
    public void run() {
        if(!(shouldUpdate.getValue()))
            return;

        String finalUrl = HttpUrl
                .parse(CHAT_LINES_LIST)
                .newBuilder()
                .addQueryParameter("chatversion", String.valueOf(chatVersion.get()))
                .build()
                .toString();

        //httpRequestLoggerConsumer.accept("About to invoke: " + finalUrl + " | Chat Request # " + finalRequestNumber);

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("no response");
                //httpRequestLoggerConsumer.accept("Something went wrong with Chat Request # " + finalRequestNumber);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String rawBody = response.body().string();
                    ChatLinesWithVersion clwv = GSON.fromJson(rawBody, ChatLinesWithVersion.class);
                    chatlinesConsumer.accept(clwv);
                } else {
                    System.out.println("bad response: " + response.code());
                }
            }
        });

    }




    /*
        public ChatAreaRefresher(IntegerProperty chatVersion, BooleanProperty shouldUpdate, Consumer<String> httpRequestLoggerConsumer, Consumer<ChatLinesWithVersion> chatlinesConsumer) {
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.chatlinesConsumer = chatlinesConsumer;
        this.chatVersion = chatVersion;
        this.shouldUpdate = shouldUpdate;
        requestNumber = 0;
    }*/

}
