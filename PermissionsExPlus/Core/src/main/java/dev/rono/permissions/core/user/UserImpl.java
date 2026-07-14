package dev.rono.permissions.core.user;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.subject.SubjectType;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.permission.PermissionResolver;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class UserImpl implements User, PermissionHolder {

    private final UUID id;
    private final String username;

    private final Set<String> permissions;
    private final Set<String> groups;

    private final PermissionResolver resolver;

    public UserImpl(UUID id, String username, PermissionResolver resolver){
        this.id = id;
        this.username = username;
        this.resolver = resolver;

        this.permissions = ConcurrentHashMap.newKeySet();
        this.groups = ConcurrentHashMap.newKeySet();
    }

    @Override
    public UUID id(){
        return id;
    }

    @Override
    public String username(){
        return username;
    }

    @Override
    public PermissionContext context(Realm realm){
        return new UserContextImpl(this, realm, resolver);
    }

    @Override
    public void addPermission(PermissionNode permission) {

    }

    @Override
    public void removePermission(String permission, Realm realm) {

    }

    @Override
    public SubjectType type() {
        return SubjectType.USER;
    }

    public Set<String> permissions(){
        return permissions;
    }

    public Set<String> groups(){
        return groups;
    }
}
