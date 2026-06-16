package dev.rono.permissions.core.storage.backend;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LocalSqlGroupData extends LocalSqlEntityData implements ru.tehkode.permissions.PermissionsGroupData {

    private Integer groupId;

    public LocalSqlGroupData(LocalSqlBackend backend, String groupName) {
        super(backend, groupName);
    }

    @Override
    public void load() {
        try {
            groupId = backend.resolveGroupId(identifier);
            loaded = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> permissionsForContext(String contextKey) throws Exception {
        return repository.getGroupPermissions(requireGroupId(), contextKey);
    }

    @Override
    protected void replacePermissions(String contextKey, List<String> permissions) throws Exception {
        ensurePersisted();
        repository.replaceGroupPermissions(requireGroupId(), contextKey, permissions);
    }

    @Override
    protected Set<String> worldsForEntity() throws Exception {
        return new HashSet<>(repository.listGroupPermissionWorlds(requireGroupId()));
    }

    @Override
    protected String optionForContext(String option, String contextKey) throws Exception {
        if (contextKey != null) {
            return null;
        }
        return switch (option) {
            case "prefix" -> repository.loadGroup(requireGroupId()).getOptions().getPrefix();
            case "suffix" -> repository.loadGroup(requireGroupId()).getOptions().getSuffix();
            case "default" -> repository.loadGroup(requireGroupId()).isDefaultGroup() ? "true" : null;
            default -> null;
        };
    }

    @Override
    protected void setEntityOption(String option, String value) throws Exception {
        ensurePersisted();
        if ("default".equals(option)) {
            int id = requireGroupId();
            repository.upsertGroup(identifier, repository.loadGroup(id).getWeight(), Boolean.parseBoolean(value));
            return;
        }
        repository.setGroupOption(requireGroupId(), option, value);
    }

    @Override
    protected List<String> parentsForEntity() throws Exception {
        return repository.getGroupParents(requireGroupId());
    }

    @Override
    protected void replaceParents(List<String> parents) throws Exception {
        ensurePersisted();
        int id = requireGroupId();
        repository.clearGroupParents(id);
        for (String parent : parents) {
            repository.findGroupId(parent).ifPresent(parentId -> {
                try {
                    repository.setGroupParent(id, parentId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    protected boolean entityExists() throws Exception {
        return repository.findGroupId(identifier).isPresent();
    }

    @Override
    protected void persist() throws Exception {
        if (!entityExists()) {
            groupId = repository.upsertGroup(identifier, 0, false);
        }
    }

    @Override
    protected void deleteEntity() throws Exception {
        repository.deleteGroup(requireGroupId());
    }

    private void ensurePersisted() throws Exception {
        if (!entityExists()) {
            groupId = repository.upsertGroup(identifier, 0, false);
        }
    }

    private int requireGroupId() throws Exception {
        if (groupId == null) {
            groupId = backend.resolveGroupId(identifier);
        }
        return groupId;
    }
}
