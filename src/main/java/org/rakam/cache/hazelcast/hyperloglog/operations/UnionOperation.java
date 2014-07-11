package org.rakam.cache.hazelcast.hyperloglog.operations;

/**
 * Created by buremba on 10/07/14.
 */

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBackupAwareOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

public class UnionOperation extends HyperLogLogBackupAwareOperation {

    private HLLWrapper hll;

    public UnionOperation() {
    }

    public UnionOperation(String name, HLLWrapper hll) {
        super(name);
        this.hll = hll;
    }

    @Override
    public void run() throws Exception {
        HLLWrapper number = getHLL();
        number.union(hll);
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.UNION;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        byte[] bytes = hll.bytes();
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        int size = in.readInt();
        byte[] rawHll = new byte[size];
        in.readFully(rawHll);
        hll = new HLLWrapper(rawHll);
    }

    @Override
    public Operation getBackupOperation() {
        return new UnionBackupOperation(name, hll);
    }
}

