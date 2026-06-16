package dev.rono.permissions.core.storage.backend;

import dev.rono.permissions.core.storage.LocalSqlRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class LocalSqlUserData extends LocalSqlEntityData implements ru.tehkode.permissions.PermissionsUserData {

    private UUID userId;

    public LocalSqlUserData(LocalSqlBackend backend, String userName) {
        super(backend, userName);
    }

    @Override
    public boolean setIdentifier(String identifier) {
        return false;
    }

    @Override
    public void load() {
        try {
            userId = backend.resolveUserId(identifier);
            loaded = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> permissionsForContext(String contextKey) throws Exception {
        return repository.getUserPermissions(requireUserId(), contextKey);
    }

    @Override
    protected void replacePermissions(String contextKey, List<String> permissions) throws Exception {
        ensurePersisted();
        repository.replaceUserPermissions(requireUserId(), contextKey, permissions);
    }

    @Override
    protected Set<String> worldsForEntity() throws Exception {
        return new HashSet<>(repository.listPermissionWorlds(requireUserId()));
    }

    @Override
    protected String optionForContext(String option, String contextKey) throws Exception {
        if (contextKey != null) {
            return null;
        }
        return switch (option) {
            case "prefix" -> repository.loadUser(requireUserId()).getOptions().getPrefix();
            case "suffix" -> repository.loadUser(requireUserId()).getOptions().getSuffix();
            default -> null;
        };
    }

    @Override
    protected void setEntityOption(String option, String value) throws Exception {
        ensurePersisted();
        repository.setUserOption(requireUserId(), option, value);
    }

    @Override
    protected List<String> parentsForEntity() throws Exception {
        return repository.getUserParents(requireUserId());
    }

    @Override
    protected void replaceParents(List<String> parents) throws Exception {
        ensurePersisted();
        UUID id = requireUserId();
        repository.clearUserGroups(id, null);
        for (String parent : parents) {
            repository.findGroupId(parent).ifPresent(groupId -> {
                try {
                    repository.setUserGroup(id, groupId, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    protected boolean entityExists() throws Exception {
        return repository.userExistsByName(identifier);
    }

    @Override
    protected void persist() throws Exception {
        UUID id = requireUserId();
        if (!repository.userExists(id)) {
            repository.upsertUser(id, identifier, null, Instant.now());
        }
    }

    @Override
    protected void deleteEntity() throws Exception {
        repository.deleteUser(requireUserId());
    }

    private void ensurePersisted() throws Exception {
        UUID id = requireUserId();
        if (!repository.userExists(id)) {
            repository.upsertUser(id, identifier, null, Instant.now());
        }
    }

    private UUID requireUserId() throws Exception {
        if (userId == null) {
            userId = backend.resolveUserId(identifier);
        }
        return userId;
    }
}
