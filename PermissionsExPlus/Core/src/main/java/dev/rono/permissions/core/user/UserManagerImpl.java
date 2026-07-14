package dev.rono.permissions.core.user;

import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.api.user.UserAlreadyExistsException;
import dev.rono.permissions.api.user.UserManager;
import dev.rono.permissions.api.user.UserNotFoundException;
import dev.rono.permissions.core.cache.UserCache;
import dev.rono.permissions.core.permission.PermissionNodeImpl;
import dev.rono.permissions.core.permission.PermissionResolver;
import dev.rono.permissions.core.storage.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class UserManagerImpl implements UserManager {

    private final UserCache cache;
    private final UserRepository repository;
    private final PermissionResolver resolver;

    public UserManagerImpl(UserCache cache, UserRepository repository, PermissionResolver resolver) {
        this.cache = cache;
        this.repository = repository;
        this.resolver = resolver;
    }

    @Override
    public Optional<User> find(UUID uuid) {
        return Optional.ofNullable(cache.get(uuid));
    }

    @Override
    public User load(UUID uuid) {
        User user = repository.find(uuid).orElse(null);
        if (user == null) {
            throw new UserNotFoundException(uuid.toString());
        }
        cache.put(user);
        return user;
    }

    @Override
    public User create(UUID uuid) {
        if (cache.get(uuid) != null) {
            throw new UserAlreadyExistsException(uuid.toString());
        }
        User user = new UserImpl(uuid, null, resolver);
        repository.save(user);
        cache.put(user);
        return user;
    }

    @Override
    public void unload(UUID uuid) {
        cache.remove(uuid);
    }

    @Override
    public boolean loaded(UUID uuid) {
        return cache.get(uuid) != null;
    }

    @Override
    public void addPermission(User user, String permission) {
        user.addPermission(new PermissionNodeImpl(permission));
    }

    @Override
    public void removePermission(User user, String permission) {
        user.removePermission(permission, BuiltinRealm.GLOBAL);
    }

    @Override
    public Collection<User> all() {
        return repository.all();
    }
}
