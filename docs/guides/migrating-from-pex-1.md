# Migrating from PermissionsEx 1.23.4

PermissionsExPlus is designed to be a modern successor to PEX 1.x, but there are important differences.

## Key Differences

- **API**: The new API is asynchronous and uses immutable snapshots.
- **Contexts**: Realms are replaced by arbitrary contexts.
- **Storage**: The data model is updated for better performance and flexibility.

## How to Migrate

1. **Backup**: Always backup your existing `permissions.yml` or SQL database.
2. **Import**: Use the provided migration tools (if available) or manually import your data.
3. **Compatibility Adapter**: Use `PermissionsExApiAdapter` if you have other plugins that depend on the old PEX API.

## Legacy Realms

Old named-realms should be translated to `realm=name` contexts in PermissionsExPlus.
