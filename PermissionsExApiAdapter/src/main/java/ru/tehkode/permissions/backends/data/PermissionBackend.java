package ru.tehkode.permissions.backends.data;

import dev.rono.permissions.api.PexApi;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionsGroupData;
import ru.tehkode.permissions.PermissionsUserData;
import ru.tehkode.permissions.exceptions.PermissionBackendException;

/** Legacy PermissionsEx storage view backed by the live API managers. */
public final class PermissionBackend extends ru.tehkode.permissions.backends.PermissionBackend {
    private final PexApi api;

    private final Map<String, List<String>> worldInheritance = new LinkedHashMap<>();

    public PermissionBackend(PermissionManager manager, ConfigurationSection config, PexApi api) throws PermissionBackendException {
        super(manager, config);

        this.api = Objects.requireNonNull(api, "api");
    }

    @Override
    public int getSchemaVersion() {
        return 2;
    }

    @Override
    protected void setSchemaVersion(int version) {}

    @Override
    public void reload() {}

    @Override
    public PermissionsUserData getUserData(String userName) {
        return new UserData(api, userName);
    }

    @Override
    public PermissionsGroupData getGroupData(String groupName) {
        return new GroupData(api, groupName);
    }

    @Override
    public boolean hasUser(String userName) {
        return UserData.find(api, userName).isPresent();
    }

    @Override
    public boolean hasGroup(String group) {
        return api.groups().find(group).toCompletableFuture().join().isPresent();
    }

    @Override
    public Collection<String> getUserIdentifiers() {
        return api.users().cache().identifiers().stream().map(Object::toString).sorted().toList();
    }

    @Override
    public Collection<String> getUserNames() {
        return api.users().cache().names().stream().sorted().toList();
    }

    @Override
    public Collection<String> getGroupNames() {
        return api.groups().cache().identifiers();
    }

    @Override
    public synchronized List<String> getWorldInheritance(String world) {
        return List.copyOf(worldInheritance.getOrDefault(world, List.of()));
    }

    @Override
    public synchronized Map<String, List<String>> getAllWorldInheritance() {
        var copy = new LinkedHashMap<String, List<String>>();

        worldInheritance.forEach((key, value) -> copy.put(key, List.copyOf(value)));

        return Map.copyOf(copy);
    }

    @Override
    public synchronized void setWorldInheritance(String world, List<String> inheritance) {
        if (inheritance == null || inheritance.isEmpty()) {
            worldInheritance.remove(world);
        } else {
            worldInheritance.put(world, List.copyOf(inheritance));
        }
    }

    @Override
    public void writeContents(Writer writer) throws IOException {
        writer.write("# PermissionsEx compatibility view backed by PermissionsExPlus API\n");

        writer.write("groups: " + String.join(",", getGroupNames()) + "\n");

        writer.write("users: " + String.join(",", getUserIdentifiers()) + "\n");
    }
}
