# API roadmap and gaps

Features that exist in the **engine** or **legacy API** but are not yet on the modern surface, plus suggested additions for companion plugins.

Policy: **legacy `ru.tehkode.*` stays frozen**. New capabilities should be added under `dev.rono.permissions.api.*`.

---

## High priority (common hook-plugin needs)

### Modern permission events

Legacy plugins listen to `PermissionEntityEvent` and `PermissionSystemEvent` on Spigot. The modern API has internal bus dispatches (`EntityDispatch`, `SystemDispatch` in `permissionsex-core-api`) but **no public listener registration** for hook plugins yet.

**Proposed:** `PermissionService.events()` or `PermissionEventBus` with register/unregister for typed entity/system notifications, with Spigot adapter translating to Bukkit events for backward compatibility.

### Player-scoped checks on modern API

Legacy: `PermissionManager.has(Player, permission[, world])`.

Modern: `PermissionService.has(UUID, …)` — callers must pass UUID and world explicitly.

**Proposed:** `has(Player, permission)` / `has(Player, permission, world)` defaults on `PermissionService` (Spigot-only overloads in a small `permissionsex-api-bukkit` module, or core defaults when `Player` is not on classpath — likely a separate optional artifact).

### Rank promotion / demotion

Legacy `PermissionUser` exposes `promote(PermissionUser promoter, String ladder)` and `demote(...)`.

**Proposed:** `User.promote(String ladder)` / `User.demote(String ladder)` on modern API, throwing `PermissionsExException` / `RankingException` wrapper.

### Permission check diagnostics

Legacy entities expose `getMatchingExpression(permission, world)` and `explainExpression(expression)` for debug tooling.

**Proposed:** `PermissionCheckResult` record on modern API: `(boolean allowed, String matchedExpression, String subjectIdentifier)`.

### Backend administration

Legacy `PermissionManager` exposes `setBackend`, `createBackend`, and `getBackend()`.

Modern API exposes read-only `backend()` (`BackendInfo`).

**Proposed:** `PermissionService.setActiveBackend(String alias)` and optional `BackendHandle createBackend(String alias)` for migration/admin plugins (guarded — dangerous on live servers).

---

## Medium priority

### Configuration read surface

Legacy: `PermissionManager.getConfiguration()` → `PermissionsExConfig`.

**Proposed:** `PermissionService.configuration()` returning an immutable snapshot (debug flag, default backend alias, createUserRecords, allowOps, …) without exposing Bukkit `ConfigurationSection`.

### Regex / matcher access

Legacy: `getPermissionMatcher()` / `setPermissionMatcher(PermissionMatcher)`.

**Proposed:** `PermissionService.matcher()` read-only for plugins that need to test node patterns consistently with PEX.

### User cache control

Legacy: `resetUser`, `clearUserCache`, `cacheUser` for login pipelines.

**Proposed:** `PermissionService.resetUser(id)`, `clearUserCache(UUID)`, `cacheUser(id, fallbackName)` on modern API.

### Group listing / search helpers

Legacy: `getGroups(parentGroup, world, inherit)` for hierarchy walks.

**Proposed:** `PermissionService.childGroups(name, world, inherit)` and `PermissionService descendantGroups(...)`.

### Bulk / transactional edits

No first-class batch API today. Plugins loop `addPermission` + single `save()`.

**Proposed:** `SubjectEditSession` or `User#edit(Consumer<UserWorldContext>)` with one save at end; optional `PermissionService.runBatch(Runnable)`.

---

## Lower priority / platform-specific

| Gap | Notes |
|-----|--------|
| **Superperms bridge introspection** | Spigot-only: is a player's `Permissible` injected, attachment count — useful for support plugins |
| **Proxy `PermissionService` registration** | Bungee could register a slim service (checks only, no Bukkit types) |
| **Async reload** | `reload()` is synchronous; large SQL backends may need `reloadAsync` + callback |
| **Import/export** | YAML/JSON bulk user export for web panels — currently via backend internals |
| **Placeholder/meta providers** | Vault-style prefix/suffix resolution helpers (might belong in a separate chat bridge) |
| **Option schema validation** | Typed getters: `optionBoolean`, `optionInt` on `PermissionSubject` |

---

## Already covered (modern API)

Do not re-add via legacy patterns — use modern types instead:

- User/group CRUD and permission edits
- World-scoped permissions (`inWorld`, `permissionsByWorld`)
- Timed permissions and timed group membership metadata
- World inheritance (`worldInheritance`, `setWorldInheritance`)
- Default groups, rank ladders, group membership listing
- Prefix/suffix/options per world

---

## Contributing

When adding modern API methods:

1. Add types to `permissionsex-api` only (keep `permissionsex-core-api` for platform SPI).
2. Implement in `dev.rono.permissions.core.api.*` adapters — do not expand `ru.tehkode.*`.
3. Add tests in `permissionsex-core` (`ModernPermissionServiceTest` or similar).
4. Update [MODERN_API.md](MODERN_API.md) and this file.
