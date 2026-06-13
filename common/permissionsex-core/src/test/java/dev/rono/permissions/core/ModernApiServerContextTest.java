package dev.rono.permissions.core;

import dev.rono.permissions.api.world.Worlds;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Server-bound context facades (proxy realm names share the world namespace). */
class ModernApiServerContextTest extends ModernApiTestSupport {

    @Test
    void inServerDelegatesChecksAndMutations() {
        var user = api().getUserManager().createUser("server-ctx-user");
        var ctx = user.inServer("lobby");

        assertEquals("lobby", ctx.server());
        assertEquals("lobby", ctx.world());
        assertSame(user, ctx.subject());

        ctx.addPermission("lobby.only");
        user.save();

        assertTrue(ctx.hasPermission("lobby.only"));
        assertFalse(user.global().hasPermission("lobby.only"));
        assertFalse(user.hasPermission("lobby.only"));
    }

    @Test
    void inServerMatchesInWorldForSameRealm() {
        var user = api().getUserManager().createUser("server-world-parity");
        user.inServer("survival").addPermission("shared.node");
        user.save();

        assertTrue(user.inWorld("survival").hasPermission("shared.node"));
        assertTrue(user.inServer("survival").hasPermission("shared.node"));
    }

    @Test
    void userServerContextGroupOperations() {
        api().getGroupManager().createGroup("server-ctx-group").save();
        var user = api().getUserManager().createUser("server-ctx-group-user");

        user.inServer("lobby").addGroup("server-ctx-group");
        user.save();

        assertTrue(user.inServer("lobby").inGroup("server-ctx-group"));
        assertFalse(user.inServer("hub").inGroup("server-ctx-group"));
    }

    @Test
    void groupServerContextHierarchyOperations() {
        var parent = api().getGroupManager().createGroup("server-parent");
        var child = api().getGroupManager().createGroup("server-child");
        child.inServer("lobby").addParent(parent.getName());
        child.save();

        assertTrue(child.inServer("lobby").isChildOf(parent.getName()));
        assertFalse(parent.inServer("lobby").children().isEmpty());
    }

    @Test
    void serverGlobalNormalizesEmptyString() {
        var user = api().getUserManager().createUser("server-global-user");
        user.inServer("").addPermission("server-global-key");
        user.save();

        assertTrue(user.hasPermission("server-global-key"));
        assertEquals(Worlds.GLOBAL, Worlds.normalize(""));
    }
}
