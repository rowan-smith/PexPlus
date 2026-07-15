package dev.rono.permissions.core.store.repository;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.options.OptionNode;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionValue;
import dev.rono.permissions.core.store.dto.ContextDto;
import dev.rono.permissions.core.store.dto.OptionDto;
import dev.rono.permissions.core.store.dto.ParentDto;
import dev.rono.permissions.core.store.dto.PermissionDto;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.Session;

public final class NodeRepository {
    public Set<PermissionNode> permissions(Session session, String type, String key) {
        var result = new LinkedHashSet<PermissionNode>();

        rows(session, PermissionDto.class, type, key).forEach(row -> {
            var builder = PermissionNode.builder().permission(row.permission()).value(PermissionValue.valueOf(row.value())).contexts(contexts(session, "permission", row.id()));

            if (row.expiry() != null) {
                builder.expiry(row.expiry());
            }

            result.add(builder.build());
        });

        return Set.copyOf(result);
    }

    public Set<OptionNode> options(Session session, String type, String key) {
        var result = new LinkedHashSet<OptionNode>();

        rows(session, OptionDto.class, type, key).forEach(row -> {
            var builder = OptionNode.builder().key(row.optionKey()).value(row.optionValue()).contexts(contexts(session, "option", row.id()));

            if (row.expiry() != null) {
                builder.expiry(row.expiry());
            }

            result.add(builder.build());
        });

        return Set.copyOf(result);
    }

    public Set<ParentNode> parents(Session session, String type, String key) {
        var result = new LinkedHashSet<ParentNode>();

        rows(session, ParentDto.class, type, key).forEach(row -> {
            var builder = ParentNode.builder().group(row.groupName()).contexts(contexts(session, "parent", row.id()));

            if (row.expiry() != null) {
                builder.expiry(row.expiry());
            }

            result.add(builder.build());
        });

        return Set.copyOf(result);
    }

    public void replace(Session session, String type, String key, Set<PermissionNode> permissions, Set<OptionNode> options, Set<ParentNode> parents) {
        delete(session, type, key);

        permissions.forEach(node -> {
            var id = UUID.randomUUID().toString();

            session.persist(new PermissionDto(id, type, key, node.permission(), node.value().name(), node.expiry().orElse(null)));

            persistContexts(session, "permission", id, node.contexts());
        });

        options.forEach(node -> {
            var id = UUID.randomUUID().toString();

            session.persist(new OptionDto(id, type, key, node.key(), node.value(), node.expiry().orElse(null)));

            persistContexts(session, "option", id, node.contexts());
        });

        parents.forEach(node -> {
            var id = UUID.randomUUID().toString();

            session.persist(new ParentDto(id, type, key, node.group(), node.expiry().orElse(null)));

            persistContexts(session, "parent", id, node.contexts());
        });
    }

    public void delete(Session session, String type, String key) {
        deleteRows(session, PermissionDto.class, "permission", type, key);
        deleteRows(session, OptionDto.class, "option", type, key);
        deleteRows(session, ParentDto.class, "parent", type, key);
    }

    private <T> List<T> rows(Session session, Class<T> type, String subjectType, String subjectKey) {
        return session.createSelectionQuery("from " + type.getSimpleName() + " where subjectType = :subjectType and subjectKey = :subjectKey", type).setParameter("subjectType", subjectType).setParameter("subjectKey", subjectKey).getResultList();
    }

    private ContextSet contexts(Session session, String nodeType, String nodeId) {
        var builder = ContextSet.builder();

        session.createSelectionQuery("from ContextDto where nodeType = :nodeType and nodeId = :nodeId", ContextDto.class).setParameter("nodeType", nodeType).setParameter("nodeId", nodeId).getResultList().forEach(row -> builder.add(row.contextKey(), row.contextValue()));

        return builder.build();
    }

    private void persistContexts(Session session, String nodeType, String nodeId, ContextSet contexts) {
        contexts.asMap().forEach((key, values) -> values.forEach(value -> session.persist(new ContextDto(UUID.randomUUID().toString(), nodeType, nodeId, key, value))));
    }

    private <T> void deleteRows(Session session, Class<T> type, String nodeType, String subjectType, String subjectKey) {
        var ids = rows(session, type, subjectType, subjectKey).stream().map(row -> {
            if (row instanceof PermissionDto permission) {
                return permission.id();
            }

            if (row instanceof OptionDto option) {
                return option.id();
            }

            return ((ParentDto) row).id();
        }).toList();

        if (!ids.isEmpty()) {
            session.createMutationQuery("delete from ContextDto where nodeType = :nodeType and nodeId in :ids").setParameter("nodeType", nodeType).setParameter("ids", ids).executeUpdate();
        }

        session.createMutationQuery("delete from " + type.getSimpleName() + " where subjectType = :subjectType and subjectKey = :subjectKey").setParameter("subjectType", subjectType).setParameter("subjectKey", subjectKey).executeUpdate();
    }
}
