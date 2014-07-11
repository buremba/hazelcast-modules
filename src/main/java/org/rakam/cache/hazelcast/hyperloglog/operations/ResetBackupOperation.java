package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.spi.BackupOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBaseOperation;
import org.rakam.util.HLLWrapper;

/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14 16:16.
 */
public class ResetBackupOperation extends HyperLogLogBaseOperation implements BackupOperation {
    public ResetBackupOperation() {
    }

    public ResetBackupOperation(String name) {
        super(name);
    }

    @Override
    public void run() throws IllegalArgumentException {
        HLLWrapper hll = getHLL();
        hll.reset();
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.RESET_BACKUP;
    }
}
