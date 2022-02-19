package Engine.Tasks;

import Engine.Engine;
import Engine.Enums.MissionState;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public abstract class Task {
    private final String name;
    private final LocalDateTime executionDate;
    private long totalRuntime; //the total runtime of all targets
    private int progress;
    private MissionState status;
    private int workers;
    private int totalPrice;
    private String creatorName;

    public Task(String name) {
        this.name = name;
        this.executionDate = LocalDateTime.now();
        totalRuntime = 0;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getWorkers() {
        return workers;
    }

    public MissionState getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public String getName() {
        return name;
    }

    public long getTotalRuntime() {
        return totalRuntime;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void addTotalRuntime(long runTime) {
        this.totalRuntime += runTime;
    }


}
