package dev.rono.permissions.core.resolver;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.options.OptionKeys;
import dev.rono.permissions.api.options.OptionNode;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.permission.PermissionValue;
import dev.rono.permissions.api.resolver.CandidateStatus;
import dev.rono.permissions.api.resolver.DefaultGroupResolver;
import dev.rono.permissions.api.resolver.InheritanceResolver;
import dev.rono.permissions.api.resolver.OptionResolver;
import dev.rono.permissions.api.resolver.PermissionResolution;
import dev.rono.permissions.api.resolver.PermissionResolver;
import dev.rono.permissions.api.resolver.PrimaryGroupResolver;
import dev.rono.permissions.api.resolver.QueryOptions;
import dev.rono.permissions.api.resolver.ResolutionCandidate;
import dev.rono.permissions.api.resolver.ResolvedData;
import dev.rono.permissions.api.resolver.ResolvedMetaData;
import dev.rono.permissions.api.resolver.ResolvedPermissionData;
import dev.rono.permissions.api.resolver.ResolvedUserData;
import dev.rono.permissions.api.resolver.Resolvers;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.api.util.Identifiers;
import dev.rono.permissions.api.util.Node;
import dev.rono.permissions.core.config.MetaFormatting;
import dev.rono.permissions.core.config.PermissionConflictResolution;
import dev.rono.permissions.core.manager.GroupManagerImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public final class ResolverImpl implements Resolvers, PermissionResolver, OptionResolver, InheritanceResolver, DefaultGroupResolver {
    private final GroupManagerImpl groups;
    private final int maxDepth;
    private final boolean caseSensitive, wildcards, negations;
    private final String defaultGroup;
    private final PermissionConflictResolution conflictResolution;
    private final MetaFormatting metaFormatting;
    private final Consumer<String> conflictWarning;

    public ResolverImpl(GroupManagerImpl groups, int maxDepth) {
        this(groups, maxDepth, false, true, true, "default");
    }

    public ResolverImpl(
            GroupManagerImpl groups,
            int maxDepth,
            boolean caseSensitive,
            boolean wildcards,
            boolean negations) {

        this(groups, maxDepth, caseSensitive, wildcards, negations, "default");
    }

    public ResolverImpl(
            GroupManagerImpl groups,
            int maxDepth,
            boolean caseSensitive,
            boolean wildcards,
            boolean negations,
            String defaultGroup) {

        this(groups, maxDepth, caseSensitive, wildcards, negations, defaultGroup, PermissionConflictResolution.DENY_WINS, MetaFormatting.HIGHEST_WEIGHT, ignored -> {});
    }

    public ResolverImpl(
            GroupManagerImpl groups,
            int maxDepth,
            boolean caseSensitive,
            boolean wildcards,
            boolean negations,
            String defaultGroup,
            PermissionConflictResolution conflictResolution,
            MetaFormatting metaFormatting,
            Consumer<String> conflictWarning) {

        this.groups = groups;
        this.maxDepth = Math.max(1, maxDepth);
        this.caseSensitive = caseSensitive;
        this.wildcards = wildcards;
        this.negations = negations;
        this.defaultGroup = Identifiers.group(defaultGroup);
        this.conflictResolution = Objects.requireNonNull(conflictResolution, "conflictResolution");
        this.metaFormatting = Objects.requireNonNull(metaFormatting, "metaFormatting");
        this.conflictWarning = Objects.requireNonNull(conflictWarning, "conflictWarning");
    }

    @Override
    public ResolvedData resolve(PermissionHolder holder, QueryOptions options) {
        Objects.requireNonNull(holder, "holder");
        Objects.requireNonNull(options, "options");

        return new Data(options, permissionData(holder, options), metaData(holder, options));
    }

    @Override
    public ResolvedUserData resolve(User user, QueryOptions options) {
        return new UserData(options, permissionData(user, options), metaData(user, options), groups(user, options), resolvePrimary(user, options));
    }

    @Override
    public PermissionResolver permissions() {
        return this;
    }

    @Override
    public OptionResolver options() {
        return this;
    }

    @Override
    public InheritanceResolver inheritance() {
        return this;
    }

    @Override
    public PrimaryGroupResolver primaryGroup() {
        return this::resolvePrimary;
    }

    @Override
    public DefaultGroupResolver defaultGroups() {
        return this;
    }

    @Override
    public PermissionResult check(PermissionHolder holder, String permission, QueryOptions options) {
        return explain(holder, permission, options).result();
    }

    @Override
    public PermissionResolution explain(PermissionHolder holder, String requested, QueryOptions options) {
        Objects.requireNonNull(holder, "holder");
        Objects.requireNonNull(options, "options");
        Objects.requireNonNull(requested, "requested");

        var permission = caseSensitive ? requested.trim() : Identifiers.permission(requested);
        var candidates = new ArrayList<Candidate>();

        for (var source : sources(holder, options)) {
            for (var node : source.holder.explicitPermissions()) {
                CandidateStatus status;

                String detail = null;

                if (node.expired()) {
                    status = CandidateStatus.EXPIRED;
                    detail = "node expired";
                } else if (!applies(node.contexts(), options.contexts())) {
                    status = CandidateStatus.CONTEXT_MISMATCH;
                    detail = "node contexts are not active";
                } else if (!matches(expression(node), permission)) {
                    status = CandidateStatus.PERMISSION_MISMATCH;
                } else if (source.excluded != null) {
                    status = source.excluded;
                    detail = source.detail;
                } else {
                    status = CandidateStatus.OUTRANKED;
                }

                candidates.add(new Candidate(node, source.holder, source.distance, specificity(node.contexts()), status, Optional.ofNullable(detail), source.weight));
            }
        }

        var resolution = resolveCandidates(permission, candidates.stream().filter(value -> value.status == CandidateStatus.OUTRANKED).toList());
        return new Resolution(resolution.map(Candidate::result).orElse(PermissionResult.UNDEFINED), permission, resolution.map(value -> (ResolutionCandidate) value), List.copyOf(candidates));
    }

    @Override
    public Optional<String> resolve(PermissionHolder holder, String key, QueryOptions options) {
        var normalized = Identifiers.optionKey(key);
        var candidates = optionCandidates(holder, normalized, options);

        if (metaFormatting == MetaFormatting.ACCUMULATED && (OptionKeys.PREFIX.equals(normalized) || OptionKeys.SUFFIX.equals(normalized))) {
            var accumulated = candidates.stream()
                    .sorted(optionComparator().reversed().thenComparing(OptionCandidate::source))
                    .map(candidate -> candidate.node.value()).reduce("", String::concat);

            return accumulated.isEmpty() ? Optional.empty() : Optional.of(accumulated);
        }

        return candidates.stream().max(optionComparator().thenComparing(OptionCandidate::source))
                .map(candidate -> candidate.node.value());
    }

    @Override
    public Set<Group> groups(User user, QueryOptions options) {
        var result = new LinkedHashSet<Group>();
        var direct = applicableMemberships(user, options.contexts());

        if (user.groups().isEmpty() && options.includeDefaults()) {
            resolve().ifPresent(result::add);
        } else {
            direct.forEach(node -> groups.cache().get(node.group()).ifPresent(result::add));
        }

        if (options.includeInheritance()) {
            for (var group : new ArrayList<>(result)) {
                collectParents(group, options.contexts(), result, new HashSet<>(), 0);
            }
        }

        return Set.copyOf(result);
    }

    @Override
    public Set<Group> parents(Group group, QueryOptions options) {
        var result = new LinkedHashSet<Group>();

        for (var parent : group.parents()) {
            if (applicable(parent, options.contexts())) {
                groups.cache().get(parent.group()).ifPresent(result::add);
            }
        }

        if (options.includeInheritance()) {
            for (var direct : new ArrayList<>(result)) {
                collectParents(direct, options.contexts(), result, new HashSet<>(), 0);
            }
        }

        return Set.copyOf(result);
    }

    @Override
    public boolean inherits(User user, String group, QueryOptions options) {
        var key = Identifiers.group(group);

        return groups(user, options).stream().anyMatch(value -> value.name().equals(key));
    }

    @Override
    public boolean inherits(Group group, String parent, QueryOptions options) {
        var key = Identifiers.group(parent);

        return parents(group, options).stream().anyMatch(value -> value.name().equals(key));
    }

    private Optional<Group> resolvePrimary(User user, QueryOptions options) {
        var direct = applicableMemberships(user, options.contexts()).stream()
                .map(node -> groups.cache().get(node.group()).orElse(null)).filter(Objects::nonNull).toList();

        var value = highest(direct);
        if (value.isPresent()) {
            return value;
        }

        if (options.includeInheritance()) {
            value = highest(groups(user, QueryOptions.builder(options).includeDefaults(false).build()));
            if (value.isPresent()) {
                return value;
            }
        }

        return user.groups().isEmpty() && options.includeDefaults() ? resolve() : Optional.empty();
    }

    @Override
    public Optional<Group> resolve() {
        return groups.cache().get(defaultGroup);
    }

    private List<Source> sources(PermissionHolder holder, QueryOptions options) {
        var result = new ArrayList<Source>();

        result.add(new Source(holder, 0, weight(holder), null, null));

        if (holder instanceof User user) {
            var direct = applicableMemberships(user, options.contexts());

            if (user.groups().isEmpty()) {
                resolve().ifPresent(group -> addGroupSource(group, 1, options, result, new HashSet<>(), options.includeDefaults() ? null : CandidateStatus.DEFAULTS_DISABLED));
            } else {
                for (var membership : direct) {
                    groups.cache().get(membership.group()).ifPresent(group -> addGroupSource(group, 1, options, result, new HashSet<>(), null));
                }
            }
        } else if (holder instanceof Group group) {
            for (var parent : group.parents()) {
                if (applicable(parent, options.contexts())) {
                    groups.cache().get(parent.group()).ifPresent(value -> addGroupSource(value, 1, options, result, new HashSet<>(), null));
                }
            }
        }

        return result;
    }

    private void addGroupSource(Group group, int distance, QueryOptions options, List<Source> result, Set<String> visited, CandidateStatus inheritedStatus) {
        if (distance > maxDepth || !visited.add(group.name())) {
            return;
        }

        var status = inheritedStatus != null ? inheritedStatus : distance > 1 && !options.includeInheritance() ? CandidateStatus.INHERITANCE_DISABLED : null;

        result.add(new Source(group, distance, group.weight().orElse(0), status,
                status == CandidateStatus.INHERITANCE_DISABLED ? "inheritance disabled" : status == CandidateStatus.DEFAULTS_DISABLED ? "defaults disabled" : null));

        for (var parent : group.parents()) {
            if (applicable(parent, options.contexts())) {
                groups.cache().get(parent.group()).ifPresent(value -> addGroupSource(value, distance + 1, options, result, visited, status));
            }
        }
    }

    private List<OptionCandidate> optionCandidates(PermissionHolder holder, String key, QueryOptions options) {
        var values = new ArrayList<OptionCandidate>();

        for (var source : sources(holder, options)) {
            if (source.excluded == null) {
                for (var node : source.holder.explicitOptions()) {
                    if (!node.expired() && node.key().equals(key) && applies(node.contexts(), options.contexts())) {
                        values.add(new OptionCandidate(node, source.distance, specificity(node.contexts()), source.weight, sourceKey(source.holder)));
                    }
                }
            }
        }

        return values;
    }

    private ResolvedPermissionData permissionData(PermissionHolder holder, QueryOptions options) {
        var map = new LinkedHashMap<String, PermissionResult>();

        var expressions = sources(holder, options).stream().filter(source -> source.excluded == null)
                .flatMap(source -> source.holder.explicitPermissions().stream())
                .filter(node -> !node.expired() && applies(node.contexts(), options.contexts()))
                .map(PermissionNode::permission).distinct().toList();

        for (var expression : expressions) {
            map.put(expression, check(holder, expression, options));
        }

        return new PermissionData(holder, options, Map.copyOf(map), this);
    }

    private ResolvedMetaData metaData(PermissionHolder holder, QueryOptions options) {
        var keys = sources(holder, options).stream().filter(source -> source.excluded == null)
                .flatMap(source -> source.holder.explicitOptions().stream())
                .filter(node -> !node.expired() && applies(node.contexts(), options.contexts())).map(OptionNode::key)
                .distinct().toList();

        var map = new LinkedHashMap<String, String>();

        for (var key : keys) {
            resolve(holder, key, options).ifPresent(value -> map.put(key, value));
        }

        return new MetaData(Map.copyOf(map));
    }

    private void collectParents(Group group, ContextSet contexts, Set<Group> result, Set<String> visited, int depth) {
        if (depth >= maxDepth || !visited.add(group.name())) {
            return;
        }

        for (var parent : group.parents()) {
            if (applicable(parent, contexts)) {
                groups.cache().get(parent.group()).ifPresent(value -> {
                    if (result.add(value)) {
                        collectParents(value, contexts, result, visited, depth + 1);
                    }
                });
            }
        }
    }

    private static List<ParentNode> applicableMemberships(User user, ContextSet contexts) {
        return user.groups().stream().filter(node -> applicable(node, contexts)).toList();
    }

    private static boolean applicable(Node node, ContextSet contexts) {
        return !node.expired() && applies(node.contexts(), contexts);
    }

    private static boolean applies(ContextSet required, ContextSet active) {
        return required.asMap().entrySet().stream()
                .allMatch(entry -> active.values(entry.getKey()).containsAll(entry.getValue()));
    }

    private static int specificity(ContextSet contexts) {
        return contexts.asMap().values().stream().mapToInt(Set::size).sum();
    }

    private static int weight(PermissionHolder holder) {
        return holder instanceof Group group ? group.weight().orElse(0) : Integer.MAX_VALUE;
    }

    private String expression(PermissionNode node) {
        return negations && node.permission().startsWith("-") ? node.permission().substring(1) : node.permission();
    }

    private PermissionResult candidateResult(PermissionNode node) {
        return negations && node.permission().startsWith("-") ? PermissionResult.DENY : node.value() == PermissionValue.ALLOW ? PermissionResult.ALLOW : PermissionResult.DENY;
    }

    private boolean matches(String expression, String permission) {
        return expression.equals(permission) || wildcards && (expression.equals("*") || expression.endsWith(".*") && permission.startsWith(expression.substring(0, expression.length() - 1)));
    }

    private int matchRank(String expression, String requested) {
        return expression.equals(requested) ? Integer.MAX_VALUE : expression.equals("*") ? 0 : expression.length();
    }

    private static Optional<Group> highest(Collection<Group> values) {
        return values.stream().max(Comparator.comparingInt((Group value) -> value.weight().orElse(0)).thenComparing(Group::name));
    }

    private Optional<Candidate> resolveCandidates(String permission, List<Candidate> candidates) {
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        var priority = candidateComparator(permission);

        var best = candidates.stream().max(priority).orElseThrow();

        var tied = candidates.stream().filter(candidate -> priority.compare(candidate, best) == 0).toList();

        var results = tied.stream().map(Candidate::result).collect(java.util.stream.Collectors.toSet());

        if (results.size() > 1 && conflictResolution == PermissionConflictResolution.STRICT) {
            tied.forEach(candidate -> candidate.status = CandidateStatus.CONFLICT);

            conflictWarning.accept("Strict permission conflict for '" + permission + "' between " + tied.stream().map(candidate -> sourceKey(candidate.source)).sorted().distinct().collect(java.util.stream.Collectors.joining(", ")) + "; returning undefined");

            return Optional.empty();
        }

        var preferred = results.size() == 1 ? results.iterator().next() : conflictResolution == PermissionConflictResolution.TRUE_WINS ? PermissionResult.ALLOW : PermissionResult.DENY;

        var winner = tied.stream().filter(candidate -> candidate.result() == preferred)
                .min(Comparator.comparing((Candidate candidate) -> sourceKey(candidate.source))
                        .thenComparing(candidate -> candidate.node.permission()))
                .orElseThrow();

        winner.status = CandidateStatus.WINNER;

        return Optional.of(winner);
    }

    private static String sourceKey(PermissionHolder holder) {
        if (holder instanceof Group group) {
            return "group:" + group.name();
        }

        if (holder instanceof User user) {
            return "user:" + user.uniqueId();
        }

        return holder.getClass().getName();
    }

    private Comparator<Candidate> candidateComparator(String permission) {
        return Comparator.comparingInt((Candidate value) -> matchRank(expression(value.node), permission))
                .thenComparingInt(Candidate::contextSpecificity)
                .thenComparing(Comparator.comparingInt(Candidate::inheritanceDistance).reversed())
                .thenComparingInt(value -> value.weight);
    }

    private static Comparator<OptionCandidate> optionComparator() {
        return Comparator.comparingInt(OptionCandidate::specificity)
                .thenComparing(Comparator.comparingInt(OptionCandidate::distance).reversed())
                .thenComparingInt(OptionCandidate::weight);
    }

    private record Source(PermissionHolder holder, int distance, int weight, CandidateStatus excluded, String detail) {}

    private record OptionCandidate(OptionNode node, int distance, int specificity, int weight, String source) {}

    private final class Candidate implements ResolutionCandidate {
        private final PermissionNode node;
        private final PermissionHolder source;
        private final int distance, specificity, weight;
        private CandidateStatus status;
        private final Optional<String> detail;

        Candidate(PermissionNode node, PermissionHolder source, int distance, int specificity, CandidateStatus status, Optional<String> detail, int weight) {
            this.node = node;
            this.source = source;
            this.distance = distance;
            this.specificity = specificity;
            this.status = status;
            this.detail = detail;
            this.weight = weight;
        }

        PermissionResult result() {
            return ResolverImpl.this.candidateResult(node);
        }

        @Override
        public PermissionNode node() {
            return node;
        }

        @Override
        public PermissionHolder source() {
            return source;
        }

        @Override
        public int inheritanceDistance() {
            return distance;
        }

        @Override
        public int contextSpecificity() {
            return specificity;
        }

        @Override
        public CandidateStatus status() {
            return status;
        }

        @Override
        public Optional<String> detail() {
            return detail;
        }
    }

    private record Resolution(PermissionResult result, String requestedPermission, Optional<ResolutionCandidate> winner, List<ResolutionCandidate> candidates) implements PermissionResolution {}

    private record Data(QueryOptions queryOptions, ResolvedPermissionData permissions, ResolvedMetaData meta) implements ResolvedData {}

    private record UserData(QueryOptions queryOptions, ResolvedPermissionData permissions, ResolvedMetaData meta, Set<Group> groups, Optional<Group> primaryGroup) implements ResolvedUserData {}

    private record PermissionData(PermissionHolder holder, QueryOptions options, Map<String, PermissionResult> permissionMap, ResolverImpl resolver) implements ResolvedPermissionData {
        @Override
        public PermissionResult check(String permission) {
            return resolver.check(holder, permission, options);
        }
    }

    private record MetaData(Map<String, String> options) implements ResolvedMetaData {
        @Override
        public Optional<String> option(String key) {
            return Optional.ofNullable(options.get(Identifiers.optionKey(key)));
        }

        @Override
        public Optional<String> prefix() {
            return option(OptionKeys.PREFIX);
        }

        @Override
        public Optional<String> suffix() {
            return option(OptionKeys.SUFFIX);
        }
    }
}
