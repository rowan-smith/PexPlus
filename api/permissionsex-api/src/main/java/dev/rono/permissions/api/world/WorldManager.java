package dev.rono.permissions.api.world;

import dev.rono.permissions.api.realm.RealmManager;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * World registry with explicit find/get/create/exists lifecycle.
 *
 * @deprecated Use {@link RealmManager} — {@code WorldManager} is the legacy name for the realm registry.
 */
@Deprecated(since = "3.0.0")
public interface WorldManager extends RealmManager {

    /**
     * @deprecated Use {@link #findRealm(String)}
     */
    @Deprecated(since = "3.0.0")
    default Optional<World> findWorld(String name) {
        return findRealm(name).map(World.class::cast);
    }

    /**
     * @deprecated Use {@link #getRealm(String)}
     */
    @Deprecated(since = "3.0.0")
    default World getWorld(String name) throws WorldNotFoundException {
        try {
            return (World) getRealm(name);
        } catch (dev.rono.permissions.api.realm.RealmNotFoundException ex) {
            throw new WorldNotFoundException(name);
        }
    }

    /**
     * @deprecated Use {@link #createRealm(String)}
     */
    @Deprecated(since = "3.0.0")
    default World createWorld(String name) throws WorldAlreadyExistsException {
        try {
            return (World) createRealm(name);
        } catch (dev.rono.permissions.api.realm.RealmAlreadyExistsException ex) {
            throw new WorldAlreadyExistsException(name);
        }
    }

    /**
     * @deprecated Use {@link #listRealmNames()}
     */
    @Deprecated(since = "3.0.0")
    default java.util.List<String> listWorldNames() {
        return listRealmNames();
    }

    /**
     * @deprecated Use {@link #listRealms()}
     */
    @Deprecated(since = "3.0.0")
    default java.util.List<World> listWorlds() {
        return listRealms().stream().map(World.class::cast).collect(Collectors.toUnmodifiableList());
    }
}
