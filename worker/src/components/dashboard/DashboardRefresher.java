package components.dashboard;

import Engine.DTO.MissionDTOWithoutCB;
import Engine.users.User;
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

public class DashboardRefresher extends TimerTask {

    private final Consumer<List<MissionDTOWithoutCB>> missionsConsumer;
    private final Consumer<List<User>> usersListConsumer;
    private final Consumer<List<MissionDTOWithoutCB>> missionConsumerDashboard;
    private final BooleanProperty shouldUpdate;


    public DashboardRefresher(BooleanProperty shouldUpdate, Consumer<List<User>> userConsumer,
                              Consumer<List<MissionDTOWithoutCB>> missionConsumerDashboard, Consumer<List<MissionDTOWithoutCB>> missionsConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = userConsumer;
        this.missionConsumerDashboard = missionConsumerDashboard;
        this.missionsConsumer = missionsConsumer;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get())
            return;

        String userUrl = HttpUrl
                .parse(USERS_LIST)
                .newBuilder()
                .build()
                .toString();

        String missionUrl = HttpUrl
                .parse(MISSION_LIST)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(userUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("error userUrl table update")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("error userUrl table update with code: " + response.code())
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            User[] lst = GSON.fromJson(responseBody, User[].class);
                            usersListConsumer.accept(Arrays.asList(lst));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        HttpClientUtil.runAsync(missionUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        System.out.println("error missionUrl table update " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            System.out.println("error missionUrl table update with code: " + response.code())
                    );
                } else {
                    Platform.runLater(() -> {
                        try {
                            String responseBody = response.body().string();
                            MissionDTOWithoutCB[] lst = GSON.fromJson(responseBody, MissionDTOWithoutCB[].class);
                            missionsConsumer.accept(Arrays.asList(lst));
                            missionConsumerDashboard.accept(Arrays.asList(lst));

                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
