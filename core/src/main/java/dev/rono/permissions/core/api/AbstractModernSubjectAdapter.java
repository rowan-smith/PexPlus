package dev.rono.permissions.core.api;

import dev.rono.permissions.api.subject.PermissionSubject;
import dev.rono.permissions.api.subject.SubjectType;
import dev.rono.permissions.core.DefaultPermissionManager;
import java.util.List;
import java.util.Map;
import ru.tehkode.permissions.PermissionEntity;

abstract class AbstractModernSubjectAdapter implements PermissionSubject {
    protected final PermissionEntity delegate;
    protected final DefaultPermissionManager manager;

    AbstractModernSubjectAdapter(PermissionEntity delegate, DefaultPermissionManager manager) {
        this.delegate = delegate;
        this.manager = manager;
    }

    @Override
    public abstract SubjectType type();

    @Override
    public String identifier() {
        return delegate.getIdentifier();
    }

    @Override
    public String name() {
        return delegate.getName();
    }

    @Override
    public boolean virtual() {
        return delegate.isVirtual();
    }

    @Override
    public boolean has(String permission, String world) {
        return delegate.has(permission, ModernWorlds.toLegacy(world));
    }

    @Override
    public List<String> permissions(String world) {
        return delegate.getOwnPermissions(ModernWorlds.toLegacy(world));
    }

    @Override
    public List<String> effectivePermissions(String world) {
        return delegate.getPermissions(ModernWorlds.toLegacy(world));
    }

    @Override
    public void addPermission(String permission, String world) {
        delegate.addPermission(permission, ModernWorlds.toLegacy(world));
    }

    @Override
    public void removePermission(String permission, String world) {
        delegate.removePermission(permission, ModernWorlds.toLegacy(world));
    }

    @Override
    public void setPermissions(List<String> permissions, String world) {
        delegate.setPermissions(permissions, ModernWorlds.toLegacy(world));
    }

    @Override
    public void addTimedPermission(String permission, String world, int lifetimeSeconds) {
        delegate.addTimedPermission(permission, ModernWorlds.toLegacy(world), lifetimeSeconds);
    }

    @Override
    public void removeTimedPermission(String permission, String world) {
        delegate.removeTimedPermission(permission, ModernWorlds.toLegacy(world));
    }

    @Override
    public List<String> timedPermissions(String world) {
        return delegate.getTimedPermissions(ModernWorlds.toLegacy(world));
    }

    @Override
    public String prefix(String world) {
        return delegate.getPrefix(ModernWorlds.toLegacy(world));
    }

    @Override
    public String suffix(String world) {
        return delegate.getSuffix(ModernWorlds.toLegacy(world));
    }

    @Override
    public void setPrefix(String prefix, String world) {
        delegate.setPrefix(prefix, ModernWorlds.toLegacy(world));
    }

    @Override
    public void setSuffix(String suffix, String world) {
        delegate.setSuffix(suffix, ModernWorlds.toLegacy(world));
    }

    @Override
    public String option(String key, String world) {
        return delegate.getOption(key, ModernWorlds.toLegacy(world), null);
    }

    @Override
    public void setOption(String key, String value, String world) {
        delegate.setOption(key, value, ModernWorlds.toLegacy(world));
    }

    @Override
    public Map<String, String> options(String world) {
        return delegate.getOptions(ModernWorlds.toLegacy(world));
    }

    @Override
    public void save() {
        delegate.save();
    }
}
