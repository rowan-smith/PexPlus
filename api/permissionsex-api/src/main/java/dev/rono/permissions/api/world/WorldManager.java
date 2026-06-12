package dev.rono.permissions.api.world;

import java.util.Optional;
import java.util.function.Predicate;

/** World registry with explicit find/get/create/exists lifecycle. */
public interface WorldManager {

    Optional<World> findWorld(String name);

    World getWorld(String name) throws WorldNotFoundException;

    World createWorld(String name) throws WorldAlreadyExistsException;

    boolean exists(String name);

    /**
     * Returns the number of registered worlds (platform realms and backend world inheritance entries).
     *
     * @return total registered world count
     */
    int count();

    /**
     * Returns how many registered worlds match {@code filter}.
     *
     * @param filter predicate applied to each registered world; must not be {@code null}
     * @return count of worlds for which the predicate is {@code true}
     */
    int count(Predicate<World> filter);
}
