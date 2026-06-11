package dev.rono.permissions.core.api;

import dev.rono.permissions.api.subject.SubjectType;
import dev.rono.permissions.api.subject.User;
import dev.rono.permissions.core.DefaultPermissionManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ru.tehkode.permissions.PermissionUser;

public final class ModernUserAdapter extends AbstractModernSubjectAdapter implements User {
    private final PermissionUser user;

    public ModernUserAdapter(PermissionUser user, DefaultPermissionManager manager) {
        super(user, manager);
        this.user = user;
    }

    @Override
    public SubjectType type() {
        return SubjectType.USER;
    }

    @Override
    public Optional<UUID> uniqueId() {
        try {
            return Optional.of(UUID.fromString(user.getIdentifier()));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<String> groups(String world, boolean inherit) {
        String legacyWorld = ModernWorlds.toLegacy(world);
        return inherit ? user.getParentIdentifiers(legacyWorld) : user.getOwnParentIdentifiers(legacyWorld);
    }

    @Override
    public boolean inGroup(String groupName, String world, boolean inherit) {
        return user.inGroup(groupName, ModernWorlds.toLegacy(world), inherit);
    }

    @Override
    public void addGroup(String groupName, String world) {
        user.addGroup(groupName, ModernWorlds.toLegacy(world));
    }

    @Override
    public void addGroup(String groupName, String world, int lifetimeSeconds) {
        user.addGroup(groupName, ModernWorlds.toLegacy(world), lifetimeSeconds);
    }

    @Override
    public void removeGroup(String groupName, String world) {
        user.removeGroup(groupName, ModernWorlds.toLegacy(world));
    }

    @Override
    public void delete() {
        String id = user.getIdentifier();
        user.remove();
        manager.resetUser(id);
    }
}
