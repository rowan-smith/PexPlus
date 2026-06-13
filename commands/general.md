---
layout: default
title: General Commands
permalink: /commands/general/
description: General /pex commands for reload, config, backends, and debugging.
---

Most commands start with `/pex`. Standalone `/promote` and `/demote` are listed on the [Ranks]({{ site.baseurl }}/commands/ranks/) page.

## Help & info

| Command | What it does |
|---------|--------------|
| `/pex` | Show help |
| `/pex help [page]` | Show more help pages |

## Server management

| Command | What it does |
|---------|--------------|
| `/pex reload` | Reload config and permissions |
| `/pex report` | Generate a report for bug reports |
| `/pex config <node> [value]` | View or change a setting |
| `/pex toggle debug` | Turn debug logging on/off |

## Backends

| Command | What it does |
|---------|--------------|
| `/pex backend` | Show active backend |
| `/pex backend <name>` | Switch backend (e.g. `file`, `sql`) |
| `/pex import <backend>` | Import data from another backend |

## Data tools

| Command | What it does |
|---------|--------------|
| `/pex convert uuid` | Convert usernames to UUIDs |
| `/pex hierarchy [world]` | Show full permission tree |

## Permission node

| Node | Effect |
|------|--------|
| `permissionsex.disabled` | Disables regex permission matching for that player |
