package components.missions;

import Engine.DTO.TargetDTOWithoutCB;
import http.HttpClientUtil;
import javafx.application.Platform;
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

import static components.app.HttpResourcesPaths.GSON;
import static components.app.HttpResourcesPaths.MISSION_LIST;


public class RunnableTargetRefresher extends TimerTask {
    private String missionName;
    private final BooleanProperty shouldUpdate;
    Consumer<TargetDTOWithoutCB> targetDTOWithoutCBConsumer; //what the other function gets
    private final IntegerProperty threadsLeft;

    public RunnableTargetRefresher(IntegerProperty threadsLeft, String missionName, BooleanProperty shouldUpdate, Consumer<TargetDTOWithoutCB> targetDTOWithoutCBConsumer) {
        this.threadsLeft = threadsLeft;
        this.missionName = missionName;
        this.shouldUpdate = shouldUpdate;
        this.targetDTOWithoutCBConsumer = targetDTOWithoutCBConsumer;
    }


    @Override
    public void run() {
        System.out.println("IN RUN");
        if (!shouldUpdate.get())
            return;
        if (threadsLeft.getValue() == 0)
            return;
        String finalUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .addQueryParameter("name", missionName)
                .addQueryParameter("get", "true")
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println(e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println(("signup Fail code: " + response.code()) + " " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            TargetDTOWithoutCB t = GSON.fromJson(response.body().string(), TargetDTOWithoutCB.class);
                            System.out.println("TARGET: " + t);
                                if (t == null)
                                    targetDTOWithoutCBConsumer.accept(null);
                                else
                                    targetDTOWithoutCBConsumer.accept(t);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
