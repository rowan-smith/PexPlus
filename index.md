---
layout: default
title: Getting Started
permalink: /
description: Install PermissionsExPlus and set up permissions on your Minecraft server.
---

PermissionsExPlus (PEX) is a permissions plugin for Minecraft servers. It lets you manage **users**, **groups**, **permissions**, **prefixes**, and **multi-world** setups using familiar `/pex` commands.

**Current version:** {{ site.version }}

## Install

1. Download **`PermissionsExPlus-{{ site.version }}.jar`** from [GitHub Releases](https://github.com/{{ site.repo }}/releases) or build from source.
2. Remove any old PermissionsEx jars from your `plugins/` folder.
3. Place the jar in `plugins/` and restart the server.
4. Use `/pex` in-game to confirm it loaded.

> **Java 21+** is required. Works on Spigot, Paper, BungeeCord, Velocity, and Sponge.

## Quick setup

Create an admin group and assign a player:

```text
/pex group admin create
/pex group admin add '*'
/pex user Steve group set admin
```

Add a permission to a player:

```text
/pex user Alex add essentials.home
```

Set a chat prefix on a group:

```text
/pex group moderator prefix [Mod]
```

## What you can do

- Assign permissions to **users** and **groups**
- Set up **group inheritance** (e.g. admin inherits moderator)
- Add **prefixes and suffixes** for chat plugins
- Grant **temporary permissions** that expire automatically
- Use **different permissions per world**
- **Promote and demote** players on rank ladders

## Where to go next

| Topic | Guide |
|-------|-------|
| Where data is stored | [Storage]({{ site.baseurl }}/storage/) |
| Config files | [Configuration]({{ site.baseurl }}/configuration/) |
| All commands | [Commands]({{ site.baseurl }}/commands/general/) |
| Common questions | [FAQ]({{ site.baseurl }}/faq/default-groups/) |
| Plugin developers | [Developers]({{ site.baseurl }}/developers/) |

## Need help?

- [Report issues on GitHub](https://github.com/{{ site.repo }}/issues)
- Use `/pex report` in-game to generate a diagnostic report
