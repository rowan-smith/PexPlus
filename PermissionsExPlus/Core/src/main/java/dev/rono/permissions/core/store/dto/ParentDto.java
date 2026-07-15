package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "pex_parents", indexes = @Index(name = "idx_pex_parents_subject", columnList = "subject_type,subject_key"))
public class ParentDto {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "subject_type", nullable = false, length = 16)
    private String subjectType;

    @Column(name = "subject_key", nullable = false, length = 64)
    private String subjectKey;

    @Column(name = "group_name", nullable = false, length = 64)
    private String groupName;

    private Instant expiry;

    protected ParentDto() {}

    public ParentDto(String id, String subjectType, String subjectKey, String groupName, Instant expiry) {
        this.id = id;
        this.subjectType = subjectType;
        this.subjectKey = subjectKey;
        this.groupName = groupName;
        this.expiry = expiry;
    }

    public String id() {
        return id;
    }

    public String groupName() {
        return groupName;
    }

    public Instant expiry() {
        return expiry;
    }
}
