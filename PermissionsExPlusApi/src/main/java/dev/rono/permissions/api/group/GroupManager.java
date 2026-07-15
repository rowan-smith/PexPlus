package dev.rono.permissions.api.group;

import dev.rono.permissions.api.managers.Manager;

import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface GroupManager extends Manager<String, Group, GroupModifier> {

    @Override
    GroupCacheManager cache();

    @Override
    GroupStorageManager storage();

    default CompletionStage<Group> modify(Group group, Consumer<GroupModifier> action) {
        Objects.requireNonNull(group, "group");
        Objects.requireNonNull(action, "action");

        return modify(group.name(), action);
    }
}
