package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "pex_ladder_groups", indexes = @Index(name = "idx_pex_ladder_groups_ladder", columnList = "ladder_name,position"))
public class LadderGroupDto {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "ladder_name", nullable = false, length = 64)
    private String ladderName;

    @Column(name = "group_name", nullable = false, length = 64)
    private String groupName;

    @Column(nullable = false)
    private int position;

    protected LadderGroupDto() {}

    public LadderGroupDto(String id, String ladderName, String groupName, int position) {
        this.id = id;
        this.ladderName = ladderName;
        this.groupName = groupName;
        this.position = position;
    }

    public String groupName() {
        return groupName;
    }

    public int position() {
        return position;
    }
}
