package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "pex_permissions", indexes = @Index(name = "idx_pex_permissions_subject", columnList = "subject_type,subject_key"))
public class PermissionDto {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "subject_type", nullable = false, length = 16)
    private String subjectType;

    @Column(name = "subject_key", nullable = false, length = 64)
    private String subjectKey;

    @Column(nullable = false, length = 255)
    private String permission;

    @Column(name = "permission_value", nullable = false, length = 16)
    private String value;

    private Instant expiry;

    protected PermissionDto() {}

    public PermissionDto(String id, String subjectType, String subjectKey, String permission, String value, Instant expiry) {
        this.id = id;
        this.subjectType = subjectType;
        this.subjectKey = subjectKey;
        this.permission = permission;
        this.value = value;
        this.expiry = expiry;
    }

    public String id() {
        return id;
    }

    public String permission() {
        return permission;
    }

    public String value() {
        return value;
    }

    public Instant expiry() {
        return expiry;
    }
}
