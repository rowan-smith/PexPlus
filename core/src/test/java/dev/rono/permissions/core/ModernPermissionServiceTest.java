package dev.rono.permissions.core;

import dev.rono.permissions.api.service.PermissionService;
import dev.rono.permissions.api.service.PermissionServiceBridge;
import dev.rono.permissions.api.subject.Group;
import dev.rono.permissions.api.subject.TimedGroupMembership;
import dev.rono.permissions.api.subject.TimedPermissionEntry;
import dev.rono.permissions.api.subject.User;
import dev.rono.permissions.api.world.Worlds;
import org.junit.jupiter.api.Test;
import ru.tehkode.permissions.PEXTestBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModernPermissionServiceTest extends PEXTestBase {

    private PermissionService pex() {
        return (PermissionService) manager;
    }

    private PermissionServiceBridge bridge() {
        return PermissionService.requireBridge(pex());
    }

    @Test
    void permissionServiceExposesBackendAndCounts() {
        assertNotNull(pex().query().backend().info());
        assertTrue(pex().query().groups().count() >= 0);
        assertTrue(pex().query().users().count() >= 0);
    }

    @Test
    void userAndGroupCrudViaModernApi() {
        Group defaultGroup = bridge().group("default");
        defaultGroup.addPermission("modern.group", null);

        User user = bridge().user("modern-api-user");
        user.addPermission("modern.test", null);
        user.addGroup("default", null);
        user.save();

        assertTrue(bridge().findUser("modern-api-user").isPresent());
        assertTrue(user.has("modern.test", null));
        assertTrue(user.inGroup("default", null, false));
        assertTrue(defaultGroup.permissions(null).contains("modern.group"));

        user.removePermission("modern.test", null);
        user.removeGroup("default", null);
        user.delete();
        assertTrue(user.permissions(null).isEmpty());
    }

    @Test
    void worldPermissionsAndTimedEntries() {
        User user = bridge().user("world-perms-user");

        user.inWorld("world").addPermission("world.node");
        user.inWorld("world").addTimedPermission("world.temp", 120);
        user.save();

        assertTrue(user.permissionsByWorld().containsKey("world"));
        assertTrue(user.inWorld("world").has("world.node"));
        assertFalse(user.inWorld("other").has("world.node"));

        List<TimedPermissionEntry> timed = user.timedPermissionEntries("world");
        assertEquals(1, timed.size());
        assertEquals("world.temp", timed.get(0).permission());
        assertTrue(timed.get(0).remainingSeconds() > 0);

        bridge().setWorldInheritance("world", List.of());
        assertNotNull(bridge().worldInheritance("world"));
    }

    @Test
    void timedGroupMembershipMetadata() {
        bridge().group("timed-group");
        User user = bridge().user("timed-group-user");
        user.addGroup("timed-group", null, 90);

        List<TimedGroupMembership> memberships = user.timedGroupMemberships(null);
        assertEquals(1, memberships.size());
        assertEquals("timed-group", memberships.get(0).groupName());
        assertTrue(memberships.get(0).remainingSeconds() > 0);
        assertTrue(user.groupMembershipRemainingSeconds("timed-group", null) > 0);
    }

    @Test
    void reloadWrapsBackendFailures() throws Exception {
        pex().query().reload();
    }

    @Test
    void findUserByUuidWhenPersisted() {
        UUID id = UUID.randomUUID();
        User user = bridge().user(id.toString());
        user.setOption("name", "uuid-user", null);
        user.save();

        Optional<User> found = bridge().findUser(id);
        assertTrue(found.isPresent());
        assertEquals("uuid-user", found.get().name());

        user.delete();
    }

    @Test
    void groupMembersAndDefaultGroups() {
        Group group = bridge().group("member-group");
        User user = bridge().user("member-user");
        user.addGroup("member-group", Worlds.GLOBAL);
        user.save();

        assertTrue(group.memberIdentifiers().contains(user.identifier()));
        assertFalse(group.members().isEmpty());
        assertFalse(group.members(Worlds.GLOBAL, true).isEmpty());
        assertNotNull(bridge().defaultGroups(null));
    }

    @Test
    void eventBusNotifiesListeners() {
        var received = new java.util.concurrent.atomic.AtomicInteger(0);
        var subscription = pex().query().events().subscribe(new dev.rono.permissions.api.event.PermissionEventListener() {
            @Override
            public void onEntity(dev.rono.permissions.api.bus.EntityDispatch dispatch) {
                received.incrementAndGet();
            }
        });
        bridge().user("event-bus-user").addPermission("event.test", null);
        bridge().user("event-bus-user").save();
        assertTrue(received.get() > 0);
        pex().query().events().unsubscribe(subscription);
    }

    @Test
    void editSessionBatchSave() {
        try (var session = pex().query().editSession()) {
            session.editUser("batch-user", user -> user.addPermission("batch.node", null));
            session.editGroup("batch-group", group -> group.addPermission("batch.group", null));
            session.save();
        }
        assertTrue(bridge().findUser("batch-user").isPresent());
        assertTrue(bridge().group("batch-group").permissions(null).contains("batch.group"));
    }

    @Test
    void groupChildrenAndAsyncReload() throws Exception {
        Group parent = bridge().group("parent-group");
        Group child = bridge().group("child-group");
        child.addParent("parent-group", null);
        child.save();
        assertFalse(parent.children().isEmpty());
        assertFalse(parent.descendants().isEmpty());
        pex().query().reloadAsync().get();
    }

    @Test
    void promoteDemoteViaModernUser() throws dev.rono.permissions.api.RankingException {
        Group mod = bridge().group("mod");
        mod.setRank(2, "default");
        mod.save();
        Group admin = bridge().group("admin");
        admin.setRank(1, "default");
        admin.save();
        User user = bridge().user("rank-user");
        user.addGroup("mod", null);
        user.save();

        Group promoted = user.promote("default");
        assertEquals("admin", promoted.identifier());
        Group demoted = user.demote("default");
        assertEquals("mod", demoted.identifier());
    }

    @Test
    void queryApiEntryPoints() {
        bridge().group("member-group");
        User user = bridge().user("fluent-user");
        user.addPermission("fluent.test", null);
        user.addGroup("member-group", Worlds.GLOBAL);
        user.save();

        assertTrue(pex().query().users().resolve("fluent-user").inWorld(null).has("fluent.test"));
        assertTrue(pex().query().world(null).user("fluent-user").inGroup("member-group"));
        assertFalse(pex().query().groups().resolve("member-group").inWorld(null).members().isEmpty());
        assertTrue(pex().query().world(null).findUser("fluent-user").map(u -> u.inGroup("member-group")).orElse(false));
        assertTrue(pex().query().world(null).findGroup("member-group").map(g -> !g.members().isEmpty()).orElse(false));
        assertTrue(pex().query().users().count() > 0);
        assertTrue(pex().query().groups().count() > 0);

        bridge().group("fluent-parent");
        Group child = bridge().group("fluent-child");
        child.addParent("fluent-parent", null);
        child.save();
        assertFalse(pex().query().world(null).group("fluent-parent").children().isEmpty());
        assertFalse(pex().query().world(null).group("fluent-parent").descendants().isEmpty());

        user.delete();
    }

    @Test
    void exportDataAndBackendHandle() throws dev.rono.permissions.api.PermissionsExException {
        assertNotNull(pex().query().backend().exportData());
        var handle = pex().query().backend().createHandle("mock");
        assertNotNull(handle.info());
    }
}
