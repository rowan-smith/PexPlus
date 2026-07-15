package dev.rono.permissions.api.ladder;

import java.util.List;

public interface LadderModifier {

    LadderModifier add(String groupName);

    LadderModifier insert(int position, String groupName);

    LadderModifier remove(String groupName);

    LadderModifier move(String groupName, int position);

    LadderModifier setGroups(List<String> groups);

    LadderModifier clear();
}
