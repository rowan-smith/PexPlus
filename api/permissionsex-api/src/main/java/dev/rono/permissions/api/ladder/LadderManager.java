package dev.rono.permissions.api.ladder;

import java.util.Optional;

/** Rank ladder registry with explicit find/get/create/exists lifecycle. */
public interface LadderManager {

    Optional<Ladder> findLadder(String name);

    Ladder getLadder(String name) throws LadderNotFoundException;

    Ladder createLadder(String name) throws LadderAlreadyExistsException;

    boolean exists(String name);
}
