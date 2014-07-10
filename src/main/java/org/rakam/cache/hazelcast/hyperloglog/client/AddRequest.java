/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */
package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.operations.AddOperation;

import java.io.IOException;

public class AddRequest extends WriteRequest {

    private String value;

    public AddRequest() {
    }

    protected AddRequest(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    protected Operation prepareOperation() {
        return new AddOperation(name, value);
    }

    @Override
    public int getClassId() {
        return HyperLogLogPortableFactory.ADD;
    }

    @Override
    public void write(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
        writer.writeUTF("d", value);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
        value = reader.readUTF("d");
    }
}