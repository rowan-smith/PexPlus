package dev.rono.permissions.api.managers;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CacheManager<Identifier, Type> {

    Optional<Type> get(Identifier key);

    Set<Identifier> identifiers();

    Collection<Type> all();

    boolean isCached(Identifier key);

    boolean unload(Identifier key);
}
