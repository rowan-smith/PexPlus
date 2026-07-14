package dev.rono.permissions.api.event;

import dev.rono.permissions.api.subject.PermissionSubject;

public interface SubjectEvent<T extends PermissionSubject> extends PermissionEvent {
    T subject();
}
