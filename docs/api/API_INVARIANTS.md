# API invariants and layering

This document codifies the permission API architecture: which entry points exist, how they relate, and the invariants that keep the system stable as it evolves.

Policy: **new hook plugins use `PermissionsExApi`**. Legacy `ru.tehkode.*` stays frozen. See [README.md](README.md) and [ARCHITECTURE.md](../../ARCHITECTURE.md).

---

## Primary API

| Layer | Type | Role |
|-------|------|------|
| **Primary** | `PermissionsExApi` | Entry point for new plugins — managers, events, holder bridge accessor |
| **Convenience** | `User` / `Group` (`PermissionSubject`) | Subject-centric CRUD and checks obtained from managers |
| **Legacy bridge** | `PermissionManager` | Frozen classic API + holder-based operations for interop |
| **Internal engine** | `DefaultPermissionManager`, `PermissionEntity`, `HierarchyTraverser` | Resolution, persistence, caches — not for hook plugins |

```java
// Correct for new plugins
var api = PermissionsEx.getApi();
var user = api.getUserManager().getUser(player.getUniqueId());
if (user.hasPermission("my.plugin.use")) { ... }

// Holder + rich context (still via primary API)
api.getPermissionManager().hasPermission(
    user.asHolder(), "my.plugin.use", PermissionContext.of(world, server, region, gamemode));

// Legacy interop only — do not use in new code
PermissionManager classic = api.getPermissionManager();
PermissionUser legacyUser = classic.getUser(player.getName());
```

All three paths above resolve to the **same in-memory entities** in `DefaultPermissionManager`. The difference is surface area and intent, not data.

---

## Core invariants

These rules must hold across refactors:

| Invariant | Meaning |
|-----------|---------|
| **Mutation through managers** | Create/load subjects via `UserManager` / `GroupManager`. Persist with `subject.save()`. Do not construct engine entities from hook plugins. |
| **Evaluation through subject / engine** | Permission checks and effective lists delegate to `PermissionEntity` + `HierarchyTraverser`. API adapters must not reimplement resolution. |
| **World context = thin projection** | `SubjectWorldContext` binds a world and delegates to `PermissionSubject`. No business logic in `SubjectWorldContexts`. |
| **Unified expiry** | Timed permissions and timed group memberships use different storage but share one scheduler: `TimedExpiryCoordinator`. |

Violating these produces drift bugs (especially around timed expiry and inheritance).

---

## Three overlapping entry points (by design)

```
PermissionsExApi  ──►  managers  ──►  UserImpl / GroupImpl  ──►  PermissionEntity
       │
       └──► getPermissionManager()  ──►  HolderPermissionService  ──►  PermissionEntity
```

| Entry | When to use |
|-------|-------------|
| `api.getUserManager().getUser(uuid)` | Normal subject operations |
| `user.inWorld("world").addPermission(...)` | World-bound convenience (thin facade) |
| `api.getPermissionManager().hasPermission(holder, node, context)` | Rich `PermissionContext` checks or holder-centric plugins |
| `manager.getUser(name)` | Legacy plugins only |

Long-term, only the first row is “correct” for new code. The others remain for compatibility and specialized holder/context flows.

---

## PermissionSubject role split

The public type `PermissionSubject` is large by necessity (users and groups share one surface). Internally it composes three narrower roles:

| Role | Interface | Responsibility |
|------|-----------|----------------|
| Identity | `SubjectIdentity` | `type`, `identifier`, `name`, `virtual` |
| Evaluation | `PermissionView` | `has`, effective/direct lists, prefix/suffix/options reads, timed reads |
| Mutation | `PermissionMutator` | add/remove/set permissions, meta writes, `save`, `delete` |

Implementations (`AbstractPermissionSubjectAdapter`) should treat evaluation and mutation as separate concerns even though callers see one interface. Tests and mocks can depend on `PermissionView` alone when writes are not needed.

`SubjectWorldContext` is the world-bound projection of `PermissionView` + `PermissionMutator` methods (still pure delegation).

---

## Timed permissions and timed groups

Two parallel **grant models**, one **expiry engine**:

| System | Storage | Purpose |
|--------|---------|---------|
| Timed permissions | `AbstractPermissionEntity.timedPermissions` + epoch map | Direct temporary grants |
| Timed groups | User option `group-{name}-until` | Temporary structural inheritance |
| Holder add result | `PermissionNode.expiresAt()` | Write receipt after holder-based add |

Read models for hook plugins:

- `TimedPermissionEntry` — permission + world + remaining seconds
- `TimedGroupMembership` — group + world + remaining seconds

**Rule:** registering or sweeping expiry always goes through `TimedExpiryCoordinator.notifyEarliestExpiry` / `runSweep`. New timed features must hook into that path, not spawn separate timers.

---

## WorldContext constraints

`SubjectWorldContexts` (~470 lines) is intentionally repetitive: each method forwards `subject.method(normalizedWorld)`.

Allowed:

- World normalization via `Worlds.normalize`
- Binding `world()` on the returned context
- Type-specific wrappers (`UserWorldContext`, `GroupWorldContext`) that add group/member operations

Not allowed:

- Permission resolution or inheritance logic
- Timed expiry calculations
- Backend I/O beyond what the subject already performs
- Caching effective results at the context layer

---

## Engine layer (not `PermissionService`)

There is no public `PermissionService` type today. The engine bridge consists of:

| Component | Role |
|-----------|------|
| `DefaultPermissionManager` | Central cache, backend, resolution entry |
| `InternalPermissionManager` | Runtime hooks (platform, events, timer) |
| `HolderPermissionService` | Holder → entity bridge |
| `AbstractPermissionEntity` | Storage + `has()` entry |
| `HierarchyTraverser` | World → inheritance → global DFS |
| `TimedExpiryCoordinator` | Unified timed expiry |

Future read-only configuration/matcher surfaces may adopt the `PermissionService` name (see [FUTURE.md](FUTURE.md)); until then, refer to these types in implementation docs.

---

## Contributing checklist

When adding API or engine features:

1. New public types → `permissionsex-api` under `dev.rono.permissions.api.*`
2. Implementation → `dev.rono.permissions.core.api.*` or core engine
3. Preserve the four invariants above
4. Route timed expiry through `TimedExpiryCoordinator`
5. Keep `SubjectWorldContexts` as delegation-only
6. Update [MODERN_API.md](MODERN_API.md) and this file when behavior or layering changes
