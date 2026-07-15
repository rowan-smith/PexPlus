package dev.rono.permissions.api.resolver;

import java.util.Map;
import java.util.Optional;

/** Immutable effective options, prefix, and suffix for one query. */
public interface ResolvedMetaData {

    Optional<String> option(String key);

    Optional<String> prefix();

    Optional<String> suffix();

    Map<String, String> options();
}
