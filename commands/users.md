---
layout: default
title: User Commands
permalink: /commands/users/
description: Manage player permissions and group membership with /pex user commands.
---

Replace `<user>` with a player name or UUID.

## View users

| Command | What it does |
|---------|--------------|
| `/pex users list` | List all users |
| `/pex user <user>` | Show user details |
| `/pex user <user> list [world]` | List permissions |
| `/pex user <user> check <perm> [world]` | Test if user has a permission |

## Permissions

| Command | What it does |
|---------|--------------|
| `/pex user <user> add <perm> [world]` | Grant a permission |
| `/pex user <user> remove <perm> [world]` | Remove a permission |
| `/pex user <user> timed add <perm> <time> [world]` | Temporary permission |
| `/pex user <user> timed remove <perm> [world]` | Remove timed permission |

**Time format:** `30s`, `5m`, `2h`, `7d`

## Groups

| Command | What it does |
|---------|--------------|
| `/pex user <user> group list [world]` | Show groups |
| `/pex user <user> group add <group> [world] [time]` | Add to a group |
| `/pex user <user> group set <group> [world]` | Set primary group |
| `/pex user <user> group remove <group> [world]` | Remove from group |

## Chat meta

| Command | What it does |
|---------|--------------|
| `/pex user <user> prefix [prefix] [world]` | Get or set prefix |
| `/pex user <user> suffix [suffix] [world]` | Get or set suffix |

See [Prefix, Suffix & Meta]({{ site.baseurl }}/advanced/meta/) for more.

## Other

| Command | What it does |
|---------|--------------|
| `/pex user <user> delete` | Delete user record |
| `/pex users cleanup <group> [days]` | Remove inactive users |
| `/pex user <user> toggle debug` | Debug for one user |

## Examples

```text
/pex user Steve add essentials.home
/pex user Steve group set admin
/pex user Alex timed add essentials.fly 7d
/pex user Steve prefix &b[VIP]
```
