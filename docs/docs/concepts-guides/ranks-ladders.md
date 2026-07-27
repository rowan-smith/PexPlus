---
sidebar_position: 11
---

# Ranks & Ladders

PermissionsExPlus includes a built-in promotion and demotion system. Groups can be assigned ranks on named ladders, and players can be moved up or down with `/promote` and `/demote`.

## How It Works

A **ladder** is a named progression path (e.g. `default`, `donor`, `build`). Each group can have a **rank** on one or more ladders. Higher rank numbers are considered "higher" on the ladder.

When you promote a player, PEX finds the next group on that ladder with a higher rank and moves the player into it. Demoting does the opposite, it finds the next group with a lower rank.

```text
newcomer (100) → member (200) → trusted (300) → elite (400)
```

## Setting Up a Ladder

### 1. Create the groups

```text
/pex group newcomer create
/pex group member create
/pex group trusted create
/pex group elite create
```

### 2. Assign ranks

Use `/pex group <group> rank <number> <ladder>` to assign each group a rank on a ladder. The ladder name can be anything, `default` is common for the main progression.

```text
/pex group newcomer rank 100 default
/pex group member rank 200 default
/pex group trusted rank 300 default
/pex group elite rank 400 default
```

### 3. Set weights

Weights control which group's prefix is shown and how permissions resolve. Higher weight = higher priority.

```text
/pex group newcomer weight 0
/pex group member weight 10
/pex group trusted weight 20
/pex group elite weight 30
```

### 4. Build inheritance

Inheritance lets lower groups inherit permissions from higher groups, so you don't have to duplicate permission lists.

```text
/pex group member parents set newcomer
/pex group trusted parents set member
/pex group elite parents set trusted
```

### 5. Assign players

```text
/pex user Steve group add newcomer
```

## Using the Ladder

### Promoting

```text
/pex promote Steve default
```

This moves Steve to the next group up on the `default` ladder. If Steve is in `newcomer` (rank 100), he'll be promoted to `member` (rank 200).

### Demoting

```text
/pex demote Steve default
```

This moves Steve to the next group down. If Steve is in `trusted` (rank 300), he'll be demoted to `member` (rank 200).

### Standalone aliases

For convenience, you can use `/promote` and `/demote` without the `/pex` prefix:

```text
/promote Steve default
/demote Steve default
```

## Checking Ranks

To see a group's rank on a ladder:

```text
/pex group member rank default
```

To see all ranks for a group:

```text
/pex group member rank
```

## Weight vs Rank

Weight and rank are independent settings that serve different purposes:

| Setting | Controls | Example |
|---------|----------|---------|
| **Rank** | Position on a promotion ladder | `member` has rank 200 on `default` |
| **Weight** | Priority between groups (prefix, permission resolution) | `admin` has weight 100, `default` has weight 0 |

A group can have a high weight but low rank, or vice versa:

```text
/pex group admin weight 100    # High priority for prefix/permissions
/pex group admin rank 0 default  # Low rank — can't be promoted

/pex group vip weight 10       # Lower priority for prefix/permissions
/pex group vip rank 500 default  # High rank — at the top of the ladder
```

:::note
A group with no rank on a ladder won't be included in `/promote` or `/demote`. This is how you keep staff groups off the progression ladder while still giving them high weight.
:::

## Multiple Ladders

You can create multiple ladders for different progression paths. Each ladder is independent, a player can be on several ladders at once.

### Example: default + donor ladders

```text
/pex group newcomer rank 100 default
/pex group member rank 200 default
/pex group trusted rank 300 default

/pex group donor1 rank 100 donor
/pex group donor2 rank 200 donor
/pex group donor3 rank 300 donor
```

```text
/pex promote Steve default    # moves up the default ladder
/pex promote Steve donor      # moves up the donor ladder
```

### Use cases for multiple ladders

| Ladder | Purpose |
|--------|---------|
| `default` | Main player progression (newcomer → member → trusted → elite) |
| `donor` | Donation tiers (bronze → silver → gold → diamond) |
| `build` | Builder ranks (apprentice → journeyman → master) |
| `event` | Temporary event progression |

## Ladder Behavior

### What happens at the top?

If a player is already at the highest rank on a ladder, `/promote` does nothing. The player stays in their current group.

### What happens at the bottom?

If a player is at the lowest rank, `/demote` does nothing.

### Multiple groups on the same ladder?

A player should only be in one group per ladder. If a player is somehow in multiple groups on the same ladder, `/promote` will promote from the group with the highest current rank.

### Group removal

When a player is promoted or demoted, they are **removed** from their current group on that ladder and **added** to the new group. Their permissions from other groups and inheritance are preserved.

## Complete Example

```yaml
groups:
  newcomer:
    options:
      default: true
      weight: 0
      prefix: "[&7Newcomer&r] "
      rank: 100
      rank-ladder: default
    permissions:
      - essentials.spawn
      - essentials.tpaccept

  member:
    inheritance:
      - newcomer
    permissions:
      - essentials.home
      - essentials.sethome
      - essentials.tpa
    options:
      weight: 10
      prefix: "[&aMember&r] "
      rank: 200
      rank-ladder: default

  trusted:
    inheritance:
      - member
    permissions:
      - essentials.fly
      - essentials.back
      - essentials.kit
    options:
      weight: 20
      prefix: "[&bTrusted&r] "
      rank: 300
      rank-ladder: default

  elite:
    inheritance:
      - trusted
    permissions:
      - essentials.heal
      - essentials.tp
      - essentials.tphere
    options:
      weight: 30
      prefix: "[&6Elite&r] "
      rank: 400
      rank-ladder: default

  staff:
    permissions:
      - essentials.ban
      - essentials.kick
      - essentials.mute
      - essentials.teleport
    options:
      weight: 100
      prefix: "[&9Staff&r] "
```

Staff has rank 0 on the default ladder, they can't be promoted or demoted, but they have high weight (100) so their prefix takes priority.

For a ready-made setup with commands, see [Rank Ladder](../common-setups/rank-ladder).
