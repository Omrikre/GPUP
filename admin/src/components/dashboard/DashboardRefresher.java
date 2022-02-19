package components.dashboard;

import Engine.users.UserManager;
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
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static components.app.HttpResourcesPaths.*;

public class DashboardRefresher extends TimerTask {

    private Consumer<List<UserManager.User>> usersListConsumer = null;
    private int requestNumber;
    private final BooleanProperty shouldUpdate;


    public DashboardRefresher(BooleanProperty shouldUpdate, Consumer<List<UserManager.User>> dashboardConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

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
                            System.out.println(responseBody);
                            //List<UserManager.User> lst = GSON.fromJson(responseBody, UserManager.User.class);
                            //System.out.println(lst.size());//TODO
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
