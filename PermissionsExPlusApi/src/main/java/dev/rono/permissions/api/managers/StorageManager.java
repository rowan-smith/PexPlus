package dev.rono.permissions.api.managers;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface StorageManager<Identifier, Type> {

    CompletionStage<Optional<Type>> get(Identifier key);
}
