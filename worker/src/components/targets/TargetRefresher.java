package components.targets;

import Engine.DTO.TargetForWorkerDTO;
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

public class TargetRefresher extends TimerTask {
    private final Consumer<List<TargetForWorkerDTO>> targetConsumer;
    private final BooleanProperty shouldUpdate;

    public TargetRefresher(Consumer<List<TargetForWorkerDTO>> targetConsumer, BooleanProperty shouldUpdate) {
        this.targetConsumer = targetConsumer;
        this.shouldUpdate = shouldUpdate;
    }


    @Override
    public void run() {

        if (!shouldUpdate.get())
            return;

        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("targets-for-worker", "true")
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("error target table update")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("error target table update with code: " + response.code())
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            System.out.println("TARGET LIST JSON: " + responseBody);
                            TargetForWorkerDTO[] lst = GSON.fromJson(responseBody, TargetForWorkerDTO[].class);
                            if(lst==null) {
                                System.out.println("lst is null ------");
                                return;
                            }
                            else
                                System.out.println(lst.toString());
                            targetConsumer.accept(Arrays.asList(lst));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
