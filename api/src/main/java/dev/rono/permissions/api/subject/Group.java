package dev.rono.permissions.api.subject;

import java.util.List;

/** Modern view of a permission group. */
public interface Group extends PermissionSubject {

    @Override
    default SubjectType type() {
        return SubjectType.GROUP;
    }

    int weight();

    void setWeight(int weight);

    boolean isDefault(String world);

    void setDefault(boolean value, String world);

    /** Direct parent group identifiers in {@code world}. */
    List<String> parents(String world);

    /** Effective parent groups (inheritance expanded) in {@code world}. */
    List<String> parentTree(String world);

    void addParent(String parentName, String world);

    void removeParent(String parentName, String world);

    void setParents(List<String> parentNames, String world);

    boolean isChildOf(String groupName, String world, boolean inherit);

    int rank();

    String rankLadder();

    void setRank(int rank, String ladder);
}
