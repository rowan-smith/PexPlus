package ru.tehkode.permissions.backends.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import dev.rono.permissions.api.PexApi;
import dev.rono.permissions.api.backend.BackendManager;
import dev.rono.permissions.api.config.ConfigurationManager;
import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.event.EventBus;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.platform.context.ContextManager;
import dev.rono.permissions.api.resolver.Resolvers;
import dev.rono.permissions.api.user.UserManager;
import dev.rono.permissions.core.event.EventBusImpl;
import dev.rono.permissions.core.manager.GroupManagerImpl;
import dev.rono.permissions.core.manager.LadderManagerImpl;
import dev.rono.permissions.core.manager.UserManagerImpl;
import dev.rono.permissions.core.resolver.ResolverImpl;
import dev.rono.permissions.core.store.MemoryDataStore;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tehkode.permissions.PermissionManager;

class PermissionBackendTest {
    private UserManagerImpl users;

    private GroupManagerImpl groups;

    private PermissionBackend backend;

    @BeforeEach
    void setUp() throws Exception {
        var store = new MemoryDataStore();
        store.open();

        var events = new EventBusImpl(Assertions::fail);

        groups = new GroupManagerImpl(store, events, 10);
        users = new UserManagerImpl(store, events);

        var ladders = new LadderManagerImpl(store, events);

        groups.attach(users, ladders);
        users.attachGroups(groups);
        ladders.attach(users, groups);

        var resolvers = new ResolverImpl(groups, 10);

        PexApi api = new TestApi(users, groups, ladders, resolvers, events);

        backend = new PermissionBackend(mock(PermissionManager.class), null, api);
    }

    @Test
    void legacyGroupDataMutatesNodesParentsWeightAndReadsImplicitDefault() {
        var defaultData = backend.getGroupData("default");

        defaultData.save();

        backend.getGroupData("parent").setPermissions(List.of("example.parent"), null);

        var data = backend.getGroupData("staff");

        assertTrue(data.isVirtual());

        data.setPermissions(List.of("example.use", "-example.blocked"), "world_nether");

        data.setParents(List.of("parent"), null);

        data.setOption("prefix", "[Staff]", "world_nether");

        data.setOption("weight", "50", null);

        assertThrows(UnsupportedOperationException.class,
                () -> data.setOption("default", "true", "world_nether"));

        var staff = groups.cache().get("staff").orElseThrow();

        assertEquals(java.util.Set.of("example.use", "-example.blocked"),
                java.util.Set.copyOf(data.getPermissions("world_nether")));

        assertTrue(staff.hasDirectParent("parent", ContextSet.empty()));

        assertEquals(50, staff.weight().orElseThrow());

        assertEquals("[Staff]", data.getOption("prefix", "world_nether"));

        assertEquals("true", defaultData.getOption("default", "world_nether"));

        assertEquals("false", data.getOption("default", "world_nether"));
    }

    @Test
    void legacyUserDataCreatesAndUpdatesTheUser() {
        backend.getGroupData("member").save();

        var id = UUID.randomUUID();

        var data = backend.getUserData(id.toString());

        assertTrue(data.isVirtual());

        data.setOption("name", "Rono", null);

        data.setPermissions(List.of("example.use"), null);

        data.setParents(List.of("member"), "survival");

        var user = users.cache().get(id).orElseThrow();

        assertEquals("Rono", user.name());

        assertTrue(user.explicitlyAllows("example.use", ContextSet.empty()));

        assertTrue(user.hasDirectGroup("member", ContextSet.builder().add("world", "survival").build()));

        assertTrue(backend.hasUser(id.toString()));

        assertTrue(backend.getUserNames().contains("Rono"));
    }

    private record TestApi(UserManager users, GroupManager groups, LadderManager ladders, Resolvers resolvers, EventBus events) implements PexApi {
        @Override
        public BackendManager backend() {
            return null;
        }

        @Override
        public ContextManager<UUID> contexts() {
            return null;
        }

        @Override
        public ConfigurationManager config() {
            return null;
        }
    }
}
