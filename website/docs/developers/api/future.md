---
title: API Roadmap
description: Planned API additions and known gaps.
slug: /developers/api/future
---
Policy: **legacy `ru.tehkode.*` stays frozen**. New capabilities belong under `dev.rono.permissions.api.*`.

---

## Implemented (modern API)

| Feature | API |
|---------|-----|
| Modern event bus | `api.getEventBus()` → `PermissionEventBus` |
| Player checks (Bukkit) | `getPermissionManager().has(player, "node")` or `User.inContext(...)` / holder context |
| Global permission checks | `api.getUserManager().getUser(id).has("node")` |
| Promote / demote | `LadderManager.promote/demote` — Modern: `/pex ladder <ladder> promote\|demote <user>`; classic: `/pex promote` |
| Hierarchy helpers | `Group.children` / `Group.descendants`, `Group.childIdentifiers`, `Group.members(world, inherit)` |
| Timed group removal | `User.removeTimedGroup(name[, context])` |
| Proxy `PermissionsExApi` | `dev.rono.permissions.bungee.PermissionsEx.getApi()` on Bungee/Waterfall |

---

## Still open

### Backend admin surface

Commands: `/pex backend list`, `switch`, `import`, `export`.

**Proposed:** `PermissionsExApi` backend helpers (`activate`, `createHandle`, `importFrom`, `exportData`) for programmatic admin.

### Import / export

**Proposed:** `importData(document, ImportMode)` and `exportData()` on a backend handle type.

### Flat convenience accessors

**Proposed:** shorthand `pex.user()`, `pex.world()`, `pex.users()`, `pex.groups()`, `pex.backend()` on `PermissionsExApi`.

### Batch edits

**Proposed:** batch `save()` on subjects or manager-level flush helpers.

### Async reload

**Proposed:** `reloadAsync()` → `CompletableFuture<Void>` on `PermissionsExApi`.

### Permission check diagnostics

Legacy: `getMatchingExpression`, `explainExpression`.

**Proposed:** `PermissionCheckResult` record on `PermissionSubject` or `PermissionsExApi`.

### Configuration read surface

Legacy: `PermissionManager.getConfiguration()`.

**Proposed:** immutable `PermissionService.configuration()` snapshot.

### Regex / matcher access

Legacy: `getPermissionMatcher()`.

**Proposed:** read-only `PermissionService.matcher()`.

### User cache control

Legacy: `resetUser`, `clearUserCache`, `cacheUser`.

**Proposed:** same operations on modern `PermissionsExApi`.

### Superperms bridge introspection

Spigot-only attachment/injection status for support plugins.

### Observability

Metrics hooks, structured logging adapters for large networks.

### Typed permission scope (holder bridge)

Today holder checks use `Map<String, String>` context while subject APIs use `String world`. Two parallel scoping systems can diverge.

**Proposed:** sealed scope type replacing null-world overloads on the holder bridge:

```java
sealed interface PermissionScope permits GlobalScope, WorldScope {}
record GlobalScope() implements PermissionScope {}
record WorldScope(String world) implements PermissionScope {}
```

Then: `hasPermission(holder, node, PermissionScope scope)` and `PermissionAddRequest` carrying `PermissionScope` instead of raw maps where possible. `PermissionContext.resolveWorld` remains for legacy map interop.

### PermissionManager overload consolidation

Deprecate holder `addPermission(holder, string)` / `addPermission(holder, string, duration)` in favor of `PermissionAddRequest` as the single write path. Document `remove` vs `clearUserCache` vs `resetUser` vs `reset()` in modern `PermissionsExApi` cache-control methods.

### Modern cache control on PermissionsExApi

Legacy: `resetUser`, `clearUserCache`, `cacheUser`.

**Proposed:** explicit modern equivalents with threading documented (`@MainThreadOnly` / `@AsyncSafe` in Javadoc).

---

## Contributing

When adding modern API methods:

1. Types in `permissionsex-api` (optional Bukkit/proxy helpers in separate modules).
2. Implementation in `dev.rono.permissions.core.api.*`.
3. Tests in `ModernApiTest` or focused test classes.
4. Update [MODERN_API.md](/developers/api/modern) and this file.
