---
layout: default
title: Context & Worlds
permalink: /advanced/context/
description: How world and server context affects permission checks.
---

Most of the time you only need to add `[world]` to a command. This page explains what that means.

## Global vs world-scoped

- **Global** — permission applies everywhere
- **World-scoped** — permission only applies in that world

```text
/pex group vip add essentials.fly          # all worlds
/pex group vip add essentials.fly world_nether   # nether only
```

## World inheritance

If `world_nether` inherits from `world`, players get `world`'s permissions plus any nether-specific ones.

```text
/pex world world_nether inherit world
```

## Checking in a specific world

```text
/pex user Steve check essentials.fly world_nether
/pex user Steve list world_nether
```

## When to use world context

| Situation | Approach |
|-----------|----------|
| Same permissions everywhere | Don't specify a world |
| Special permissions in one world | Add `[world]` to commands |
| Shared base across worlds | Set up world inheritance |

## Proxy servers

On BungeeCord or Velocity, permissions can also be scoped per backend server. For most single-server setups, world context is all you need.
