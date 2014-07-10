package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBaseOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by buremba on 10/07/14.
 */
public class AddAllBackupOperation extends HyperLogLogBaseOperation implements BackupOperation {
    private Collection<String> items;

    public AddAllBackupOperation() {
    }

    public AddAllBackupOperation(String name, Collection<String> item) {
        super(name);
        this.items = item;
    }

    @Override
    public void run() throws Exception {
        HLLWrapper hll = getHLL();
        hll.addAll(items);
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.ADD_ALL_BACKUP;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeInt(items.size());
        for (String item : items)
            out.writeUTF(item);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        int size = in.readInt();
        items = new ArrayList(size);
        for (int i = 0; i < size; i++)
            items.add(in.readUTF());
    }
}
