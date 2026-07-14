package dev.rono.permissions.core.bridge;

import java.time.Duration;

public interface PlatformScheduler {
    void execute(Runnable task);

    void executeAsync(Runnable task);

    void executeLater(Runnable task, Duration delay);

    void executeLaterAsync(Runnable task, Duration delay);

    void executeRepeating(Runnable task, Duration interval);

    void executeRepeatingAsync(Runnable task, Duration interval);

    boolean isMainThread();

    void cancelTask(int taskId);

    void cancelTasks();
}
