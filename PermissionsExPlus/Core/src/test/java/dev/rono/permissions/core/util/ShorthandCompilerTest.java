package dev.rono.permissions.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

class ShorthandCompilerTest {
    @Test
    void expandsTrimmedStringLists() {
        assertEquals(List.of("essentials.kits.vip", "essentials.kits.mvp", "essentials.kits.donor"), ShorthandCompiler.expand("essentials.kits.{vip, mvp,donor}"));
    }

    @Test
    void expandsAscendingDescendingAndNegativeNumericRanges() {
        assertEquals(List.of("plot.1", "plot.2", "plot.3"), ShorthandCompiler.expand("plot.{1-3}"));
        assertEquals(List.of("plot.3", "plot.2", "plot.1"), ShorthandCompiler.expand("plot.{3-1}"));
        assertEquals(List.of("plot.-2", "plot.-1", "plot.0", "plot.1"), ShorthandCompiler.expand("plot.{-2-1}"));
    }

    @Test
    void expandsAscendingAndDescendingCharacterRangesWithoutChangingCase() {
        assertEquals(List.of("sector.a", "sector.b", "sector.c"), ShorthandCompiler.expand("sector.{a-c}"));
        assertEquals(List.of("sector.C", "sector.B", "sector.A"), ShorthandCompiler.expand("sector.{C-A}"));
    }

    @Test
    void recursivelyBuildsChainedCrossProductsInStableOrder() {
        assertEquals(List.of(
                "network.survival.kit.1",
                "network.survival.kit.2",
                "network.survival.kit.3",
                "network.skyblock.kit.1",
                "network.skyblock.kit.2",
                "network.skyblock.kit.3"),
                ShorthandCompiler.expand("network.{survival,skyblock}.kit.{1-3}"));
    }

    @Test
    void malformedAndEscapedTokensRemainLiteralWhileLaterValidTokensExpand() {
        assertEquals(List.of("literal.{single}.1", "literal.{single}.2"), ShorthandCompiler.expand("literal.{single}.{1-2}"));
        assertEquals(List.of("literal.\\{a,b}.1", "literal.\\{a,b}.2"), ShorthandCompiler.expand("literal.\\{a,b}.{1-2}"));

        for (var literal : List.of("node.{a,}", "node.{A-c}", "node.{1-}", "node.{1-2")) {
            assertEquals(List.of(literal), ShorthandCompiler.expand(literal));
        }
    }

    @Test
    void excessiveCrossProductsFallBackToTheLiteralInput() {
        var input = "node.{1-101}.{1-100}";

        assertEquals(List.of(input), ShorthandCompiler.expand(input));
    }
}
