package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.api.world.World;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;

import java.util.UUID;

final class UserImpl implements User {

    private final UUID id;
    private final PermissionUser delegate;
    private final PermissionHolder holder;

    UserImpl(UUID id, PermissionUser delegate) {
        this.id = id;
        this.delegate = delegate;
        this.holder = new UserPermissionHolder(id);
    }

  PermissionUser delegate() {
        return delegate;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        String name = delegate.getName();
        return name != null ? name : id.toString();
    }

    @Override
    public PermissionHolder asHolder() {
        return holder;
    }
}

final class GroupImpl implements Group {

    private final String name;
    private final PermissionGroup delegate;
    private final PermissionHolder holder;

    GroupImpl(String name, PermissionGroup delegate) {
        this.name = name;
        this.delegate = delegate;
        this.holder = new GroupPermissionHolder(name);
    }

  PermissionGroup delegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionHolder asHolder() {
        return holder;
    }
}

final class WorldImpl implements World {

    private final String name;
    private final PermissionHolder holder;

    WorldImpl(String name) {
        this.name = name;
        this.holder = new WorldPermissionHolder(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionHolder asHolder() {
        return holder;
    }
}

final class LadderImpl implements Ladder {

    private final String name;
    private final PermissionHolder holder;

    LadderImpl(String name) {
        this.name = name;
        this.holder = new LadderPermissionHolder(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PermissionHolder asHolder() {
        return holder;
    }
}
