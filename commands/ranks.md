---
layout: default
title: Rank Commands
permalink: /commands/ranks/
description: Promote and demote players on rank ladders in PermissionsExPlus.
---

Rank ladders let you move players between groups in order — useful for staff progression or RPG ranks.

## Setup a ladder

Assign ranks to groups on a named ladder (e.g. `staff`):

```text
/pex group trainee rank 1 staff
/pex group helper rank 2 staff
/pex group moderator rank 3 staff
/pex group admin rank 4 staff
```

Lower number = lower rank. Promoting moves the player to the next higher rank.

## Promote & demote

```text
/pex promote Steve staff
/pex demote Steve staff
```

**Shortcut commands** (no `/pex` prefix):

```text
/promote Steve
/demote Steve
```

These use the default ladder.

## View ranks

```text
/pex group moderator rank staff
```

## Example workflow

```text
/pex group trainee rank 1 staff
/pex group moderator rank 2 staff
/pex user NewMod group set trainee
/pex promote NewMod staff
```

NewMod moves from `trainee` to `moderator`.
