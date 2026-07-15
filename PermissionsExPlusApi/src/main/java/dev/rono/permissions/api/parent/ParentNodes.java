package dev.rono.permissions.api.parent;

final class ParentNodes {

    private ParentNodes() {
        throw new AssertionError();
    }

    static ParentNodeBuilder builder() {
        return new ParentNodeBuilderImpl();
    }
}
