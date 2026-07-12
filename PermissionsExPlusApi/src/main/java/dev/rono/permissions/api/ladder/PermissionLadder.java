package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.group.PermissionGroup;

import java.util.List;

public interface PermissionLadder {

    String name();

    List<PermissionGroup> groups();

}
