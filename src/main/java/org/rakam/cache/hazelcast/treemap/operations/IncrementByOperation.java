package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.treemap.OrderedCounterMap;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 02:33.
 */
public class IncrementByOperation extends TreeMapBackupAwareOperation {
    private String key;
    private long by;

    public IncrementByOperation(String name, String key, long by) {
        super(name);
        this.by = by;
        this.key = key;
    }

    public IncrementByOperation() {

    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeLong(by);
        out.writeUTF(key);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        by = in.readLong();
        key = in.readUTF();
    }

    @Override
    public void run() throws Exception {

        OrderedCounterMap map = ((TreeMapService) getService()).getHLL(name);
        map.increment(key, by);
    }

    @Override
    public Operation getBackupOperation() {
        return new IncrementByBackupOperation();
    }

    @Override
    public int getId() {
        return TreeMapSerializerFactory.ADD;
    }
}
