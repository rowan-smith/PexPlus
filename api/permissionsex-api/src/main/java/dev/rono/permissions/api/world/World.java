package dev.rono.permissions.api.world;

import dev.rono.permissions.api.realm.Realm;

/**
 * Registered permission namespace (realm/world).
 *
 * @deprecated Use {@link Realm} and {@link dev.rono.permissions.api.realm.RealmManager} — {@code World}
 *     is the legacy name for a permission realm.
 */
@Deprecated(since = "3.0.0")
public interface World extends Realm {

    @Override
    String getName();

    @Override
    default String name() {
        return getName();
    }
}
