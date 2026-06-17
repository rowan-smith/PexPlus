---
title: Context & Realms
description: Permission scopes (world, server, realm) in PermissionsExPlus.
slug: /concepts/context
---

**Context** is the scope a permission applies to. On a single game server, context usually means a **realm** keyed by **world** name. On a proxy network, context often means a **backend server** id.

| Concept | What it is | Plugin API |
|---------|------------|------------|
| **Realm** | Stored permission namespace in the backend | `RealmManager`, `Realm`, `Realms` |
| **Context** | Scope used when checking or mutating permissions | `PermissionContext`, `user.inContext(...)` |

Use **`PermissionContext`** for day-to-day scoped checks and grants. Use **`RealmManager`** to register realms and configure inheritance chains.

Full recipes: [API Cookbook](/developers/cookbook).

---

## Global vs world-scoped

| Scope | When to use | Example |
|-------|-------------|---------|
| **Global** (no realm) | Permission applies everywhere | `essentials.home` |
| **World-scoped** | Permission only in one world | `essentials.fly` in `world_nether` only |

```text
/pex group vip add essentials.fly
/pex group vip add essentials.fly world_nether
```

The first grants fly everywhere. The second grants fly **only** in the Nether.

**API:**

```java
import dev.rono.permissions.api.permission.PermissionContext;

user.global().addPermission("essentials.home");
user.inContext(PermissionContext.world("world_nether")).addPermission("essentials.fly");
user.save();
```

---

## Realm inheritance

Child realms can inherit a parent's permissions. This is **namespace inheritance** (which worlds/servers share a base ruleset), not group parent trees.

```text
/pex world world_nether inherit world
/pex world world_the_end inherit world
```

```text
/pex group builder add worldedit.* world
/pex group builder add worldedit.navigator world_nether
```

**API:**

```java
var nether = api.getRealmManager().getRealm("world_nether");
nether.addParent("world");
nether.parentTree(); // ancestor realm names
```

See [Realm inheritance (admin)](/developers/cookbook/#realm-inheritance-admin) in the cookbook.

---

## Checking in context

Always specify the world when testing world-scoped permissions:

```text
/pex user Steve check essentials.fly
/pex user Steve check essentials.fly world_nether
/pex user Steve list world_nether
```

A player might have fly in the Nether but not in the overworld — always check the right context.

**API:**

```java
var world = player.getWorld().getName();
boolean flyHere = user.inContext(PermissionContext.world(world)).has("essentials.fly");
```

---

## Server context (proxy networks)

On BungeeCord or Waterfall, permissions can be scoped per backend server. The backend stores these under the same realm namespace as worlds; proxy plugins should scope with `PermissionContext.server(id)`.

```text
/pex server lobby user Steve add network.vip
/pex server lobby user Steve check network.vip
```

**API:**

```java
user.inContext(PermissionContext.server("lobby")).addPermission("network.vip");
if (user.inContext(PermissionContext.server("lobby")).has("network.vip")) { ... }
```

See [Server-scoped permissions](/developers/cookbook/#server-scoped-permissions-proxy) in the cookbook.

---

## Combined context

For minigames, regions, or gamemode-specific rules, combine context keys:

```java
var ctx = PermissionContext.of("arena", "survival-1", "red-base", "ADVENTURE");
// world, server, region, gamemode
user.inContext(ctx).has("minigame.capture");
```

---

## Default group per world

Different worlds can have different default groups:

```text
/pex set default group default true
/pex set default group survival true world_survival
```

New players in `world_survival` join the `survival` group automatically.

---

## Example data structure

Representative YAML layout (used for import/migration; default storage is H2):

```yaml
groups:
  vip:
    permissions:
      - essentials.fly
    worlds:
      world_nether:
        permissions:
          - essentials.godmode
```

The `worlds:` key in YAML maps to **realm** namespaces in the modern API.

---

## Related

- [API Cookbook](/developers/cookbook) — world, server, realm, and timed-grant recipes
- [Modern API — Realm model](/developers/api/modern#realm-model)
- [World commands](/commands/worlds/)
