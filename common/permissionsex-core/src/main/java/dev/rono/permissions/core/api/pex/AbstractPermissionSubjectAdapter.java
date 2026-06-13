package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.subject.PermissionSubject;
import dev.rono.permissions.api.subject.SubjectType;
import dev.rono.permissions.api.subject.TimedPermissionEntry;
import dev.rono.permissions.api.world.Worlds;
import dev.rono.permissions.core.DefaultPermissionManager;
import dev.rono.permissions.core.api.ContextPermissionEvaluator;
import dev.rono.permissions.core.api.ModernWorlds;
import ru.tehkode.permissions.PermissionEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class AbstractPermissionSubjectAdapter implements PermissionSubject {

    protected final PermissionEntity delegate;
    protected final DefaultPermissionManager manager;

    AbstractPermissionSubjectAdapter(PermissionEntity delegate, DefaultPermissionManager manager) {
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
    public boolean has(String permission, PermissionContext context) {
        return ContextPermissionEvaluator.has(delegate, permission, context, manager.getPlatform());
    }

    @Override
    public List<String> permissions(PermissionContext context) {
        return delegate.getOwnPermissions(storageRealm(context));
    }

    @Override
    public List<String> effectivePermissions(PermissionContext context) {
        return delegate.getPermissions(storageRealm(context));
    }

    @Override
    public void addPermission(String permission, PermissionContext context) {
        delegate.addPermission(permission, storageRealm(context));
    }

    @Override
    public void removePermission(String permission, PermissionContext context) {
        delegate.removePermission(permission, storageRealm(context));
    }

    @Override
    public void setPermissions(List<String> permissions, PermissionContext context) {
        delegate.setPermissions(permissions, storageRealm(context));
    }

    @Override
    public void addTimedPermission(String permission, PermissionContext context, int lifetimeSeconds) {
        delegate.addTimedPermission(permission, storageRealm(context), lifetimeSeconds);
    }

    @Override
    public void removeTimedPermission(String permission, PermissionContext context) {
        delegate.removeTimedPermission(permission, storageRealm(context));
    }

    @Override
    public List<String> timedPermissions(PermissionContext context) {
        return delegate.getTimedPermissions(storageRealm(context));
    }

    @Override
    public String prefix(PermissionContext context) {
        return delegate.getPrefix(storageRealm(context));
    }

    @Override
    public String suffix(PermissionContext context) {
        return delegate.getSuffix(storageRealm(context));
    }

    @Override
    public void setPrefix(String prefix, PermissionContext context) {
        delegate.setPrefix(prefix, storageRealm(context));
    }

    @Override
    public void setSuffix(String suffix, PermissionContext context) {
        delegate.setSuffix(suffix, storageRealm(context));
    }

    @Override
    public String option(String key, PermissionContext context) {
        return delegate.getOption(key, storageRealm(context), null);
    }

    @Override
    public void setOption(String key, String value, PermissionContext context) {
        delegate.setOption(key, value, storageRealm(context));
    }

    @Override
    public Map<String, String> options(PermissionContext context) {
        return delegate.getOptions(storageRealm(context));
    }

    @Override
    public Set<String> configuredWorlds() {
        var worlds = new LinkedHashSet<String>();
        for (String world : delegate.getWorlds()) {
            worlds.add(Worlds.fromMapKey(world));
        }
        return Set.copyOf(worlds);
    }

    @Override
    public Map<String, List<String>> permissionsByWorld() {
        var mapped = new LinkedHashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> entry : delegate.getAllPermissions().entrySet()) {
            mapped.put(Worlds.fromMapKey(entry.getKey()), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(mapped);
    }

    @Override
    public Map<String, List<String>> effectivePermissionsByWorld() {
        var worlds = new LinkedHashSet<String>();
        worlds.add(Worlds.GLOBAL);
        worlds.addAll(configuredWorlds());
        var mapped = new LinkedHashMap<String, List<String>>();
        for (String world : worlds) {
            mapped.put(world, effectivePermissions(world));
        }
        return Map.copyOf(mapped);
    }

    @Override
    public List<TimedPermissionEntry> timedPermissionEntries(PermissionContext context) {
        var legacyWorld = storageRealm(context);
        var apiWorld = Worlds.fromMapKey(legacyWorld == null ? "" : legacyWorld);
        var entries = new ArrayList<TimedPermissionEntry>();
        for (String permission : delegate.getTimedPermissions(legacyWorld)) {
            entries.add(new TimedPermissionEntry(
                    permission, apiWorld, delegate.getTimedPermissionLifetime(permission, legacyWorld)));
        }
        return List.copyOf(entries);
    }

    @Override
    public int timedPermissionRemainingSeconds(String permission, PermissionContext context) {
        return delegate.getTimedPermissionLifetime(permission, storageRealm(context));
    }

    @Override
    public void save() {
        delegate.save();
    }

    private String storageRealm(PermissionContext context) {
        return ContextPermissionEvaluator.storageRealm(context, manager.getPlatform());
    }
}
