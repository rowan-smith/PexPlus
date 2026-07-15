package dev.rono.permissions.api.permission;

public interface NamedPermissionHolder extends PermissionHolder {
    /**
     * Returns the name of the holder.
     *
     * @return the name of this holder
     */
    String name();
}
