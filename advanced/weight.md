---
layout: default
title: Weight
permalink: /advanced/weight/
description: Group weight and sort order in PermissionsExPlus.
---

**Weight** controls the order groups are processed. Higher weight = higher priority when multiple groups apply.

## Set weight

```text
/pex group admin weight 100
/pex group moderator weight 50
/pex group default weight 0
```

## View weight

```text
/pex group admin weight
```

## Why weight matters

Weight affects:

- **Prefix/suffix resolution** — which group's chat prefix wins when a player is in multiple groups
- **Permission ordering** — when permissions conflict, weight can influence the result

## Typical setup

```text
/pex group default weight 0
/pex group vip weight 10
/pex group moderator weight 50
/pex group admin weight 100
```

Staff groups should have higher weight than regular player groups so their prefixes display correctly.
