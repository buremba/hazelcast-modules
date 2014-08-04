package org.rakam.cache.hazelcast.treemap.client;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.treemap.operations.IncrementByOperation;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14 16:18.
 */
public class IncrementByRequest extends WriteRequest {

    private String key;
    private long inc;

    public IncrementByRequest(String name, String key, long inc) {
        super(name);
        this.key = key;
        this.inc = inc;
    }

    public IncrementByRequest() {

    }
    @Override
    public void write(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
        writer.writeUTF("d", key);
        writer.writeLong("l", inc);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
        key = reader.readUTF("d");
        inc = reader.readLong("l");
    }
    @Override
    protected Operation prepareOperation() {
        return new IncrementByOperation(name, key, inc);
    }

    @Override
    public int getClassId() {
        return TreeMapPortableFactory.ADD;
    }
}
