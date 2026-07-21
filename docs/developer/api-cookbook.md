# API Cookbook

Practical examples of how to use the PermissionsExPlus API.

## Getting the API Instance

```java
PexApi api = PexProvider.get();
```

## Checking a Permission

```java
api.users().find(uuid).thenAccept(optionalUser -> {
    optionalUser.ifPresent(user -> {
        QueryOptions query = QueryOptions.builder()
                .contexts(ContextSet.builder().add("world", "nether").build())
                .build();
        
        PermissionResult result = api.resolvers().permissions().check(user, "example.use", query);
        if (result.allowed()) {
            // Do something
        }
    });
});
```

## Mutations

Mutations use atomic modifiers and complete after persistence and cache replacement:

```java
api.users().modify(playerId, modifier -> modifier
        .allowPermission("example.use")
        .addGroup("member")
        .setPrefix("[Member]"));
```

## Creating a Group

```java
api.groups().modify("new_group", modifier -> modifier.allowPermission("group.permission"));
```
