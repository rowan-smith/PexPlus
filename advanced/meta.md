---
layout: default
title: Prefix, Suffix & Meta
permalink: /advanced/meta/
description: Chat prefixes, suffixes, and custom options in PermissionsExPlus.
---

PEX integrates with chat plugins (like EssentialsChat or LuckPerms-format bridges) via **prefixes**, **suffixes**, and **options**.

## Prefix & suffix

Set on a group:

```text
/pex group admin prefix &c[Admin]
/pex group admin suffix &7»
/pex group vip prefix &6[VIP]
```

Set on a user (overrides group):

```text
/pex user Steve prefix &b[Builder]
```

Use Minecraft colour codes with `&` (e.g. `&c` = red, `&6` = gold).

## View current values

```text
/pex group admin prefix
/pex user Steve prefix
```

## Options

Options are custom key-value pairs stored on users or groups:

```text
/pex user Steve set name "Builder Steve"
/pex user Steve get name
/pex group vip set priority 10
```

## Weight & prefixes together

When a player is in multiple groups, **weight** decides which prefix shows. Set higher weight on the group whose prefix should display:

```text
/pex group admin weight 100
/pex group admin prefix &c[Admin]
/pex group vip weight 10
/pex group vip prefix &6[VIP]
```

A player in both groups shows the admin prefix.

## In permissions.yml

```yaml
groups:
  admin:
    prefix: '&c[Admin] '
    suffix: '&7» '
    options:
      custom-tag: staff
```
