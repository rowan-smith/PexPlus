package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.user.User;

import java.util.Collection;
import java.util.Optional;

public interface LadderManager {

    Optional<Ladder> find(String name);

    Ladder load(String name);

    Ladder create(String name);

    void delete(String name);

    PromotionResult promote(User user, Ladder ladder, Realm realm);

    PromotionResult demote(User user, Ladder ladder, Realm realm);

    void addGroup(Ladder ladder, Group group);

    void removeGroup(Ladder ladder, Group group);

    Collection<Ladder> all();
}
