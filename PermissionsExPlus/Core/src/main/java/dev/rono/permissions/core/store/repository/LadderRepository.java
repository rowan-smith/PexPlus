package dev.rono.permissions.core.store.repository;

import dev.rono.permissions.core.model.LadderSnapshot;
import dev.rono.permissions.core.store.dto.LadderDto;
import dev.rono.permissions.core.store.dto.LadderGroupDto;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Session;

public final class LadderRepository {
    public Optional<LadderSnapshot> find(Session session, String key) {
        return Optional.ofNullable(session.find(LadderDto.class, key)).map(dto -> snapshot(session, dto));
    }

    public Map<String, LadderSnapshot> all(Session session) {
        var result = new LinkedHashMap<String, LadderSnapshot>();

        session.createSelectionQuery("from LadderDto", LadderDto.class).getResultList().forEach(dto -> result.put(dto.name(), snapshot(session, dto)));

        return result;
    }

    public void save(Session session, LadderSnapshot ladder) {
        session.merge(new LadderDto(ladder.name()));
        session.createMutationQuery("delete from LadderGroupDto where ladderName = :name").setParameter("name", ladder.name()).executeUpdate();

        for (var index = 0; index < ladder.groups().size(); index++) {
            session.persist(new LadderGroupDto(UUID.randomUUID().toString(), ladder.name(), ladder.groups().get(index), index));
        }
    }

    public boolean delete(Session session, String key) {
        var dto = session.find(LadderDto.class, key);
        if (dto == null) {
            return false;
        }

        session.createMutationQuery("delete from LadderGroupDto where ladderName = :name").setParameter("name", key).executeUpdate();
        session.remove(dto);

        return true;
    }

    private LadderSnapshot snapshot(Session session, LadderDto dto) {
        var groups = session.createSelectionQuery("from LadderGroupDto where ladderName = :name order by position", LadderGroupDto.class)
                .setParameter("name", dto.name()).getResultList().stream().map(LadderGroupDto::groupName).toList();

        return new LadderSnapshot(dto.name(), groups);
    }
}
