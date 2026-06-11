# PermissionsExPlus Architecture

## Module stack

```
permissionsex-core-api   Platform-neutral SPI (PlatformAdapter, bus dispatches)
permissionsex-api        Thin PermissionService façade
permissionsex-legacy-api Classic ru.tehkode.permissions compile surface + Bukkit events
permissionsex-core       Engine (manager, backends, commands, hierarchy)
permissionsex-spigot   Bukkit/Paper bootstrap, superperms bridge, platform adapters
permissionsex-bungee   Proxy bootstrap and permission bridge
permissionsex-bootstrap Universal shaded jar (plugin.yml + bungee.yml)
```

Dependency direction: platform modules → core → legacy-api / api → core-api.

## Namespace policy

| Package | Role |
|---------|------|
| `ru.tehkode.permissions.*` | Stable legacy API for hook plugins (interfaces, events, backend aliases) |
| `dev.rono.permissions.core.*` | Implementation (manager, backends, commands) |
| `dev.rono.permissions.api.*` | Modern minimal integration SPI |
| `dev.rono.permissions.spigot.*` / `bungee.*` | Platform-specific wiring |

New implementation code belongs under `dev.rono`. Public contracts consumed by third-party plugins remain under `ru.tehkode` as thin types delegating to core where needed.

## Permission resolution

1. `DefaultPermissionManager.getUser()` resolves UUID-first, then name / offline records.
2. `HierarchyTraverser` walks entity parents depth-first (world → world inheritance → global).
3. `AbstractPermissionEntity.getPermissionsInternal()` merges own + timed permissions from hierarchy.
4. `getMatchingExpression()` applies first regex match in list order.
5. `RegExpMatcher` compiles patterns with a bounded Guava cache.

### Spigot runtime

Two paths coexist:

- **Direct API**: `PermissionManager.has()` / `PermissionUser.has()`
- **Superperms bridge**: `SuperpermsListener` materializes PEX data into synthetic Bukkit permissions; `PermissiblePEX` regex-matches on `hasPermission()` with a per-player cache

Bus dispatches (`EntityDispatch`, `SystemDispatch`) are translated to legacy `ru.tehkode.permissions.events.*` Bukkit events on game servers via `SpigotEventPublisher`.

### Bungee runtime

`BungeePexPermissionBridge` handles `PermissionCheckEvent` using direct expression matching. No Bukkit events are published on proxy.

## Backends

| Alias | Implementation | Registered in |
|-------|----------------|---------------|
| `file` | `YamlFileBackend` (core), wrapped as `ru.tehkode...FileBackend` / `BungeeFileBackend` | Spigot / Bungee plugin constructors |
| `memory` | Platform memory backends | Spigot / Bungee |
| `sql` | `SQLBackend` | `CorePermissionBackendRegistrar` |
| `multi` | `MultiBackend` | `CorePermissionBackendRegistrar` |

YAML file I/O is unified in `dev.rono.permissions.core.backends.file` (SnakeYAML). Platform wrappers only supply classpath-stable legacy type names.

## Performance

- **Group membership index**: `GroupMembershipIndex` backs `getUsers(group)` without scanning every user.
- **SQL name caches**: entity identifiers and display names cached with `AtomicReference<ImmutableSet>`.
- **User entity caches**: per-user permission / option / prefix caches in `DefaultPermissionUser`.
- **Selective superperms updates**: permission/metadata changes clear `PermissiblePEX` regex cache without full permissible rebuild when injected.

## Commands

Cloud Command Framework (shared annotations in core) with platform command managers:

- Spigot: `StrippingBukkitCommandManager` + Brigadier when available
- Bungee: `StrippingBungeeCommandManager`

Business logic lives in `CoreCommandService`.

## Reload contract

`/pex reload` → `DefaultPermissionManager.reset()` clears in-memory users/groups, marks group index dirty, reloads backend, fires `RELOADED` system dispatch / Bukkit event on game servers.
