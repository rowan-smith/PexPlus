---
layout: default
title: API Introduction
permalink: /developers/
description: Introduction to integrating with PermissionsExPlus from your plugin.
---

If you are building a **companion plugin** that reads or modifies permissions, PEX provides two APIs.

## Which API?

| Your situation | Use |
|----------------|-----|
| **New plugin** | Modern API (`PermissionsEx.getApi()`) |
| **Existing PEX plugin** | Legacy API — usually works without changes |
| **Listening to events on Spigot** | Legacy Bukkit events |

## Modern API (recommended)

Package: `dev.rono.permissions.api`

```xml
<dependency>
  <groupId>dev.rono.permissions</groupId>
  <artifactId>permissionsex-api</artifactId>
  <version>{{ site.version }}</version>
  <scope>provided</scope>
</dependency>
```

PEX must be on the server at runtime. Your plugin only needs this at **compile time** — do not bundle PEX in your jar.

## Legacy API

Package: `ru.tehkode.permissions`

For plugins written for original PermissionsEx 1.23.x:

```xml
<dependency>
  <groupId>dev.rono.permissions</groupId>
  <artifactId>permissionsex-legacy-api</artifactId>
  <version>{{ site.version }}</version>
  <scope>provided</scope>
</dependency>
```

Add `permissionsex-legacy-stub` if you use `PermissionsEx.getUser()` static methods.

## Sample plugins

Working examples are in the [GitHub repo](https://github.com/{{ site.repo }}/tree/main/plugin):

- `permissionsex-example-plugin` — modern API
- `permissionsex-example-legacy-plugin` — legacy API

## Next steps

- [API Usage]({{ site.baseurl }}/developers/usage/) — code examples
- [API Reference]({{ site.baseurl }}/developers/reference/) — Javadoc for all versions
