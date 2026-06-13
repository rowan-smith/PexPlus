---
layout: default
title: World Commands
permalink: /commands/worlds/
description: Multi-world permissions and world inheritance in PermissionsExPlus.
---

PEX supports different permissions per world. Add `[world]` to most commands to scope them.

## World commands

| Command | What it does |
|---------|--------------|
| `/pex worlds` | List worlds |
| `/pex world <world>` | Show world info |
| `/pex world <world> inherit <parents>` | Set world inheritance |

## Per-world permissions

Add a world name at the end of a command:

```text
/pex group vip add essentials.fly world_nether
/pex user Steve add modifyworld.world_nether world_nether
```

## World inheritance

Make one world inherit permissions from another:

```text
/pex world world_nether inherit world
```

Now `world_nether` uses `world`'s permissions as a base, plus any world-specific overrides.

## Default group per world

```text
/pex default group world_nether
/pex set default group default true world_nether
```

See [Context & Worlds]({{ site.baseurl }}/advanced/context/) for how world scoping works behind the scenes.
