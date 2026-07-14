package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.permission.PermissionHolder;

public interface PermissionSubject extends PermissionHolder {
    String name();

    SubjectType type();
}