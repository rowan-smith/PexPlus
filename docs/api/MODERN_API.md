# Modern API reference

Package root: `dev.rono.permissions.api`  
Maven artifact: `permissionsex-api`

```xml
<dependency>
  <groupId>dev.rono.permissions</groupId>
  <artifactId>permissionsex-api</artifactId>
  <version>1.23.5</version>
  <scope>provided</scope>
</dependency>
```

Runtime: on **Spigot/Paper**, `PermissionService` is registered on Bukkit `ServicesManager`. On **Bungee/Waterfall**, it is not registered — use legacy `PermissionManager` or proxy integration.

Sample plugin: [`example-plugin/`](../../example-plugin/)

---

## Quick start

```java
import dev.rono.permissions.api.service.PermissionService;
import dev.rono.permissions.api.subject.User;
import org.bukkit.plugin.RegisteredServiceProvider;

RegisteredServiceProvider<PermissionService> reg =
        getServer().getServicesManager().getRegistration(PermissionService.class);
if (reg == null) {
    getLogger().warning("PermissionsEx not loaded");
    return;
}
PermissionService pex = reg.getProvider();

if (pex.has(player.getUniqueId(), "my.plugin.use", player.getWorld().getName())) {
    User user = pex.user(player.getUniqueId());
    user.inWorld(player.getWorld().getName()).addTimedPermission("my.plugin.temp", 3600);
    user.save();
}
```

---

## World model

Classic PEX uses `null` for the **global** namespace (permissions/options that apply everywhere unless overridden per world).

| Modern constant | Meaning |
|-----------------|---------|
| `Worlds.GLOBAL` | `null` — global namespace |
| Empty string `""` | Normalized to global when passed to API methods |

Helpers in `dev.rono.permissions.api.world.Worlds`: `normalize`, `isGlobal`, `mapKey`, `fromMapKey`.

---

## `PermissionService`

Entry point for server-wide operations.

### Introspection

| Method | Description |
|--------|-------------|
| `backend()` | `BackendInfo(type, simpleName, diagnosticLabel)` |
| `userCount()` / `groupCount()` | Registered subjects in active backend |
| `registeredUserNameCount()` / `registeredGroupCount()` | Aliases for counts above |
| `activeBackendSimpleName()` | Alias for `backend().simpleName()` |
| `worlds()` | Known realm/world names from platform |
| `isDebug()` | Debug mode flag |

### World inheritance

| Method | Description |
|--------|-------------|
| `worldInheritance(world)` | Parent worlds that inherit into `world` |
| `setWorldInheritance(world, parents)` | Set inheritance parents |
| `worldInheritanceMap()` | All mappings (`Worlds.GLOBAL` key = global) |
| `defaultGroups(world)` | Default groups for a world |
| `rankLadder(ladderName)` | `Map<rank, Group>` on a promotion ladder |

### Permission checks

| Method | Description |
|--------|-------------|
| `has(UUID, permission)` | Check in global context |
| `has(UUID, permission, world)` | Check in world |
| `has(name, permission[, world])` | Resolve user by name/UUID rules |

### Users

| Method | Description |
|--------|-------------|
| `findUser(identifier)` / `findUser(uuid)` | Optional lookup — **only persisted** users |
| `user(identifier)` / `user(uuid)` | Resolve or **materialize** (classic `getUser`) |
| `userIdentifiers()` | All identifiers in backend |
| `deleteUser(identifier)` | Remove from backend and cache |
| `usersInGroup(group, world, inherit)` | Members of a group |

### Groups

| Method | Description |
|--------|-------------|
| `findGroup(name)` | Optional — **only persisted** groups |
| `group(name)` | Resolve or materialize group |
| `groupNames()` | All group names |
| `deleteGroup(name)` | Remove from backend and cache |

### Maintenance

| Method | Description |
|--------|-------------|
| `reload()` | Reload backend; throws `PermissionsExException` on failure |

Source: `api/src/main/java/dev/rono/permissions/api/service/PermissionService.java`

---

## `PermissionSubject`

Shared by `User` and `Group`.

### Metadata

| Method | Description |
|--------|-------------|
| `type()` | `SubjectType.USER` or `GROUP` |
| `identifier()` / `name()` | Backend id and display name |
| `virtual()` | In-memory-only, not yet persisted |

### Permissions

| Method | Description |
|--------|-------------|
| `has(permission[, world])` | Effective check on this subject |
| `permissions([world])` | **Direct** assignments (not inherited) |
| `effectivePermissions([world])` | Merged after inheritance |
| `addPermission` / `removePermission` / `setPermissions` | Direct CRUD |
| `permissionsByWorld()` | Map of world → direct permissions |
| `effectivePermissionsByWorld()` | Map of world → effective permissions |
| `configuredWorlds()` | Worlds with any subject data |

