---
title: Migrating from Version 1
description: Upgrade from PermissionsExPlus 1.x (1.23.x) to 3.0.0 — changes, compatibility, and step-by-step guide.
slug: /faq/migrate-from-v1
---

PermissionsExPlus **3.0.0** is a major release. If you are running any **1.x** build (the `1.23.x` line), this page covers what changed, what still works, and how to upgrade safely.

> **Not on PermissionsExPlus yet?** See [Migrate from other plugins](/faq/migration) for original PEX, LuckPerms, GroupManager, and similar.

---

## Version numbering

| Line | Maven / jar versions | Status |
|------|----------------------|--------|
| **Version 1** | `1.23.1` – `1.23.5` | Superseded — upgrade recommended |
| **Version 3** | `3.0.0`+ | Current |

The jump from `1.23.5` → `3.0.0` reflects a major release milestone, not a gap in feature history. All `1.23.x` releases are part of the **Version 1** line documented here.

---

## What changed in 3.0.0

### Defaults

| Area | Version 1 (`1.23.x`) | Version 3 (`3.0.0`) |
|------|----------------------|------------------------|
| Storage backend | Transitioned to H2 `local` in late 1.x | **`local` (H2)** is the documented default |
| Command framework | Both `modern` and `classic` available | **`modern`** remains the default |
| YAML day-to-day storage | Deprecated; file backend normalized to `local` | YAML is **import/migration only** |
| Java runtime | Java 21+ required in maintained builds | **Java 21+** required |

### New in 3.0.0

- Structured **modern command tree** is the primary documented syntax ([Command mapping](/commands/command-mapping))
- **`/pex backend export`** — YAML snapshot of the active backend (modern framework)
- **`/pex backend list`**, **`switch`**, **`import`** — explicit backend admin subcommands
- Expanded **Import & Export** workflows ([guide](/guides/import-export))
- **`dev.rono.permissions.api`** integration surface is stable and documented alongside legacy hooks
- Single universal jar for Spigot, Paper, BungeeCord, Velocity, and Sponge

### Unchanged (your data and habits)

- Permission **nodes**, **groups**, **inheritance**, **world context**, and **weight** behave the same
- **`ru.tehkode.permissions.*` legacy hook API** — frozen but fully supported; typical 1.x plugins work without recompile
- **Classic command framework** — enable with `command-framework: classic` in `config.yml`
- **H2 database** (`permissions.mv.db`) from 1.x loads directly in 3.0.0
- **`permissions.yml`** still auto-imports on first startup when the database is empty

---

## Breaking changes

| Change | Impact | What to do |
|--------|--------|------------|
| Jar version in filename | `PermissionsExPlus-1.23.5.jar` → `PermissionsExPlus-3.0.0.jar` | Remove old jars; keep only one PEX jar |
| `backend: file` in config | Normalized to `local` at load time | Update `config.yml` to `backend: local` when convenient (automatic migration preserves YAML path) |
| Some admin commands are framework-specific | `config`, `convert uuid` = classic only; `export` = modern only | See [Command mapping](/commands/command-mapping) |
| `@Deprecated(since = "3.0.0")` on select legacy methods | Compile-time warnings only | Migrate hook plugins to [Modern API](/developers/api/modern) over time |

There is **no** automatic permission data format break between 1.23.5 and 3.0.0. Your H2 database and imported YAML remain valid.

---

## Compatibility matrix

### Server platforms

| Platform | 1.x | 3.0.0 |
|----------|-----|-------|
| Spigot / Paper | Yes | Yes |
| BungeeCord / Waterfall | Yes | Yes |
| Velocity | Yes | Yes |
| Sponge | Yes | Yes |
| Minecraft `1.8.8` – `1.26.1` | Yes | Yes |

Details: [Platform Compatibility](/developers/compatibility)

### Hook plugins (companion jars)

