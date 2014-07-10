package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBaseOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

/**
 * Created by buremba on 10/07/14.
 */
public class AddBackupOperation extends HyperLogLogBaseOperation implements BackupOperation {
    private String item;

    public AddBackupOperation() {
    }

    public AddBackupOperation(String name, String item) {
        super(name);
        this.item = item;
    }

    @Override
    public void run() throws IllegalArgumentException {
        HLLWrapper number = getHLL();
        number.add(item);
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.ADD_BACKUP;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeUTF(item);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        item = in.readUTF();
    }
}
