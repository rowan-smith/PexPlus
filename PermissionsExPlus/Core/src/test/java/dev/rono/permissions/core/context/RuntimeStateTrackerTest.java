package dev.rono.permissions.core.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import dev.rono.permissions.api.context.ContextKeys;
import dev.rono.permissions.core.config.AdvancedConfiguration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RuntimeStateTrackerTest {

    @Test
    void perKeyUpdatesRetainOtherTrackedState() {
        var tracker = tracker(true, true, true, false);
        var subject = UUID.randomUUID();

        tracker.updateState(subject, ContextKeys.WORLD, "world");
        tracker.updateState(subject, ContextKeys.GAMEMODE, "survival");
        tracker.updateState(subject, ContextKeys.WORLD, "world_nether");

        var contexts = tracker.contexts(subject);

        assertTrue(contexts.contains(ContextKeys.WORLD, "world_nether"));
        assertTrue(contexts.contains(ContextKeys.GAMEMODE, "survival"));
        assertFalse(contexts.contains(ContextKeys.WORLD, "world"));
    }

    @Test
    void disabledAndThirdPartyKeysCannotEnterOfficialStateCache() {
        var tracker = tracker(true, false, false, false);
        var subject = UUID.randomUUID();

        tracker.replaceState(subject, Map.of(ContextKeys.WORLD, "world", ContextKeys.GAMEMODE, "creative", "faction", "claimed"));

        assertEquals(Map.of(ContextKeys.WORLD, Set.of("world")), tracker.contexts(subject).asMap());
    }

    @Test
    void clearReleasesTheEntirePlayerAllocation() {
        var tracker = tracker(true, true, true, true);
        var subject = UUID.randomUUID();

        tracker.updateState(subject, ContextKeys.WORLD, "world");
        tracker.clearState(subject);

        assertTrue(tracker.contexts(subject).isEmpty());
    }

    private static RuntimeStateTracker tracker(boolean worlds, boolean gameModes, boolean servers, boolean proxies) {
        var configuration = new AdvancedConfiguration("none", 10, 30, "offline", "global", worlds, servers, gameModes, proxies, Map.of(), 10, "localhost", 6379, "", "channel", 2000, true);
        return new RuntimeStateTracker(new ContextPolicy(configuration));
    }
}
