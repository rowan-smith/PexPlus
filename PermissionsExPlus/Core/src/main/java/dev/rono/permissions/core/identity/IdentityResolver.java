package dev.rono.permissions.core.identity;

import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.logger.LoggerManagerImpl;
import dev.rono.permissions.core.manager.UserManagerImpl;
import dev.rono.permissions.core.platform.PlatformScheduler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Resolves stored, offline, and Mojang identities without blocking the platform
 * thread.
 */
public final class IdentityResolver {
    private static final Pattern MOJANG_ID = Pattern.compile("\\\"id\\\"\\s*:\\s*\\\"([0-9a-fA-F]{32})\\\"");

    private final UserManagerImpl users;
    private final PlatformScheduler scheduler;
    private final String source;
    private final LoggerManagerImpl logger;

    private final HttpClient http;
    private final URI endpoint;

    public IdentityResolver(UserManagerImpl users, PlatformScheduler scheduler, String source, LoggerManagerImpl logger) {
        this(users, scheduler, source, logger, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build(), URI.create("https://api.mojang.com/users/profiles/minecraft/"));
    }

    IdentityResolver(UserManagerImpl users, PlatformScheduler scheduler, String source, LoggerManagerImpl logger, HttpClient http, URI endpoint) {
        this.users = Objects.requireNonNull(users, "users");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
        this.source = Objects.requireNonNull(source, "source");
        this.logger = Objects.requireNonNull(logger, "logger");
        this.http = Objects.requireNonNull(http, "http");
        this.endpoint = Objects.requireNonNull(endpoint, "endpoint");
    }

    public void resolveAsync(String username, Consumer<Optional<UUID>> callback) {
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(callback, "callback");

        scheduler.executeAsync(() -> {
            var stored = users.find(username).toCompletableFuture().join();

            var mode = source.trim().toLowerCase(Locale.ROOT);

            var result = switch (mode) {
                case "offline", "offline_mode" -> offline(username);
                case "online", "online_only" -> mojang(username);
                case "mix", "mixed", "mix_mode" -> stored.map(User::uniqueId).or(() -> mojang(username));
                default -> stored.map(User::uniqueId);
            };

            scheduler.execute(() -> callback.accept(result));
        });
    }

    private Optional<UUID> offline(String username) {
        logger.debug().warn("Identity", "Offline UUID fallback used for username='" + username + "'");

        return Optional.of(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8)));
    }

    private Optional<UUID> mojang(String username) {
        try {
            var request = HttpRequest.newBuilder(endpoint.resolve(username)).timeout(Duration.ofSeconds(5)).GET().build();

            var response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return Optional.empty();
            }

            var match = MOJANG_ID.matcher(response.body());
            if (!match.find()) {
                return Optional.empty();
            }

            var id = match.group(1);

            return Optional.of(UUID.fromString(id.substring(0, 8) + '-' + id.substring(8, 12) + '-' + id.substring(12, 16) + '-' + id.substring(16, 20) + '-' + id.substring(20)));
        } catch (Exception error) {
            logger.debug().log("Identity", "Mojang profile request for username='" + username + "' failed: " + error.getClass().getSimpleName());

            return Optional.empty();
        }
    }
}
