package dev.rono.permissions.core.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.meta.SimpleCommandMeta;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.command.commands.*;
import dev.rono.permissions.core.command.parser.GroupParser;
import dev.rono.permissions.core.command.parser.LadderParser;
import dev.rono.permissions.core.command.parser.RealmParser;
import dev.rono.permissions.core.command.parser.UserParser;
import dev.rono.permissions.core.command.suggestions.CoreCloudSuggestions;
import io.leangen.geantyref.TypeToken;

import java.util.function.BiConsumer;

public final class CloudCommandBootstrap {

    public static <C> void bootstrap(final CommandManager<C> manager, final Class<C> senderType, final PermissionsExPlusApi api, final BiConsumer<C, String> messageSender) {
        final var ctx = new CoreCloudCommandContext<>(manager, api, messageSender);

        registerParsers(manager, api);

        final var annotationParser = new AnnotationParser<>(
                manager,
                senderType,
                parameters -> SimpleCommandMeta.empty()
        );

        annotationParser.parse(new CoreCloudSuggestions<>(ctx));
        annotationParser.parse(new RootCommand<>(ctx));
        annotationParser.parse(new UserCommand<>(ctx));
        annotationParser.parse(new GroupCommand<>(ctx));
        annotationParser.parse(new LadderCommand<>(ctx));
        annotationParser.parse(new BackendCommand<>(ctx));
        annotationParser.parse(new ReloadCommand<>(ctx));
        annotationParser.parse(new VersionCommand<>(ctx));
        annotationParser.parse(new DebugCommand<>(ctx));
    }

    private static <C> void registerParsers(final CommandManager<C> manager, final PermissionsExPlusApi api) {
        final var registry = manager.parserRegistry();
        registry.registerParserSupplier(TypeToken.get(User.class), parameters -> new UserParser<>(api));
        registry.registerParserSupplier(TypeToken.get(Group.class), parameters -> new GroupParser<>(api));
        registry.registerParserSupplier(TypeToken.get(Ladder.class), parameters -> new LadderParser<>(api));
        registry.registerParserSupplier(TypeToken.get(Realm.class), parameters -> new RealmParser<>(api));
    }
}
