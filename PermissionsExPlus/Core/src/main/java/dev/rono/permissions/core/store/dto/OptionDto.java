package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "pex_options", indexes = @Index(name = "idx_pex_options_subject", columnList = "subject_type,subject_key"))
public class OptionDto {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "subject_type", nullable = false, length = 16)
    private String subjectType;

    @Column(name = "subject_key", nullable = false, length = 64)
    private String subjectKey;

    @Column(name = "option_key", nullable = false, length = 64)
    private String optionKey;

    @Column(name = "option_value", nullable = false, length = 2048)
    private String optionValue;

    private Instant expiry;

    protected OptionDto() {}

    public OptionDto(String id, String subjectType, String subjectKey, String optionKey, String optionValue, Instant expiry) {
        this.id = id;
        this.subjectType = subjectType;
        this.subjectKey = subjectKey;
        this.optionKey = optionKey;
        this.optionValue = optionValue;
        this.expiry = expiry;
    }

    public String id() {
        return id;
    }

    public String optionKey() {
        return optionKey;
    }

    public String optionValue() {
        return optionValue;
    }

    public Instant expiry() {
        return expiry;
    }
}
