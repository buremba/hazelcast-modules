package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.AbstractOperation;
import org.rakam.cache.hazelcast.treemap.OrderedCounterMap;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by buremba <Burak Emre Kabakcı> on 13/07/14 16:25.
 */
public class TreeMapReplicationOperation extends AbstractOperation implements IdentifiedDataSerializable {

    private Map<String, OrderedCounterMap> migrationData;

    public TreeMapReplicationOperation(Map<String, OrderedCounterMap> data) {
        migrationData = data;
    }

    public TreeMapReplicationOperation() {

    }

    @Override
    public void run() throws Exception {
        TreeMapService service = getService();
        for (Map.Entry<String, OrderedCounterMap> longEntry : migrationData.entrySet()) {
            String name = longEntry.getKey();
            service.setHLL(name, longEntry.getValue());
        }
    }


    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        migrationData = new HashMap();
        int len = in.readInt();
        for(int i=0; i<len; i++) {
            OrderedCounterMap map = new OrderedCounterMap();
            int internalLength = in.readInt();
            for(int a=0; a<internalLength; a++) {
                map.increment(in.readUTF(), in.readLong());
            }
        }
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        out.writeInt(migrationData.size());
        for (Map.Entry<String, OrderedCounterMap> entry : migrationData.entrySet()) {
            out.writeUTF(entry.getKey());
            OrderedCounterMap val = entry.getValue();
            out.writeObject(val);
        }
    }


    @Override
    public int getFactoryId() {
        return TreeMapSerializerFactory.F_ID;
    }

    @Override
    public int getId() {
        return TreeMapSerializerFactory.REPLICATION;
    }
}