| Plugin type | 1.x | 3.0.0 |
|-------------|-----|-------|
| Legacy `ru.tehkode.*` hooks | Works | Works — [Legacy API](/developers/api/legacy) frozen |
| Modern `dev.rono.*` hooks | Works (late 1.x) | Works — [Modern API](/developers/api/modern) active |
| Vault consumers | Works | Works |

### Command frameworks

| Framework | 1.x | 3.0.0 |
|-----------|-----|-------|
| `modern` (default) | Yes | Yes — recommended |
| `classic` / `legacy` / `old` | Yes | Yes — set in `config.yml` |

---

## Upgrade steps

### 1. Back up

```text
plugins/PermissionsEx/
├── config.yml
├── permissions.mv.db      # H2 database (if using local backend)
└── permissions.yml        # only if not yet migrated
```

For SQL backends, dump the database too.

### 2. Replace the jar

1. Stop the server
2. Remove **all** old PEX jars (`PermissionsEx.jar`, `PermissionsExPlus-1.23.*.jar`, module-specific jars)
3. Install **`PermissionsExPlus-3.0.0.jar`** from [GitHub Releases](https://github.com/%%site.repo%%/releases)
4. Start the server

### 3. Verify

```text
/pex version
/pex backend
/pex groups list
/pex hierarchy
```

`/pex version` should report **3.0.0**. `/pex backend` should show your expected backend (`local` or `sql`).

### 4. Review config (optional)

```yaml
permissions:
  command-framework: modern   # or classic if your staff prefer it
  backend: local
  backends:
    local:
      type: local
      database: permissions
      migration-source: permissions.yml
```

See [Configuration](/configuration) and [Example files](/guides/example-configs).

### 5. Post-upgrade checks

| Check | Command |
|-------|---------|
| Groups intact | `/pex groups list` |
| Users intact | `/pex users list` |
| Permissions resolve | `/pex user <name> check essentials.home` |
| Prefixes / weight | `/pex user <name> group list` |
| UUID keys (if needed) | `/pex convert uuid` (classic framework) |

---

## Common 1.x → 3.0.0 scenarios

### I used YAML (`backend: file`) on 1.x

Configs with `backend: file` are normalized to `local` automatically. Your `permissions.yml` path is preserved as `migration-source`. On first start with an empty database, YAML is imported and renamed to `permissions.yml.migrated`.

### I already use H2 (`permissions.mv.db`) on 1.x

No data migration needed. Drop in the 3.0.0 jar and start.

### My staff know classic `/pex` syntax

Keep `command-framework: classic` in `config.yml`. All classic commands continue to work. New 3.0.0 features like `/pex backend export` require `modern` — switch temporarily or use [Import & Export](/guides/import-export) alternatives.

### I maintain a hook plugin compiled against 1.23.x

Typical plugins using `PermissionManager`, `PermissionUser`, and Bukkit events work without changes. For new features, adopt [Modern API](/developers/api/modern) at `3.0.0` coordinates:

```xml
<version>3.0.0</version>
```

### I run a proxy network

Upgrade **every** backend and the proxy to 3.0.0. Use a shared **`sql`** backend — not separate H2 files per server. See [Common Setups — network proxy](/guides/recipes/#network-proxy-bungee--backend).

---

## Rollback

If you need to revert:

1. Stop the server
2. Restore your backed-up `plugins/PermissionsEx/` folder
3. Reinstall `PermissionsExPlus-1.23.5.jar`
4. Start the server

> Do not run 1.x and 3.0.0 jars simultaneously or alternate without understanding backend compatibility. Stick to one version per server process.

---

## Related

- [Import & Export](/guides/import-export) — backups and backend transfers
- [Command mapping](/commands/command-mapping) — modern vs classic syntax
- [Migrate from other plugins](/faq/migration) — LuckPerms, original PEX, etc.
- [Troubleshooting](/guides/troubleshooting) — when something does not work after upgrade
- [Changelog](/changelog) — full release notes
