package ru.tehkode.permissions.backends.data;

import dev.rono.permissions.api.PexApi;
import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.core.manager.GroupManagerImpl;
import java.util.List;
import java.util.Set;
import ru.tehkode.permissions.PermissionsGroupData;

final class GroupData extends AbstractData implements PermissionsGroupData {
    GroupData(PexApi api, String identifier) {
        super(api, identifier);
    }

    private Group group() {
        var group = api.groups().cache().get(identifier);

        return group
                .orElseGet(() -> api.groups().storage().get(identifier).toCompletableFuture().join()
                        .orElseGet(() -> ((GroupManagerImpl) api.groups()).create(identifier).toCompletableFuture().join()));
    }

    @Override
    protected PermissionHolder holder() {
        return group();
    }

    @Override
    protected Set<ParentNode> parents() {
        return group().parents();
    }

    @Override
    protected void replacePermissions(ContextSet contexts, List<String> permissions) {
        group();

        api.groups().modify(identifier, modifier -> {
            modifier.clearPermissions(contexts);

            permissions.forEach(value -> modifier.setPermission(PermissionNode
                    .builder().permission(value).contexts(contexts).build()));
        }).toCompletableFuture().join();
    }

    @Override
    protected void replaceParents(ContextSet contexts, List<String> parents) {
        var group = group();

        api.groups().modify(identifier, modifier -> {
            group.parents().stream().filter(node -> node.contexts().equals(contexts)).forEach(modifier::removeParent);

            parents.forEach(value -> modifier.addParent(value, contexts));
        }).toCompletableFuture().join();
    }

    @Override
    protected void setOptionNode(ContextSet contexts, String key, String value) {
        group();

        if ("weight".equalsIgnoreCase(key) && contexts.isEmpty()) {
            api.groups().modify(identifier, modifier -> {
                if (value == null) {
                    modifier.clearWeight();
                } else {
                    modifier.setWeight(Integer.parseInt(value));
                }
            }).toCompletableFuture().join();

            return;
        }

        if ("default".equalsIgnoreCase(key)) {
            var configured = api.resolvers().defaultGroups().resolve()
                    .map(group -> group.name().equalsIgnoreCase(identifier)).orElse(false);

            if (Boolean.parseBoolean(value) != configured) {
                throw new UnsupportedOperationException(
                        "The implicit default group is configured by default-group in config.yml");
            }

            return;
        }

        api.groups().modify(identifier, modifier -> {
            if (value == null) {
                modifier.removeOption(key, contexts);
            } else {
                modifier.setOption(key, value, contexts);
            }
        }).toCompletableFuture().join();
    }

    @Override
    public String getOption(String option, String world) {
        if ("weight".equalsIgnoreCase(option) && contexts(world).isEmpty()) {
            return group().weight().isPresent() ? Integer.toString(group().weight().getAsInt()) : null;
        }

        if ("default".equalsIgnoreCase(option)) {
            return Boolean.toString(api.resolvers().defaultGroups().resolve()
                    .map(value -> value.name().equalsIgnoreCase(identifier)).orElse(false));
        }

        return super.getOption(option, world);
    }

    @Override
    public boolean isVirtual() {
        return api.groups().find(identifier).toCompletableFuture().join().isEmpty();
    }

    @Override
    public void remove() {
        ((GroupManagerImpl) api.groups()).delete(identifier).toCompletableFuture().join();
    }
}
