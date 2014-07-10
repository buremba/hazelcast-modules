package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBaseOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

/**
 * Created by buremba on 10/07/14.
 */
public class UnionBackupOperation extends HyperLogLogBaseOperation implements BackupOperation {
    private HLLWrapper hll;

    public UnionBackupOperation() {
    }

    public UnionBackupOperation(String name, HLLWrapper hll) {
        super(name);
        this.hll = hll;
    }

    @Override
    public void run() throws Exception {
        HLLWrapper number = getHLL();
        number.union(hll);
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
    public int getId() {
        return HyperLogLogSerializerFactory.UNION_BACKUP;
    }

}