### Timed permissions

| Method | Description |
|--------|-------------|
| `addTimedPermission(permission, [world,] seconds)` | Temporary grant |
| `removeTimedPermission(permission[, world])` | Remove timed node |
| `timedPermissions([world])` | Active timed permission names |
| `timedPermissionEntries([world])` | `TimedPermissionEntry(permission, world, remainingSeconds)` |
| `allTimedPermissionEntries()` | Across all configured worlds |
| `timedPermissionRemainingSeconds(permission[, world])` | Seconds until expiry; `0` if absent |
| `hasTimedPermission(permission[, world])` | Whether timed node is active |

### Meta (prefix / suffix / options)

| Method | Description |
|--------|-------------|
| `prefix` / `suffix` / `setPrefix` / `setSuffix` | Chat meta per world |
| `option(key[, world])` / `setOption` / `options` | Arbitrary key/value meta |

### World views

| Method | Description |
|--------|-------------|
| `inWorld(world)` | `SubjectWorldContext` — world-scoped facade |
| `global()` | Same as `inWorld(Worlds.GLOBAL)` |

### Persistence

| Method | Description |
|--------|-------------|
| `save()` | Persist to backend |
| `delete()` | Remove from backend and cache |

---

## `User`

Extends `PermissionSubject`.

| Method | Description |
|--------|-------------|
| `uniqueId()` | `Optional<UUID>` when identifier is UUID-shaped |
| `groups([world,] inherit)` | Group membership list |
| `inGroup(name[, world, inherit])` | Membership test |
| `addGroup(name[, world])` | Add to group |
| `addGroup(name, [world,] lifetimeSeconds)` | Timed membership |
| `removeGroup(name[, world])` | Remove from group |
| `timedGroupMemberships([world])` | `TimedGroupMembership(group, world, remainingSeconds)` |
| `allTimedGroupMemberships()` | Across all worlds |
| `groupMembershipRemainingSeconds(group[, world])` | Seconds until timed membership expires |
| `inWorld(world)` / `global()` | Returns `UserWorldContext` |

---

## `Group`

Extends `PermissionSubject`.

| Method | Description |
|--------|-------------|
| `weight()` / `setWeight(int)` | Sort order |
| `isDefault([world])` / `setDefault(bool[, world])` | Default group flag |
| `parents([world])` | Direct parent groups |
| `parentTree([world])` | Expanded ancestor groups |
| `addParent` / `removeParent` / `setParents` | Inheritance CRUD |
| `isChildOf(name[, world, inherit])` | Hierarchy test |
| `rank()` / `rankLadder()` / `setRank(rank, ladder)` | Promotion ladder |
| `memberIdentifiers([world])` | User ids with direct membership |
| `members([world])` | `List<User>` with direct membership |
| `activeMembers([inherit])` | Online members |
| `inWorld(world)` / `global()` | Returns `GroupWorldContext` |

---

## World context interfaces

Ergonomic world-scoped views (same operations without repeating `world` parameter):

| Type | Extends | Extra operations |
|------|---------|------------------|
| `SubjectWorldContext` | — | Permissions, timed perms, prefix/suffix, options |
| `UserWorldContext` | `SubjectWorldContext` | Groups, timed membership |
| `GroupWorldContext` | `SubjectWorldContext` | Parents, default flag, `isChildOf` |

Obtain via `subject.inWorld("world_nether")` or `user.global()`.

---

## Supporting types

| Type | Role |
|------|------|
| `BackendInfo` | Record: backend alias, implementation class name, label |
| `TimedPermissionEntry` | Record: permission, world, remainingSeconds |
| `TimedGroupMembership` | Record: groupName, world, remainingSeconds |
| `PermissionsExException` | Checked exception for reload/backend failures |
| `SubjectType` | `USER`, `GROUP` |

---

## Advanced SPI (`permissionsex-core-api`)

Not required for typical hook plugins. Used by platform modules and deep integration:

| Type | Role |
|------|------|
| `PlatformAdapter` | Host bridge (UUID/name, realms, event publish) |
| `PermissionDispatch` | `EntityDispatch` / `SystemDispatch` notifications |
| `EntityMutation` / `SystemMutation` | Change kinds |
| `SchedulerBridge` | Sync/async scheduling |
| `ContextResolver` | `realmFor(UUID)` |

On Spigot, internal dispatches are translated to legacy Bukkit events. See [LEGACY_API.md — Events](LEGACY_API.md#events).

---

## Planned additions

See [FUTURE.md](FUTURE.md) for event bus, promote/demote, diagnostics, backend switching, and other gaps.
