package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pex_ladders")
public class LadderDto {
    @Id
    @Column(length = 64)
    private String name;

    protected LadderDto() {}

    public LadderDto(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
