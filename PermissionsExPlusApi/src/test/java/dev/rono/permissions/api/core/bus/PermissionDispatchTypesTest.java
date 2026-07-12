package dev.rono.permissions.api.core.bus;

import dev.rono.permissions.api.bus.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PermissionDispatchTypesTest {

    @Test
    void entityDispatchImplementsPermissionDispatch() {
        var dispatch = new EntityDispatch(
                UUID.randomUUID(), "alice", "USER", EntityMutation.PERMISSIONS_CHANGED);
        assertInstanceOf(PermissionDispatch.class, dispatch);
        assertEquals("alice", dispatch.entityIdentifier());
        assertEquals(EntityMutation.PERMISSIONS_CHANGED, dispatch.mutation());
    }

    @Test
    void systemDispatchImplementsPermissionDispatch() {
        var dispatch = new SystemDispatch(UUID.randomUUID(), SystemMutation.RELOADED);
        assertInstanceOf(PermissionDispatch.class, dispatch);
        assertEquals(SystemMutation.RELOADED, dispatch.mutation());
    }
}
