/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */
package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.operations.AddAllOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class AddAllRequest extends WriteRequest {

    private Collection<String> items;

    public AddAllRequest() {
    }

    protected AddAllRequest(String name, Collection<String> items) {
        this.name = name;
        this.items = items;
    }

    @Override
    protected Operation prepareOperation() {
        return new AddAllOperation(name, items);
    }

    @Override
    public void write(PortableWriter writer) throws IOException {
        ObjectDataOutput w = writer.getRawDataOutput();
        w.writeUTF(name);
        w.writeInt(items.size());
        for (String item : items)
            w.writeUTF(item);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        ObjectDataInput r = reader.getRawDataInput();
        name = r.readUTF();
        int size = r.readInt();
        items = new ArrayList(size);
        for (int i = 0; i < size; i++)
            items.add(r.readUTF());
    }

    @Override
    public int getClassId() {
        return HyperLogLogPortableFactory.ADD_ALL;
    }
}