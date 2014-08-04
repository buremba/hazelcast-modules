package org.rakam.cache.hazelcast.customMap.operations;

import com.hazelcast.map.operation.KeyBasedMapOperation;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.BackupOperation;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 03:23.
 */
public class IncrementByBackupOperation extends KeyBasedMapOperation implements BackupOperation {
    private long by;

    public IncrementByBackupOperation(String name, Data key, long by) {
        super(name, key);
        this.by = by;
    }

    public IncrementByBackupOperation() {

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
}
