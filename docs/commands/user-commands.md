---
sidebar_position: 3
---

# User Commands

Commands for managing individual users.

## Basic Management

- `/pex user <user> list`: List user details.
- `/pex user <user> check <permission>`: Check if a user has a permission.

## Permissions

- `/pex user <user> permission add <permission> [--world <world>] [--server <server>]`: Add a permission.
- `/pex user <user> permission remove <permission> [--world <world>] [--server <server>]`: Remove a permission.

## Groups

- `/pex user <user> group add <group>`: Add user to a group.
- `/pex user <user> group remove <group>`: Remove user from a group.
- `/pex user <user> group set <group>`: Set user's primary group.

## Options

- `/pex user <user> option set <key> <value>`: Set an option.
- `/pex user <user> option remove <key>`: Remove an option.
