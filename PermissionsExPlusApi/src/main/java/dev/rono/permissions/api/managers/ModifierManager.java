package dev.rono.permissions.api.managers;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface ModifierManager<Identifier, Type, Modifier> {
    CompletionStage<Type> modify(Identifier identifier, Consumer<Modifier> action);

    CompletionStage<Optional<Type>> find(Identifier identifier);
}
