package ru.tehkode.permissions.backends.data;

import dev.rono.permissions.api.PexApi;
import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.manager.UserManagerImpl;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import ru.tehkode.permissions.PermissionsUserData;

final class UserData extends AbstractData implements PermissionsUserData {
    UserData(PexApi api, String identifier) {
        super(api, identifier);
    }

    static Optional<User> find(PexApi api, String identifier) {
        try {
            return api.users().find(UUID.fromString(identifier)).toCompletableFuture().join();
        } catch (IllegalArgumentException ignored) {
            return api.users().find(identifier).toCompletableFuture().join();
        }
    }

    private User user() {
        return find(api, identifier).orElseGet(this::create);
    }

    private User create() {
        UUID id;

        try {
            id = UUID.fromString(identifier);
        } catch (IllegalArgumentException ignored) {
            id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + identifier).getBytes(StandardCharsets.UTF_8));
        }

        return ((UserManagerImpl) api.users()).create(id, identifier).toCompletableFuture().join();
    }

    @Override
    protected PermissionHolder holder() {
        return user();
    }

    @Override
    protected Set<ParentNode> parents() {
        return user().groups();
    }

    @Override
    protected void replacePermissions(ContextSet contexts, List<String> permissions) {
        var user = user();

        api.users().modify(user, modifier -> {
            modifier.clearPermissions(contexts);

            permissions.forEach(value -> modifier.setPermission(PermissionNode
                    .builder().permission(value).contexts(contexts).build()));
        }).toCompletableFuture().join();
    }

    @Override
    protected void replaceParents(ContextSet contexts, List<String> parents) {
        var user = user();

        api.users().modify(user, modifier -> {
            user.groups().stream().filter(node -> node.contexts().equals(contexts)).forEach(modifier::removeGroup);

            parents.forEach(value -> modifier.addGroup(value, contexts));
        }).toCompletableFuture().join();
    }

    @Override
    protected void setOptionNode(ContextSet contexts, String key, String value) {
        var user = user();

        api.users().modify(user, modifier -> {
            if (value == null) {
                modifier.removeOption(key, contexts);
            } else {
                modifier.setOption(key, value, contexts);
            }
        }).toCompletableFuture().join();

        if ("name".equalsIgnoreCase(key) && value != null && contexts.isEmpty()) {
            api.users().modify(user.uniqueId(), userModifier -> {
                userModifier.updateName(value);
            });
        }
    }

    @Override
    public boolean isVirtual() {
        return find(api, identifier).isEmpty();
    }

    @Override
    public void remove() {
        find(api, identifier).ifPresent(user -> ((UserManagerImpl) api.users()).delete(user.uniqueId()).toCompletableFuture().join());
    }

    @Override
    public boolean setIdentifier(String value) {
        if (value.equals(identifier)) {
            return true;
        }

        var current = find(api, identifier).orElse(null);

        if (current == null || find(api, value).isPresent()) {
            return false;
        }

        try {
            var id = UUID.fromString(value);

            var replacement = ((UserManagerImpl) api.users()).create(id, current.name()).toCompletableFuture().join();

            api.users().modify(replacement.uniqueId(), modifier -> {
                current.explicitPermissions().forEach(modifier::setPermission);

                current.explicitOptions().forEach(modifier::setOption);

                modifier.setGroups(current.groups());
            }).toCompletableFuture().join();

            ((UserManagerImpl) api.users()).delete(current.uniqueId()).toCompletableFuture().join();

            identifier = value;

            return true;
        } catch (IllegalArgumentException ignored) {
            api.users().modify(current.uniqueId(), userModifier -> {
                userModifier.updateName(value);
            });

            identifier = value;

            return true;
        }
    }
}
