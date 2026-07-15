package dev.rono.permissions.core.store.repository;

import dev.rono.permissions.core.model.GroupSnapshot;
import dev.rono.permissions.core.store.dto.GroupDto;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import org.hibernate.Session;

public final class GroupRepository {
    private final NodeRepository nodes;

    public GroupRepository(NodeRepository nodes) {
        this.nodes = nodes;
    }

    public Optional<GroupSnapshot> find(Session session, String key) {
        return Optional.ofNullable(session.find(GroupDto.class, key)).map(dto -> snapshot(session, dto));
    }

    public Map<String, GroupSnapshot> all(Session session) {
        var result = new LinkedHashMap<String, GroupSnapshot>();

        session.createSelectionQuery("from GroupDto", GroupDto.class).getResultList()
                .forEach(dto -> result.put(dto.name(), snapshot(session, dto)));

        return result;
    }

    public void save(Session session, GroupSnapshot group) {
        session.merge(new GroupDto(group.name(), group.weight().isPresent() ? group.weight().getAsInt() : null));

        nodes.replace(session, "group", group.name(), group.explicitPermissions(), group.explicitOptions(), group.parents());
    }

    public boolean delete(Session session, String key) {
        var dto = session.find(GroupDto.class, key);

        if (dto == null) {
            return false;
        }

        nodes.delete(session, "group", key);

        session.remove(dto);

        return true;
    }

    private GroupSnapshot snapshot(Session session, GroupDto dto) {
        var weight = dto.weight() == null ? OptionalInt.empty() : OptionalInt.of(dto.weight());

        return new GroupSnapshot(dto.name(), weight, nodes.permissions(session, "group", dto.name()), nodes.options(session, "group", dto.name()), nodes.parents(session, "group", dto.name()));
    }
}
