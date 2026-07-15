package dev.rono.permissions.core.platform;

import java.time.Duration;

/**
 * Represents a scheduling mechanism for executing tasks on a platform.
 * Provides both synchronous and asynchronous task scheduling options,
 * as well as utilities for scheduling delayed and repeating tasks.
 */
public interface PlatformScheduler {
    /**
     * Executes the given task synchronously on the main thread.
     *
     * @param task
     *            the runnable task to be executed; must not be null
     */
    void execute(Runnable task);

    /**
     * Executes the given task asynchronously on a separate thread or in an
     * environment that does not block the
     * main thread. The task execution order and context depend on the
     * implementation of the platform scheduler.
     *
     * @param task
     *            the runnable task to be executed asynchronously; must not be null
     */
    void executeAsync(Runnable task);

    /**
     * Schedules the specified task to be executed after a delay on the primary
     * thread.
     * <p>
     * The delay is interpreted as a duration and internally converted to the
     * platform's time unit
     * (e.g., ticks if applicable). The task is guaranteed to execute after at least
     * the specified delay
     * but may be subject to scheduling considerations from the platform.
     *
     * @param task
     *            the runnable task to be executed later; must not be null
     * @param delay
     *            the duration to wait before executing the task; must not be null
     *            and must be non-negative
     */
    void executeLater(Runnable task, Duration delay);

    /**
     * Schedules the specified task to be executed asynchronously after a delay.
     * The task is executed in a separate thread or environment that does not
     * block the main thread. The delay is interpreted as a duration and converted
     * to the platform's time unit if applicable. The task is guaranteed to execute
     * after at least the specified delay, but the exact execution time may vary
     * based on platform-specific scheduling mechanisms.
     *
     * @param task
     *            the runnable task to be executed asynchronously after the delay;
     *            must not be null
     * @param delay
     *            the duration to wait before executing the task; must not be null
     *            and must be non-negative
     */
    void executeLaterAsync(Runnable task, Duration delay);

    /**
     * Schedules the specified task to be executed repeatedly at fixed intervals
     * on the main thread. The first execution happens immediately, and subsequent
     * executions occur after each specified interval.
     * <p>
     * The provided task must be thread-safe, as this method may be called in
     * various environments depending on the platform's task scheduling mechanism.
     *
     * @param task
     *            the runnable task to execute repeatedly; must not be null
     * @param interval
     *            the duration between each execution of the task; must not be null
     *            and must be positive
     */
    void executeRepeating(Runnable task, Duration interval);

    /**
     * Schedules the specified task to be executed repeatedly at fixed intervals
     * asynchronously. The task is executed in a separate thread or environment
     * that does not block the main thread. The first execution happens immediately,
     * and subsequent executions occur after each specified interval.
     * <p>
     * The provided task must be thread-safe, as this method operates asynchronously
     * and execution may overlap depending on the platform's scheduling mechanism.
     *
     * @param task
     *            the runnable task to execute repeatedly; must not be null
     * @param interval
     *            the duration between each execution of the task; must not be null
     *            and must be positive
     */
    void executeRepeatingAsync(Runnable task, Duration interval);

    /**
     * Schedules a task to be executed repeatedly at fixed intervals on the main
     * thread.
     * This method uses {@code executeRepeating} to perform the scheduling. The
     * first execution
     * of the task happens immediately, followed by subsequent executions after each
     * specified interval.
     *
     * @param task
     *            the runnable task to be scheduled for repeated execution; must not
     *            be null
     * @param interval
     *            the duration between each execution of the task; must not be null
     *            and must be positive
     * @return an integer representing the task ID if supported by the platform, or
     *         {@code -1} if not supported
     */
    default int scheduleRepeating(Runnable task, Duration interval) {
        executeRepeating(task, interval);

        return -1;
    }

    /**
     * Schedules the specified task to be executed repeatedly at fixed intervals
     * asynchronously.
     * The task is executed in a separate thread or environment that does not block
     * the main thread.
     * The first execution happens immediately, and subsequent executions occur
     * after each specified interval.
     *
     * @param task
     *            the runnable task to be executed repeatedly; must not be null
     * @param interval
     *            the duration between each execution of the task; must not be null
     *            and must be positive
     * @return an integer value representing the task ID if supported by the
     *         platform, or {@code -1} if task IDs are not supported
     */
    default int scheduleRepeatingAsync(Runnable task, Duration interval) {
        executeRepeatingAsync(task, interval);

        return -1;
    }

    /**
     * Checks if the current thread is the main thread.
     * <p>
     * This method can be used to determine whether the current execution context
     * is running on the platform's primary thread, often required for UI updates
     * or platform-specific operations that must not occur on background threads.
     *
     * @return {@code true} if the current thread is the main thread; {@code false}
     *         otherwise.
     */
    boolean isMainThread();

    /**
     * Cancels a scheduled task associated with the specified task ID.
     * The task is removed from the scheduler and will no longer be executed.
     *
     * @param taskId
     *            the unique identifier of the task to cancel; must be a valid ID
     *            previously returned by a scheduling operation
     */
    void cancelTask(int taskId);

    /**
     * Cancels all currently scheduled tasks managed by the platform scheduler.
     * This operation halts any tasks that are pending execution and removes them
     * from the scheduler's queue.
     * <p>
     * This method does not affect tasks that are already running or tasks
     * managed outside the scheduler.
     */
    void cancelTasks();
}
