---
layout: default
title: Group Commands
permalink: /commands/groups/
description: Create and manage permission groups with /pex group commands.
---

Groups are the main way to organise permissions. Players inherit permissions from the groups they belong to.

## View groups

| Command | What it does |
|---------|--------------|
| `/pex groups list [world]` | List all groups |
| `/pex group <group>` | Show group details |
| `/pex group <group> list [world]` | List permissions |
| `/pex group <group> users` | List members |

## Create & delete

| Command | What it does |
|---------|--------------|
| `/pex group <group> create [parents]` | Create a group |
| `/pex group <group> delete` | Delete a group |

## Permissions

| Command | What it does |
|---------|--------------|
| `/pex group <group> add <perm> [world]` | Grant permission |
| `/pex group <group> remove <perm> [world]` | Remove permission |
| `/pex group <group> timed add <perm> <time> [world]` | Temporary permission |
| `/pex group <group> timed remove <perm> [world]` | Remove timed permission |

## Inheritance

| Command | What it does |
|---------|--------------|
| `/pex group <group> parents [world]` | Show parent groups |
| `/pex group <group> parents add <parents> [world]` | Add parents |
| `/pex group <group> parents set <parents> [world]` | Replace parents |
| `/pex group <group> parents remove <parents> [world]` | Remove parents |

## Examples

```text
/pex group moderator create default
/pex group admin create moderator
/pex group admin add permissions.*
/pex group admin add '*'
/pex group admin prefix &c[Admin]
```

This creates a chain: **admin** inherits from **moderator**, which inherits from **default**.
