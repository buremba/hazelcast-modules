/**
 * Created by buremba on 08/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBackupAwareOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

public class AddOperation extends HyperLogLogBackupAwareOperation {

    private String item;

    public AddOperation() {
    }

    public AddOperation(String name, String item) {
        super(name);
        this.item = item;
    }

    @Override
    public void run() throws IllegalArgumentException {
        HLLWrapper hll = getHLL();
        hll.add(item);
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.ADD;
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

    @Override
    public Operation getBackupOperation() {
        return new AddBackupOperation(name, item);
    }
}
