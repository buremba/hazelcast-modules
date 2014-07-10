/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionAwareOperation;
import org.rakam.cache.hazelcast.hyperloglog.operations.HyperLogLogSerializerFactory;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

public abstract class HyperLogLogBaseOperation extends Operation implements PartitionAwareOperation, IdentifiedDataSerializable {

    protected String name;

    public HyperLogLogBaseOperation() {
    }

    public HyperLogLogBaseOperation(String name) {
        this.name = name;
    }

    public HLLWrapper getHLL() {
        HyperLogLogService service = getService();
        return service.getHLL(name);
    }

    @Override
    public int getFactoryId() {
        return HyperLogLogSerializerFactory.F_ID;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        out.writeUTF(name);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        name = in.readUTF();
    }

    @Override
    public void afterRun() throws Exception {
    }

    @Override
    public void beforeRun() throws Exception {
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public boolean returnsResponse() {
        return true;
    }
}
