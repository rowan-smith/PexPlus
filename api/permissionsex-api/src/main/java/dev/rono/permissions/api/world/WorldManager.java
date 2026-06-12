package dev.rono.permissions.api.world;

import java.util.Optional;

/** World registry with explicit find/get/create/exists lifecycle. */
public interface WorldManager {

    Optional<World> findWorld(String name);

    World getWorld(String name) throws WorldNotFoundException;

    World createWorld(String name) throws WorldAlreadyExistsException;

    boolean exists(String name);
}
