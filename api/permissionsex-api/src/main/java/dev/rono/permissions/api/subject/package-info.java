/**
 * Permission subject model: shared read/write operations for users and groups.
 *
 * <p>{@link dev.rono.permissions.api.subject.PermissionSubject} composes internal roles
 * ({@link dev.rono.permissions.api.subject.SubjectIdentity},
 * {@link dev.rono.permissions.api.subject.PermissionView},
 * {@link dev.rono.permissions.api.subject.PermissionMutator}). World-bound facades live in
 * {@link dev.rono.permissions.api.subject.SubjectWorldContext} and typed variants.
 * Server-bound facades ({@link dev.rono.permissions.api.subject.SubjectServerContext}) use the same
 * storage namespace; on proxies they represent backend server ids.</p>
 */
package dev.rono.permissions.api.subject;
