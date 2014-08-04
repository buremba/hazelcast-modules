/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionAwareOperation;
import org.rakam.cache.hazelcast.treemap.OrderedCounterMap;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

import java.io.IOException;

public abstract class TreeMapBaseOperation extends Operation implements PartitionAwareOperation, IdentifiedDataSerializable {

    protected String name;

    public TreeMapBaseOperation() {
    }

    public TreeMapBaseOperation(String name) {
        this.name = name;
    }

    public OrderedCounterMap getHLL() {
        TreeMapService service = getService();
        return service.getHLL(name);
    }

    @Override
    public int getFactoryId() {
        return TreeMapSerializerFactory.F_ID;
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
