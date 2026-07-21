# Developer Overview

PermissionsExPlus provides a powerful and flexible API for interacting with the permissions engine.

## Maven Dependency

```xml
<dependency>
    <groupId>dev.rono.permissions</groupId>
    <artifactId>PermissionsExPlusApi</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

## Core Components

- `PexApi`: The main entry point.
- `UserManager`, `GroupManager`, `LadderManager`: Managers for each entity type.
- `QueryOptions`: Define how permissions should be resolved (contexts, etc.).
- `Modifier`: Atomic mutation builder.
