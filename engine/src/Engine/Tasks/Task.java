package Engine.Tasks;

import Engine.Engine;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public abstract class Task {
    private final String name;
    private final LocalDateTime executionDate;
    private long totalRuntime; //the total runtime of all targets

    public Task(String name) {
        this.name = name;
        this.executionDate = LocalDateTime.now();
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
}
