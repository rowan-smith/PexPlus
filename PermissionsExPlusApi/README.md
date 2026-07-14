```
PermissionsExPlusApi
└── src/main/java
└── dev/rono/permissions/api
│
├── PermissionsExPlusApi.java
├── PermissionsExPlusProvider.java
│
├── backend/
│   ├── Backend.java
│   ├── BackendType.java
│   └── BackendException.java
│
├── data/
│   ├── DataStore.java
│   ├── Transaction.java
│   └── SaveResult.java
│
├── event/
│   ├── EventBus.java
│   ├── EventSubscription.java
│   ├── PermissionEvent.java
│   │
│   ├── group/
│   ├── ladder/
│   ├── permission/
│   ├── realm/
│   └── user/
│
├── group/
│   ├── Group.java
│   ├── GroupManager.java
│   ├── GroupBuilder.java
│   ├── GroupAlreadyExistsException.java
│   ├── GroupNotFoundException.java
│   └── TimedGroupMembership.java
│
├── ladder/
│   ├── Ladder.java
│   ├── LadderManager.java
│   ├── LadderAlreadyExistsException.java
│   ├── LadderNotFoundException.java
│   └── PromotionResult.java
│
├── metadata/
│   ├── MetadataHolder.java
│   ├── MetadataMap.java
│   ├── MetadataNode.java
│   └── MetadataValue.java
│
├── permission/
│   ├── PermissionHolder.java
│   ├── PermissionManager.java
│   ├── PermissionNode.java
│   ├── PermissionContext.java
│   ├── PermissionResult.java
│   ├── PermissionValue.java
│   ├── PermissionAttachment.java
│   └── PermissionInheritance.java
│
├── realm/
│   ├── Realm.java
│   ├── RealmManager.java
│   ├── RealmAlreadyExistsException.java
│   └── RealmNotFoundException.java
│
├── subject/
│   ├── PermissionSubject.java
│   ├── SubjectType.java
│   ├── SubjectContext.java
│   └── SubjectManager.java
│
├── user/
│   ├── User.java
│   ├── UserManager.java
│   ├── UserAlreadyExistsException.java
│   ├── UserNotFoundException.java
│   └── UserProfile.java
│
└── util/
├── Identifier.java
├── Names.java
└── Preconditions.java
```


```java
// Example
user.global().addGroup(admin);

user.global().removePermission("example.test");

user.context(survival).addPermission("survival.fly");

user.context(nether).hasPermission("nether.build");
```