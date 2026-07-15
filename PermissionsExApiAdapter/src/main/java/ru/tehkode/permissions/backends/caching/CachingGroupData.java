package ru.tehkode.permissions.backends.caching;

import java.util.concurrent.Executor;
import ru.tehkode.permissions.PermissionsGroupData;

/**
 * Cached data for groups
 */
public class CachingGroupData extends CachingData implements PermissionsGroupData {
    private final PermissionsGroupData backingData;
    public CachingGroupData(PermissionsGroupData backingData, Executor executor, Object lock) {
        super(executor, lock);
        this.backingData = backingData;
    }

    @Override
    protected PermissionsGroupData getBackingData() {
        return backingData;
    }
}
