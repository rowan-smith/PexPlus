---
sidebar_position: 2
---

# Command Mapping

This page provides a mapping between legacy PermissionsEx 1.x commands and the new PermissionsExPlus command structure.

## General Command Patterns

In PermissionsExPlus, the command structure is more explicit and hierarchical.

| Legacy Command Pattern | PermissionsExPlus Pattern |
| :--- | :--- |
| `/pex user <user> ...` | `/pex user <user> ...` |
| `/pex group <group> ...` | `/pex group <group> ...` |
| `/pex reload` | `/pex reload` |
| `/pex promote <user>` | `/pex promote <user>` |

## User Commands

| Legacy Command | PermissionsExPlus Command | Description |
| :--- | :--- | :--- |
| `/pex user <user> add <perm>` | `/pex user <user> permission add <perm>` | Add a permission to a user. |
| `/pex user <user> remove <perm>` | `/pex user <user> permission remove <perm>` | Remove a permission from a user. |
| `/pex user <user> timed add <perm> <seconds>` | `/pex user <user> permission add <perm> expiry=<seconds>` | Add a timed permission. |
| `/pex user <user> group add <group>` | `/pex user <user> group add <group>` | Add a user to a group. |
| `/pex user <user> group set <group>` | `/pex user <user> group set <group>` | Set a user's primary group. |
| `/pex user <user> prefix <prefix>` | `/pex user <user> option set prefix <prefix>` | Set user prefix. |
| `/pex user <user> suffix <suffix>` | `/pex user <user> option set suffix <suffix>` | Set user suffix. |

## Group Commands

| Legacy Command | PermissionsExPlus Command | Description |
| :--- | :--- | :--- |
| `/pex group <group> add <perm>` | `/pex group <group> permission add <perm>` | Add a permission to a group. |
| `/pex group <group> remove <perm>` | `/pex group <group> permission remove <perm>` | Remove a permission from a group. |
| `/pex group <group> parents add <parent>` | `/pex group <group> parent add <parent>` | Add a parent group. |
| `/pex group <group> weight <value>` | `/pex group <group> option set weight <value>` | Set group weight. |
| `/pex group <group> prefix <prefix>` | `/pex group <group> option set prefix <prefix>` | Set group prefix. |

## Contextual Commands

Legacy PEX used worlds for context. PEX+ uses arbitrary flags for context.

| Legacy Context | PermissionsExPlus Context |
| :--- | :--- |
| `world` | `--world <world>` |
| `[world]` (optional arg) | `--world <world>` |

Example:
- Legacy: `/pex group admin add essentials.fly world`
- PEX+: `/pex group admin permission add essentials.fly --world world`
