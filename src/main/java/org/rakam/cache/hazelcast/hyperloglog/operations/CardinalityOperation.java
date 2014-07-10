package org.rakam.cache.hazelcast.hyperloglog.operations;

/**
 * Created by buremba on 10/07/14.
 */

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBaseOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

/**
 * Created by buremba on 08/07/14.
 */

public class CardinalityOperation extends HyperLogLogBaseOperation {

    private long returnValue;

    public CardinalityOperation() {
    }

    public CardinalityOperation(String name) {
        super(name);
    }

    @Override
    public void run() throws Exception {
        HLLWrapper number = getHLL();
        this.returnValue = number.cardinality();
    }

    @Override
    public Object getResponse() {
        return returnValue;
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.CARDINALITY;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
    }
}

