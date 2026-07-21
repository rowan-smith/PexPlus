# API Adapter

The `PermissionsExApiAdapter` provides binary compatibility with the legacy PermissionsEx 1.x API.

## Purpose

Plugins written for PermissionsEx 1.23.4 can continue to function on a server running PermissionsExPlus by using this adapter.

## Implementation Details

- It wraps the modern `PexApi` and translates calls back and forth.
- Supports `PermissionUser`, `PermissionGroup`, and `PermissionManager` legacy interfaces.
- Some niche v1 events may not be fully reproduced.
