package dev.rono.permissions.api.options;

final class Options {

    private Options() {
        throw new AssertionError();
    }

    static OptionNodeBuilder builder() {
        return new OptionNodeBuilderImpl();
    }
}
