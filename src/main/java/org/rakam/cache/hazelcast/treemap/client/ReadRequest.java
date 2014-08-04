/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treemap.client;

import com.hazelcast.client.ClientEngine;
import com.hazelcast.client.PartitionClientRequest;
import com.hazelcast.client.SecureRequest;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.security.permission.ActionConstants;
import com.hazelcast.security.permission.AtomicLongPermission;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

import java.io.IOException;
import java.security.Permission;

public abstract class ReadRequest extends PartitionClientRequest implements Portable, SecureRequest {

    protected String name;

    public ReadRequest() {
    }

    public ReadRequest(String name) {
        this.name = name;
    }

    @Override
    protected int getPartition() {
        ClientEngine clientEngine = getClientEngine();
        //Data key = serializationService.toData(name);
        Data key = clientEngine.getSerializationService().toData(name);
        return clientEngine.getPartitionService().getPartitionId(key);
    }

    @Override
    public String getServiceName() {
        return TreeMapService.SERVICE_NAME;
    }

    @Override
    public int getFactoryId() {
        return TreeMapPortableFactory.F_ID;
    }

    @Override
    public void write(PortableWriter writer) throws IOException {
        writer.writeUTF("n", name);
    }

    @Override
    public void read(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
    }

    @Override
    public Permission getRequiredPermission() {
        return new AtomicLongPermission(name, ActionConstants.ACTION_READ);
    }
}