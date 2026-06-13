---
layout: default
title: Permission Commands
permalink: /commands/permissions/
description: How permissions work in PermissionsExPlus and how to assign them.
---

Permissions are the nodes that control what players can do — like `essentials.home` or `worldedit.*`.

## Assigning permissions

**To a group** (recommended — applies to all members):

```text
/pex group vip add essentials.fly
/pex group admin add '*'
```

**To a user** (overrides or adds to group permissions):

```text
/pex user Steve add essentials.home
```

## Checking permissions

```text
/pex user Steve check essentials.home
/pex user Steve list
```

## Removing permissions

```text
/pex group vip remove essentials.fly
/pex user Steve remove essentials.home
```

## Temporary permissions

```text
/pex user Steve timed add essentials.fly 7d
/pex group event timed add modifyworld.* 2h
```

## Permission patterns

| Pattern | Meaning |
|---------|---------|
| `essentials.home` | One specific permission |
| `essentials.*` | All essentials permissions |
| `*` | Everything (admin) |
| `-essentials.ban` | Explicitly deny (negation) |

## Wildcards & regex

PEX supports wildcards (`*`) and regex patterns. Players with the node `permissionsex.disabled` will not have regex matching applied.

## Best practice

1. Put common permissions on **groups**
2. Add users to groups with `/pex user <name> group add <group>`
3. Only add direct user permissions for exceptions
