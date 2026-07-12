package dev.rono.permissions.api.realm;

import java.util.Optional;
import java.util.function.Predicate;

public interface RealmManager {

    Optional<PermissionRealm> find(String name);

    PermissionRealm load(String name);

    PermissionRealm create(String name);

    boolean exists(String name);

    int count();

    int count(Predicate<PermissionRealm> filter);

}
