/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.operations.UnionOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

/**
 * Created by buremba on 10/07/14.
 */
public class UnionRequest extends WriteRequest {
    HLLWrapper hll;

    public UnionRequest(String name, HLLWrapper hll) {
        super(name);
        this.hll = hll;
    }

    public UnionRequest() {

    }

    @Override
    protected Operation prepareOperation() {
        return new UnionOperation(name, hll);
    }

    @Override
    public int getClassId() {
        return HyperLogLogPortableFactory.UNION;
    }

    @Override
    public void write(PortableWriter writer) throws IOException {
        super.write(writer);
        ObjectDataOutput out = writer.getRawDataOutput();
        byte[] bytes = hll.bytes();
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        super.read(reader);
        ObjectDataInput in = reader.getRawDataInput();
        int size = in.readInt();
        byte[] rawHll = new byte[size];
        in.readFully(rawHll);
        hll = new HLLWrapper(rawHll);
    }
}
