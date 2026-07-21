# Command Adapter

The `PermissionsExCommandAdapter` restores the original command structure of PermissionsEx 1.x.

## Purpose

For users who prefer the original PEX command syntax, this adapter provides a familiar interface while using the PermissionsExPlus engine under the hood.

## Usage

When enabled, it intercepts legacy commands and routes them through the modern API manager. This ensures that even legacy commands result in atomic, persistent changes consistent with the new model.
