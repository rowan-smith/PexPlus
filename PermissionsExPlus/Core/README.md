# PermissionsExPlus Core

Core implements the permissions domain, Cloud command registration, event bus, caching, configuration, identity resolution, and storage backends. It is independent of Bukkit and Paper APIs and is driven through the `Platform` bridge.

## Storage

Core reads `database.yml` and stores local data beneath the plugin `data/` directory:

- H2: `data/<filename>.mv.db`
- SQLite: `data/<filename>.db`
- YAML/JSON: `data/users`, `data/groups`, `data/ladders`, and `data/realms`

Writes are queued on the platform's async scheduler and flushed before storage closes.

## General settings

Core reads `config.yml` for default-group assignment, wildcard/case behavior, expiring-node scans, UUID resolution mode, cache preferences, messaging configuration, and debug preferences.

Temporary permission nodes are removed by the configured async expiry scan. Group memberships do not currently carry expiry data.

## Platform bridge

To embed Core on another platform, implement `Platform`: it supplies logging, a scheduler, configuration/resource access, sender/message handling, and a Cloud command manager.
