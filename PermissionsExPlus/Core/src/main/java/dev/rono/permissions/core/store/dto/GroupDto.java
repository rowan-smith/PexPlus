package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pex_groups")
public class GroupDto {
    @Id
    @Column(length = 64)
    private String name;

    private Integer weight;

    protected GroupDto() {}

    public GroupDto(String name, Integer weight) {
        this.name = name;
        this.weight = weight;
    }

    public String name() {
        return name;
    }

    public Integer weight() {
        return weight;
    }
}
