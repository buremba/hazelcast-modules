package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBackupAwareOperation;
import org.rakam.util.HLLWrapper;

/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14 16:13.
 */
public class ResetOperation extends HyperLogLogBackupAwareOperation {


    public ResetOperation() {
    }

    public ResetOperation(String name) {
        super(name);
    }

    @Override
    public void run() throws IllegalArgumentException {
        HLLWrapper hll = getHLL();
        hll.reset();
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.RESET;
    }

    @Override
    public Operation getBackupOperation() {
        return new ResetBackupOperation(name);
    }
}