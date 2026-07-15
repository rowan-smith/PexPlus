package dev.rono.permissions.api.resolver;

/** An immutable effective-data snapshot for one holder and query. */
public interface ResolvedData {

    QueryOptions queryOptions();

    ResolvedPermissionData permissions();

    ResolvedMetaData meta();
}
