---
layout: default
title: API Usage
permalink: /developers/usage/
description: Code examples for PermissionsExPlus plugin integration.
---

## Check if PEX is loaded

```java
import ru.tehkode.permissions.bukkit.PermissionsEx;

if (!PermissionsEx.isAvailable()) {
    getLogger().warning("PermissionsEx is not loaded.");
    return;
}
```

## Modern API — get a user and check permissions

```java
var api = PermissionsEx.getApi();
var user = api.getUserManager().getUser(player.getUniqueId());

if (user.has("myplugin.use")) {
    player.sendMessage("You have access!");
}
```

## Modern API — grant a permission

```java
var user = PermissionsEx.getApi().getUserManager().getUser(player.getUniqueId());
user.addPermission("myplugin.bonus");
user.save();
```

## Modern API — world-scoped check

```java
var user = PermissionsEx.getApi().getUserManager().getUser(player.getUniqueId());
var worldCtx = user.inContext(
    dev.rono.permissions.api.permission.PermissionContext.of(player.getWorld().getName())
);
if (worldCtx.has("myplugin.nether-only")) {
    // ...
}
```

## Legacy API — classic style

```java
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

PermissionUser user = PermissionsEx.getUser(player);
boolean allowed = PermissionsEx.getPermissionManager()
    .has(player, "myplugin.use", player.getWorld().getName());
```

## Legacy API — listen for changes (Spigot)

```java
import ru.tehkode.permissions.events.PermissionEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@EventHandler
public void onPermissionChange(PermissionEntityEvent event) {
    getLogger().info("Permissions changed for: " + event.getIdentifier());
}
```

## Proxy servers (Bungee/Velocity)

Use `dev.rono.permissions.bungee.PermissionsEx.getApi()` on BungeeCord. Bukkit events are not available on proxies.

## Full reference

See [API Reference]({{ site.baseurl }}/developers/reference/) for complete Javadoc.
