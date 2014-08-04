package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.spi.BackupAwareOperation;
import com.hazelcast.spi.BackupOperation;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 02:37.
 */
public abstract class TreeMapBackupAwareOperation extends TreeMapBaseOperation implements BackupAwareOperation, BackupOperation {

    protected boolean shouldBackup = true;

    public TreeMapBackupAwareOperation() {
    }

    public TreeMapBackupAwareOperation(String name) {
        super(name);
    }

    @Override
    public boolean shouldBackup() {
        return shouldBackup;
    }

    @Override
    public int getSyncBackupCount() {
        return 1;
    }

    @Override
    public int getAsyncBackupCount() {
        return 0;
    }
}