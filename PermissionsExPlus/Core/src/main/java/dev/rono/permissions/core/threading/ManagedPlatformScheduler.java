package dev.rono.permissions.core.threading;

import dev.rono.permissions.core.platform.PlatformScheduler;

import java.time.Duration;
import java.util.Objects;

/** Routes background task bodies through the core-owned worker pool. */
public final class ManagedPlatformScheduler implements PlatformScheduler {
    private final PlatformScheduler platform;
    private final CoreTaskExecutor tasks;

    public ManagedPlatformScheduler(PlatformScheduler platform, CoreTaskExecutor tasks) {
        this.platform = Objects.requireNonNull(platform, "platform");
        this.tasks = Objects.requireNonNull(tasks, "tasks");
    }

    @Override
    public void execute(Runnable task) {
        platform.execute(task);
    }

    @Override
    public void executeAsync(Runnable task) {
        tasks.execute(task);
    }

    @Override
    public void executeLater(Runnable task, Duration delay) {
        platform.executeLater(task, delay);
    }

    @Override
    public void executeLaterAsync(Runnable task, Duration delay) {
        platform.executeLater(() -> tasks.execute(task), delay);
    }

    @Override
    public void executeRepeating(Runnable task, Duration interval) {
        platform.executeRepeating(task, interval);
    }

    @Override
    public void executeRepeatingAsync(Runnable task, Duration interval) {
        platform.executeRepeating(() -> tasks.execute(task), interval);
    }

    @Override
    public int scheduleRepeating(Runnable task, Duration interval) {
        return platform.scheduleRepeating(task, interval);
    }

    @Override
    public int scheduleRepeatingAsync(Runnable task, Duration interval) {
        return platform.scheduleRepeating(() -> tasks.execute(task), interval);
    }

    @Override
    public boolean isMainThread() {
        return platform.isMainThread();
    }

    @Override
    public void cancelTask(int taskId) {
        platform.cancelTask(taskId);
    }

    @Override
    public void cancelTasks() {
        platform.cancelTasks();
    }
}
