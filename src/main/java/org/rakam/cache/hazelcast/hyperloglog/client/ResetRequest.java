package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.operations.ResetOperation;

/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14 16:18.
 */
public class ResetRequest extends WriteRequest {

    public ResetRequest(String name) {
        super(name);
    }

    public ResetRequest() {

    }

    @Override
    protected Operation prepareOperation() {
        return new ResetOperation(name);
    }

    @Override
    public int getClassId() {
        return HyperLogLogPortableFactory.RESET;
    }
}
