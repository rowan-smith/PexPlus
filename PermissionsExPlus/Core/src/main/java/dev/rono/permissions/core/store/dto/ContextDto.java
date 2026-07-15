package dev.rono.permissions.core.store.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "pex_contexts", indexes = @Index(name = "idx_pex_contexts_node", columnList = "node_type,node_id"))
public class ContextDto {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "node_type", nullable = false, length = 16)
    private String nodeType;

    @Column(name = "node_id", nullable = false, length = 36)
    private String nodeId;

    @Column(name = "context_key", nullable = false, length = 64)
    private String contextKey;

    @Column(name = "context_value", nullable = false, length = 255)
    private String contextValue;

    protected ContextDto() {}

    public ContextDto(String id, String nodeType, String nodeId, String contextKey, String contextValue) {
        this.id = id;
        this.nodeType = nodeType;
        this.nodeId = nodeId;
        this.contextKey = contextKey;
        this.contextValue = contextValue;
    }

    public String contextKey() {
        return contextKey;
    }

    public String contextValue() {
        return contextValue;
    }
}
