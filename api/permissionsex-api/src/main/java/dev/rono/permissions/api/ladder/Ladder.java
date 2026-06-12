package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.permission.PermissionHolder;

/** Rank ladder metadata (ordered promotion groups sharing a ladder name). */
public interface Ladder {

    String getName();

    PermissionHolder asHolder();
}
