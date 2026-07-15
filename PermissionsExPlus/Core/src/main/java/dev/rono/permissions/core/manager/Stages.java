package dev.rono.permissions.core.manager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

final class Stages {
    private Stages() {}

    static <T> CompletionStage<T> call(Supplier<T> action) {
        try {
            return CompletableFuture.completedFuture(action.get());
        } catch (Throwable error) {
            return CompletableFuture.failedFuture(error);
        }
    }

    static <T> CompletionStage<T> call(Supplier<T> action, Executor executor) {
        var future = new CompletableFuture<T>();

        try {
            executor.execute(() -> {
                try {
                    future.complete(action.get());
                } catch (Throwable error) {
                    future.completeExceptionally(error);
                }
            });
        } catch (Throwable error) {
            future.completeExceptionally(error);
        }

        return future;
    }
}
