# PermissionsEx Command Adapter

## Legacy command compatibility shim

Provides the legacy `/pex`, `/promote`, and `/demote` command formats while delegating
every lookup and mutation to `PermissionsExPlusApi`. It intentionally publishes no
`ru.tehkode.permissions` API contracts; consumers needing the legacy binary API must
use `PermissionsExApiAdapter`.

## Hard dependency

- PermissionsExPlus
