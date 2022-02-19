package components.graphManager.xmlLoader;

import Engine.DTO.GraphDTO;
import Engine.DTO.MissionDTO;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.HttpResourcesPaths.*;
import static components.app.HttpResourcesPaths.GSON;

public class LoadXMLRefresher extends TimerTask {

    private Consumer<List<GraphDTO>> graphListConsumer;
    private BooleanProperty shouldUpdate;


    public LoadXMLRefresher(BooleanProperty shouldUpdate, Consumer<List<GraphDTO>> graphListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.graphListConsumer = graphListConsumer;
    }

    @Override
    public void run() {
//TODO
/*        if (!shouldUpdate.get()) {
            return;
        }*/

        String finalUrl = HttpUrl
                .parse(GRAPH_LIST)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("error graphList table update")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("error graphList table update with code: " + response.code())
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            GraphDTO[] lst = GSON.fromJson(responseBody, GraphDTO[].class);
                            graphListConsumer.accept(Arrays.asList(lst));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
