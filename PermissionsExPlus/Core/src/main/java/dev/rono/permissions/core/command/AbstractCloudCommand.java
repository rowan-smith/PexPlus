package dev.rono.permissions.core.command;

public abstract class AbstractCloudCommand<C> {

    protected final CoreCloudCommandContext<C> ctx;

    protected AbstractCloudCommand(final CoreCloudCommandContext<C> ctx) {
        this.ctx = ctx;
    }

    protected void reply(final C sender, final String message) {
        ctx.send(sender, message);
    }
}
