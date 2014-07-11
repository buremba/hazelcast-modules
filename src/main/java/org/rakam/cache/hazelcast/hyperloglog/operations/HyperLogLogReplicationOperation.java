/**
 * Created by buremba on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.AbstractOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogService;
import org.rakam.util.HLLWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HyperLogLogReplicationOperation extends AbstractOperation implements IdentifiedDataSerializable {

    private Map<String, byte[]> migrationData;

    public HyperLogLogReplicationOperation() {
    }

    public HyperLogLogReplicationOperation(Map<String, byte[]> migrationData) {
        this.migrationData = migrationData;
    }

    @Override
    public void run() throws Exception {
        HyperLogLogService atomicLongService = getService();
        for (Map.Entry<String, byte[]> longEntry : migrationData.entrySet()) {
            String name = longEntry.getKey();
            HLLWrapper number = atomicLongService.getHLL(name);
            byte[] value = longEntry.getValue();
            number.set(value);
        }
    }

    @Override
    public String getServiceName() {
        return HyperLogLogService.SERVICE_NAME;
    }

    @Override
    public int getFactoryId() {
        return HyperLogLogSerializerFactory.F_ID;
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.REPLICATION;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        out.writeInt(migrationData.size());
        for (Map.Entry<String, byte[]> entry : migrationData.entrySet()) {
            out.writeUTF(entry.getKey());
            byte[] val = entry.getValue();
            out.writeInt(val.length);
            out.write(val);
        }
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        int mapSize = in.readInt();
        migrationData = new HashMap<String, byte[]>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            String name = in.readUTF();
            int number = in.readInt();
            byte[] a = new byte[number];
            in.readFully(a);
            migrationData.put(name, a);
        }
    }
}
