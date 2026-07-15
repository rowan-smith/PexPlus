package dev.rono.permissions.core.manager;

import java.util.Objects;

/** One expired node removed from a cached permission holder. */
public record ExpiryRemoval(String subjectType, String subject, String nodeType, String node) {
    public ExpiryRemoval {
        Objects.requireNonNull(subjectType, "subjectType");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(nodeType, "nodeType");
        Objects.requireNonNull(node, "node");
    }
}
