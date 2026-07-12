package dev.rono.permissions.api.permission;

import java.util.Optional;

public final class PermissionResult {

    private static final PermissionResult ALLOW =
            new PermissionResult(State.ALLOW, null, null, null);
    private static final PermissionResult DENY =
            new PermissionResult(State.DENY, null, null, null);
    private static final PermissionResult UNDEFINED =
            new PermissionResult(State.UNDEFINED, null, null, null);

    private final State state;
    private final PermissionHolder source;
    private final String permission;
    private final PermissionContext context;

    private PermissionResult(State state, PermissionHolder source, String permission, PermissionContext context) {
        this.state = state;
        this.source = source;
        this.permission = permission;
        this.context = context;
    }

    public State state() {
        return state;
    }

    public Optional<PermissionHolder> source() {
        return Optional.ofNullable(source);
    }

    public Optional<String> permission() {
        return Optional.ofNullable(permission);
    }

    public Optional<PermissionContext> context() {
        return Optional.ofNullable(context);
    }

    public boolean isAllow() {
        return state == State.ALLOW || state == State.INHERITED;
    }

    public boolean isDeny() {
        return state == State.DENY;
    }

    public boolean isUndefined() {
        return state == State.UNDEFINED;
    }

    public static PermissionResult allow(PermissionHolder source, String permission, PermissionContext context) {
        return new PermissionResult(State.ALLOW, source, permission, context);
    }

    public static PermissionResult deny(PermissionHolder source, String permission, PermissionContext context) {
        return new PermissionResult(State.DENY, source, permission, context);
    }

    public static PermissionResult inherited(PermissionHolder source, String permission, PermissionContext context) {
        return new PermissionResult(State.INHERITED, source, permission, context);
    }

    public static PermissionResult undefined() {
        return UNDEFINED;
    }

    public static PermissionResult allow() {
        return ALLOW;
    }

    public static PermissionResult deny() {
        return DENY;
    }

    public enum State {
        ALLOW,
        DENY,
        INHERITED,
        UNDEFINED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionResult that)) return false;
        return state == that.state;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return "PermissionResult{state=" + state + "}";
    }

}
