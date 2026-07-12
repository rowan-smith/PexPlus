package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.group.PermissionGroup;
import dev.rono.permissions.api.user.PermissionUser;

import java.util.Optional;
import java.util.function.Predicate;

public interface LadderManager {

    Optional<PermissionLadder> find(String name);

    PermissionLadder load(String name);

    PermissionLadder create(String name);

    void delete(String name);

    boolean exists(String name);

    int count();

    int count(Predicate<PermissionLadder> filter);

    PermissionGroup promote(PermissionUser user, PermissionLadder ladder);

    PermissionGroup demote(PermissionUser user, PermissionLadder ladder);

}
