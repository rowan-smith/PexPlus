package dev.rono.permissions.core.user;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.permission.PermissionResolver;

import java.util.Collection;
import java.util.List;

public final class UserContextImpl implements PermissionContext {

    private final User user;
    private final Realm realm;

    private final PermissionResolver resolver;

    public UserContextImpl(User user, Realm realm, PermissionResolver resolver) {
        this.user = user;
        this.realm = realm;
        this.resolver = resolver;
    }

    @Override
    public Realm realm() {
        return realm;
    }

    @Override
    public PermissionHolder holder() {
        return user;
    }

    @Override
    public boolean has(String permission) {
        return check(permission) == PermissionResult.TRUE;
    }

    @Override
    public PermissionResult check(String permission) {
        return resolver.resolve(user, realm, permission);
    }

    @Override
    public Collection<PermissionNode> nodes() {
        return List.of();
    }
}
