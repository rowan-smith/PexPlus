---
sidebar_position: 6
---

# World Commands

PermissionsExPlus handles world-specific permissions through context flags rather than dedicated world commands.

## Using World Context

World-scoped permissions are applied using the `--world` flag with user and group commands:

### User commands with world context

```bash
# Add a permission in a specific world
/pex user <user> permission add <permission> --world <world>

# Remove a permission in a specific world
/pex user <user> permission remove <permission> --world <world>

# List permissions in a specific world
/pex user <user> permissions list --world <world>

# Add user to a group in a specific world
/pex user <user> groups add <group> --world <world>
```

### Group commands with world context

```bash
# Add a permission in a specific world
/pex group <group> permission add <permission> --world <world>

# Set group prefix in a specific world
/pex group <group> options set prefix <prefix> --world <world>

# Add parent group in a specific world
/pex group <group> parents add <parent> --world <world>
```

## Legacy World Commands

The legacy PermissionsEx 1.x `/pex world` commands for managing world inheritance are not supported in PermissionsExPlus. World inheritance is now handled through the context system.
