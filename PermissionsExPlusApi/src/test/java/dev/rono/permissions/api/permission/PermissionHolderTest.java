package dev.rono.permissions.api.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.options.OptionNode;
import dev.rono.permissions.api.resolver.PermissionResolution;
import dev.rono.permissions.api.resolver.PermissionResolver;
import dev.rono.permissions.api.resolver.QueryOptions;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PermissionHolderTest {

    @Test
    void explicitPermissionLookupUsesPermissionAndExactContexts() {
        var nether = ContextSet.builder().add("world", "nether").build();

        PermissionHolder holder = new TestHolder(Set.of(
                PermissionNode.builder().permission("example.test").build(),
                PermissionNode.builder()
                        .permission("example.test")
                        .value(PermissionValue.DENY)
                        .contexts(nether)
                        .build()),
                Set.of());

        assertTrue(holder.explicitlyAllows("EXAMPLE.TEST", ContextSet.empty()));

        assertTrue(holder.explicitlyDenies("example.test", nether));

        assertFalse(holder.explicitlyAllows("example.test", nether));

        assertTrue(holder.explicitPermission("missing.permission").isEmpty());
    }

    @Test
    void permissionResolverBooleanConvenienceOnlyAllowsAllowResult() {
        PermissionResolver allow = resolver(PermissionResult.ALLOW);

        PermissionResolver deny = resolver(PermissionResult.DENY);

        PermissionResolver undefined = resolver(PermissionResult.UNDEFINED);

        PermissionHolder holder = new TestHolder(Set.of(), Set.of());

        assertTrue(allow.hasPermission(holder, "example.test", ContextSet.empty()));

        assertFalse(deny.hasPermission(holder, "example.test", ContextSet.empty()));

        assertFalse(undefined.hasPermission(holder, "example.test", ContextSet.empty()));
    }

    @Test
    void nodeIdentifiersAreNormalized() {
        var node = PermissionNode.builder().permission("Example.TEST").build();

        assertEquals("example.test", node.permission());
    }

    private static PermissionResolver resolver(PermissionResult result) {
        return new PermissionResolver() {
            @Override
            public PermissionResult check(PermissionHolder holder, String permission, QueryOptions options) {
                return result;
            }

            @Override
            public PermissionResolution explain(PermissionHolder holder, String permission, QueryOptions options) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private record TestHolder(
            Set<PermissionNode> explicitPermissions,
            Set<OptionNode> explicitOptions) implements PermissionHolder {}
}
