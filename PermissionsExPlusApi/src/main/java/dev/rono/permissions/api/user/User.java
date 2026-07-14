package dev.rono.permissions.api.user;

import dev.rono.permissions.api.subject.PermissionSubject;

import java.util.UUID;

public interface User extends PermissionSubject {
    UUID id();

    String username();

    @Override
    default String name() {
        return username();
    }
}