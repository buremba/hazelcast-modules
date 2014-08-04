package org.rakam.cache.hazelcast.treemap.client;

import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.treemap.operations.GetOperation;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 19/07/14 20:24.
 */
public class GetRequest extends ReadRequest {
    private Integer limit;

    public GetRequest(String name, Integer limit) {
        super(name);
        this.limit = limit==null ? -1 : limit;
    }

    public GetRequest() {

    }

    @Override
    protected Operation prepareOperation() {
        return new GetOperation(name, limit==-1 ? null : limit);
    }

    @Override
    public void write(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
        writer.writeInt("d", limit);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
        limit = reader.readInt("d");
    }

    @Override
    public int getClassId() {
        return TreeMapPortableFactory.RESET;
    }
}
