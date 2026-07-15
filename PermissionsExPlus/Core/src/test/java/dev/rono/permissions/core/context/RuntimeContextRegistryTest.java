package dev.rono.permissions.core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class RuntimeContextRegistryTest {

    @Test
    void suppliersAreEvaluatedDynamically() {
        var values = new AtomicReference<>(List.of("world"));
        var registry = new RuntimeContextRegistry();

        registry.registerContextType("WORLD", values::get);

        assertEquals(List.of("world"), registry.validValues("world"));

        values.set(List.of("world_nether", "world", "world_nether"));

        assertEquals(List.of("world", "world_nether"), registry.validValues("WORLD"));
    }

    @Test
    void keysAreNormalizedAndReturnedInStableOrder() {
        var registry = new RuntimeContextRegistry();

        registry.registerContextType("world", List::of);
        registry.registerContextType("faction", List::of);

        assertEquals(List.of("faction", "world"), registry.registeredKeys().stream().toList());
    }

    @Test
    void universalContextFlagIsReserved() {
        var registry = new RuntimeContextRegistry();

        assertThrows(IllegalArgumentException.class, () -> registry.registerContextType("context", () -> List.of("world=nether")));
    }

    @Test
    void registrationHandlesOnlyRemoveTheirOwnSupplier() {
        var registry = new RuntimeContextRegistry();

        var first = registry.registerContextType("flying", () -> List.of("false"));
        var second = registry.registerContextType("flying", () -> List.of("false", "true"));

        first.close();

        assertEquals(List.of("false", "true"), registry.validValues("flying"));

        second.close();

        assertFalse(registry.registeredKeys().contains("flying"));
    }
}
