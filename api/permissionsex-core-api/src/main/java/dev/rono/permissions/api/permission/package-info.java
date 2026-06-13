/**
 * Holder-based permission identities, add requests, and check context keys.
 *
 * <p>Used by the legacy/holder bridge on {@link ru.tehkode.permissions.PermissionManager} and by
 * {@link dev.rono.permissions.api.permission.PermissionAddRequest}. Subject-centric plugins
 * typically use {@code String world} on {@link dev.rono.permissions.api.subject.PermissionSubject}
 * instead of context maps.</p>
 */
package dev.rono.permissions.api.permission;
