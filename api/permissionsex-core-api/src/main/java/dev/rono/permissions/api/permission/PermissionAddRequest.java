package dev.rono.permissions.api.permission;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder request for advanced permission additions (duration, expiry, context, source).
 *
 * <p>Preferred holder-based write path — use this instead of shorter {@code addPermission} overloads
 * when world scope, expiry, or source metadata matter.</p>
 */
public final class PermissionAddRequest {

    private final PermissionHolder holder;
    private final String permission;
    private final Duration duration;
    private final Instant expiresAt;
    private final Map<String, String> context;
    private final PermissionSource source;

    private PermissionAddRequest(Builder builder) {
        this.holder = builder.holder;
        this.permission = builder.permission;
        this.duration = builder.duration;
        this.expiresAt = builder.expiresAt;
        this.context = Map.copyOf(builder.context);
        this.source = builder.source;
    }

    /** @return holder that receives the grant */
    public PermissionHolder holder() {
        return holder;
    }

    /** @return permission node expression to grant */
    public String permission() {
        return permission;
    }

    /** @return relative duration when specified at build time; may be {@code null} when {@link #expiresAt()} was set directly */
    public Duration duration() {
        return duration;
    }

    /** @return absolute expiry instant resolved at build time; {@code null} for permanent grants */
    public Instant expiresAt() {
        return expiresAt;
    }

    /** @return immutable world/context map for scoped grants */
    public Map<String, String> context() {
        return context;
    }

    /** @return provenance metadata for the grant */
    public PermissionSource source() {
        return source;
    }

    /**
     * Creates a new builder for a holder permission add request.
     *
     * @return new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for {@link PermissionAddRequest}.
     */
    public static final class Builder {

        private PermissionHolder holder;
        private String permission;
        private Duration duration;
        private Instant expiresAt;
        private Map<String, String> context = new HashMap<>();
        private PermissionSource source = PermissionSource.SYSTEM;

        /** @param holder permission target; required */
        public Builder holder(PermissionHolder holder) {
            this.holder = holder;
            return this;
        }

        /** @param permission node to grant; required, non-empty */
        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        /** @param duration relative lifetime; mutually exclusive with {@link #expiresAt(Instant)} */
        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        /** @param expiresAt absolute expiry; mutually exclusive with {@link #duration(Duration)} */
        public Builder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        /**
         * Adds a context entry for world-scoped grants.
         *
         * @param key context map key (for example {@code "world"})
         * @param value context value
         */
        public Builder addContext(String key, String value) {
            this.context.put(key, value);
            return this;
        }

        /** @param source provenance metadata; defaults to {@link PermissionSource#SYSTEM} */
        public Builder source(PermissionSource source) {
            this.source = source;
            return this;
        }

        /**
         * Builds the add request.
         *
         * @return immutable request
         * @throws IllegalStateException if holder or permission is missing, or both duration and expiresAt are set
         */
        public PermissionAddRequest build() {
            if (holder == null) {
                throw new IllegalStateException("Holder cannot be null");
            }
            if (permission == null || permission.isEmpty()) {
                throw new IllegalStateException("Permission cannot be null or empty");
            }
            if (duration != null && expiresAt != null) {
                throw new IllegalStateException("Use either duration or expiresAt, not both");
            }
            Instant resolvedExpiresAt = expiresAt;
            if (duration != null && resolvedExpiresAt == null) {
                resolvedExpiresAt = Instant.now().plus(duration);
            }
            Builder copy = new Builder();
            copy.holder = holder;
            copy.permission = permission;
            copy.duration = duration;
            copy.expiresAt = resolvedExpiresAt;
            copy.context = new HashMap<>(context);
            copy.source = source;
            return new PermissionAddRequest(copy);
        }
    }
}
