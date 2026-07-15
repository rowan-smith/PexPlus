package dev.rono.permissions.api.options;

/**
 * Built-in metadata keys.
 *
 * <p>
 * API 1.0 intentionally uses a single effective prefix and suffix value.
 * Prefix/suffix nodes do not stack and have no numeric priority; normal option
 * precedence selects one applicable value. They may still be contextual and
 * temporary.
 * </p>
 */
public final class OptionKeys {
    public static final String PREFIX = "prefix";

    public static final String SUFFIX = "suffix";

    public static final String DISPLAY_NAME = "display-name";

    private OptionKeys() {
        throw new AssertionError();
    }
}
