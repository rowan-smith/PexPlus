# API roadmap and gaps

Policy: **legacy `ru.tehkode.*` stays frozen**. New capabilities belong under `dev.rono.permissions.api.*`.

---

## Implemented (modern API)

| Feature | API |
|---------|-----|
| Modern event bus | `pex.events()` → `PermissionEventBus` |
| Player checks (Bukkit) | `getPermissionManager().has(player, "node")` or `User.inWorld()` / holder context |
| Global permission checks | `pex.user(id).hasPermission("node")` |
| Promote / demote | `User.promote` / `User.demote` (+ `RankingException`) |
| Backend admin | `pex.backend().activate`, `createHandle`, `importFrom` |
| Import / export | `pex.backend().exportData`, `importData(document, ImportMode)` |
| Hierarchy helpers | `Group.children` / `Group.descendants`, `Group.members(world, inherit)` |
| Flat API | `pex.user()`, `pex.world()`, `pex.users()`, `pex.groups()`, `pex.backend()` |
| Batch edits | batch `save()` on subjects |
| Async reload | `pex.reloadAsync()` → `CompletableFuture<Void>` |
| Proxy `PermissionsExApi` | `PermissionsExPlus` on Bungee/Waterfall |

---

## Still open

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

---

## Contributing

When adding modern API methods:

1. Types in `permissionsex-api` (optional Bukkit/proxy helpers in separate modules).
2. Implementation in `dev.rono.permissions.core.api.*`.
3. Tests in `ModernApiTest` or focused test classes.
4. Update [MODERN_API.md](MODERN_API.md) and this file.
