package dev.rono.permissions.core.storage.backend;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.core.storage.LocalSqlRepository;
import dev.rono.permissions.core.storage.resolution.EffectiveUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tehkode.permissions.PEXBackendConfiguration;
import ru.tehkode.permissions.exceptions.PermissionBackendException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalSqlBackendTest extends ru.tehkode.permissions.PEXTestBase {

    private LocalSqlRepository repository;
    private LocalSqlBackend backend;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        repository = LocalSqlRepository.inMemory("backend-" + System.nanoTime());
        repository.deploySchema();
        repository.setSchemaVersion(LocalSqlRepository.SCHEMA_VERSION);
        backend = new LocalSqlBackend(manager, repository, java.io.File.createTempFile("unused", ".yml"));
    }

    @AfterEach
    void tearDownBackend() throws PermissionBackendException {
        if (backend != null) {
            backend.close();
        }
        if (repository != null) {
            repository.close();
        }
    }

    @Test
    void persistsAndResolvesPermissions() throws Exception {
        var group = backend.getGroupData("staff");
        group.setPermissions(List.of("staff.chat"), null);
        group.save();

        var user = backend.getUserData("alex");
        user.setParents(List.of("staff"), null);
        user.setPermissions(List.of("-staff.chat"), null);
        user.save();

        EffectiveUser effective = backend.resolveEffectiveUser("alex", PermissionContext.global());
        assertFalse(effective.hasPermission("staff.chat"));
        assertTrue(backend.hasUser("alex"));
        assertTrue(backend.hasGroup("staff"));
    }

    @Test
    void invalidatesCacheAfterUserMutation() throws Exception {
        var user = backend.getUserData("cached");
        user.setPermissions(List.of("a"), null);
        user.save();

        EffectiveUser first = backend.resolveEffectiveUser("cached", PermissionContext.global());
        assertTrue(first.hasPermission("a"));

        user.setPermissions(List.of(), null);
        user.save();

        EffectiveUser second = backend.resolveEffectiveUser("cached", PermissionContext.global());
        assertNotSame(first, second);
        assertFalse(second.hasPermission("a"));
    }

    @Test
    void diagnosticLabelIncludesDatabaseName() {
        assertTrue(backend.diagnosticLabel().startsWith("local-h2:"));
    }
}
