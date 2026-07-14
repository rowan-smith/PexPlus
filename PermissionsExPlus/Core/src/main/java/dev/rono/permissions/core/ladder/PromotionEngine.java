package dev.rono.permissions.core.ladder;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.user.User;


public final class PromotionEngine {

    public Group promote(User user, Ladder ladder) {
        var groups = ladder.groups();

        int current = groups.indexOf(user);

        return groups.get(current + 1);
    }

    public Group demote(User user, Ladder ladder) {
        var groups = ladder.groups();

        int current = groups.indexOf(user);

        return groups.get(current - 1);
    }
}
