package dev.rono.permissions.core.store.repository;

import dev.rono.permissions.core.model.UserSnapshot;
import dev.rono.permissions.core.store.dto.UserDto;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Session;

public final class UserRepository {
    private final NodeRepository nodes;

    public UserRepository(NodeRepository nodes) {
        this.nodes = nodes;
    }

    public Optional<UserSnapshot> find(Session session, String key) {
        return Optional.ofNullable(session.find(UserDto.class, key)).map(dto -> snapshot(session, dto));
    }

    public Map<String, UserSnapshot> all(Session session) {
        var result = new LinkedHashMap<String, UserSnapshot>();

        session.createSelectionQuery("from UserDto", UserDto.class).getResultList().forEach(dto -> result.put(dto.id(), snapshot(session, dto)));

        return result;
    }

    public void save(Session session, UserSnapshot user) {
        var key = user.uniqueId().toString();

        session.merge(new UserDto(key, user.name()));

        nodes.replace(session, "user", key, user.explicitPermissions(), user.explicitOptions(), user.groups());
    }

    public boolean delete(Session session, String key) {
        var dto = session.find(UserDto.class, key);
        if (dto == null) {
            return false;
        }

        nodes.delete(session, "user", key);

        session.remove(dto);

        return true;
    }

    private UserSnapshot snapshot(Session session, UserDto dto) {
        return new UserSnapshot(UUID.fromString(dto.id()), dto.name(), nodes.permissions(session, "user", dto.id()), nodes.options(session, "user", dto.id()), nodes.parents(session, "user", dto.id()));
    }
}
