---
layout: default
title: API Reference
permalink: /developers/reference/
description: Javadoc API reference for all PermissionsExPlus versions.
---

Browse the generated API documentation for each release.

<div class="version-cards">
{% for doc in site.javadoc_versions %}
  <a class="version-card" href="{{ doc.url | prepend: site.baseurl }}">
    <span class="version-card-label">{{ doc.label }}</span>
    <span class="version-card-desc">{{ doc.description }}</span>
  </a>
{% endfor %}
</div>

## Package overview

| API | Package | Javadoc |
|-----|---------|---------|
| **Modern** | `dev.rono.permissions.api.*` | [1.23.5]({{ site.baseurl }}/apidocs/1.23.5/dev/rono/permissions/api/package-summary.html) |
| **Legacy** | `ru.tehkode.permissions.*` | [1.23.5]({{ site.baseurl }}/apidocs/1.23.5/ru/tehkode/permissions/package-summary.html) |
| **Classic (archive)** | `ru.tehkode.permissions.*` | [1.22.1]({{ site.baseurl }}/apidocs/1.22.1/apidocs/ru/tehkode/permissions/package-summary.html) |

## Generate locally

From the repository root:

```bash
mvn -pl api/permissionsex-api,legacy-api/permissionsex-legacy-api javadoc:javadoc -am -Ddoclint=none
```

Output:

- `api/permissionsex-api/target/reports/apidocs/`
- `legacy-api/permissionsex-legacy-api/target/reports/apidocs/`

## Maven coordinates

```xml
<groupId>dev.rono.permissions</groupId>
<artifactId>permissionsex-api</artifactId>
<version>{{ site.version }}</version>
<scope>provided</scope>
```

```xml
<groupId>dev.rono.permissions</groupId>
<artifactId>permissionsex-legacy-api</artifactId>
<version>{{ site.version }}</version>
<scope>provided</scope>
```
