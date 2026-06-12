package dev.rono.permissions.api.ladder;

import java.util.Optional;
import java.util.function.Predicate;

/** Rank ladder registry with explicit find/get/create/exists lifecycle. */
public interface LadderManager {

    Optional<Ladder> findLadder(String name);

    Ladder getLadder(String name) throws LadderNotFoundException;

    Ladder createLadder(String name) throws LadderAlreadyExistsException;

    boolean exists(String name);

    /**
     * Returns the number of rank ladders referenced by stored groups.
     *
     * @return total known ladder count
     */
    int count();

    /**
     * Returns how many known ladders match {@code filter}.
     *
     * @param filter predicate applied to each known ladder; must not be {@code null}
     * @return count of ladders for which the predicate is {@code true}
     */
    int count(Predicate<Ladder> filter);
}
