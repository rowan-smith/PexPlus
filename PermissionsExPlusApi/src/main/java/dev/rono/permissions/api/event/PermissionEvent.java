package dev.rono.permissions.api.event;

public sealed abstract class PermissionEvent
        permits UserCreatedEvent,
                UserLoadedEvent,
                UserUnloadedEvent,
                GroupCreatedEvent,
                GroupDeletedEvent,
                PermissionAddedEvent,
                PermissionRemovedEvent,
                UserGroupAddedEvent,
                UserGroupRemovedEvent,
                ReloadEvent {

}
