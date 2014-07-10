/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.spi.BackupAwareOperation;
import com.hazelcast.spi.BackupOperation;

public abstract class HyperLogLogBackupAwareOperation extends HyperLogLogBaseOperation implements BackupAwareOperation, BackupOperation {

    protected boolean shouldBackup = true;

    public HyperLogLogBackupAwareOperation() {
    }

    public HyperLogLogBackupAwareOperation(String name) {
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
