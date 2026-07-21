# Hook API

The Hook API allows external plugins to register custom context calculators and other extensions.

## Registering a Context Calculator

```java
api.contexts().registerCalculator(new MyCustomCalculator());
```

## Available Hooks

- `Vault`: Integration for economy and chat.
- `PlaceholderAPI`: Support for custom placeholders.
