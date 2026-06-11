package dev.rono.permissions.core;

import dev.rono.permissions.api.PermissionsExException;
import dev.rono.permissions.api.backend.BackendInfo;
import dev.rono.permissions.api.service.PermissionService;
import dev.rono.permissions.api.subject.Group;
import dev.rono.permissions.api.subject.User;
import org.junit.jupiter.api.Test;
import ru.tehkode.permissions.PEXTestBase;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModernPermissionServiceTest extends PEXTestBase {

    @Test
    void permissionServiceExposesBackendAndCounts() {
        PermissionService service = (PermissionService) manager;
        BackendInfo backend = service.backend();
        assertNotNull(backend);
        assertNotNull(backend.type());
        assertNotNull(backend.simpleName());
        assertEquals(backend.simpleName(), service.activeBackendSimpleName());
        assertEquals(service.groupCount(), service.registeredGroupCount());
        assertEquals(service.userCount(), service.registeredUserNameCount());
    }

    @Test
    void userAndGroupCrudViaModernApi() {
        PermissionService service = (PermissionService) manager;

        Group defaultGroup = service.group("default");
        defaultGroup.addPermission("modern.group", null);

        User user = service.user("modern-api-user");
        user.addPermission("modern.test", null);
        user.addGroup("default", null);
        user.save();

        assertTrue(service.findUser("modern-api-user").isPresent());
        assertTrue(user.has("modern.test", null));
        assertTrue(user.inGroup("default", null, false));
        assertTrue(defaultGroup.permissions(null).contains("modern.group"));
        assertTrue(service.has("modern-api-user", "modern.test", null));

        user.removePermission("modern.test", null);
        user.removeGroup("default", null);
        user.delete();
        assertTrue(user.permissions(null).isEmpty());
    }

    @Test
    void reloadWrapsBackendFailures() throws PermissionsExException {
        PermissionService service = (PermissionService) manager;
        service.reload();
    }

    @Test
    void findUserByUuidWhenPersisted() {
        PermissionService service = (PermissionService) manager;
        UUID id = UUID.randomUUID();
        User user = service.user(id.toString());
        user.setOption("name", "uuid-user", null);
        user.save();

        Optional<User> found = service.findUser(id);
        assertTrue(found.isPresent());
        assertEquals("uuid-user", found.get().name());

        user.delete();
    }
}
