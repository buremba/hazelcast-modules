package org.rakam.cache.hazelcast.customMap.operations;

import com.hazelcast.map.operation.KeyBasedMapOperation;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.BackupAwareOperation;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.treemap.operations.IncrementByBackupOperation;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 02:33.
 */
public class IncrementByOperation extends KeyBasedMapOperation implements BackupAwareOperation {
    private long by;

    public IncrementByOperation(String name, Data key, long by) {
        super(name, key);
        this.by = by;
    }

    public IncrementByOperation() {

    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeLong(by);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        by = in.readLong();
    }

    @Override
    public void run() throws Exception {

        getService();

    }

    @Override
    public Object getResponse() {
        return null;
    }


    @Override
    public boolean shouldBackup() {
        return true;
    }

    @Override
    public int getSyncBackupCount() {
        return 0;
    }

    @Override
    public int getAsyncBackupCount() {
        return 0;
    }

    @Override
    public Operation getBackupOperation() {
        return new IncrementByBackupOperation();
    }
}
