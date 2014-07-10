/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.client.ClientEngine;
import com.hazelcast.client.PartitionClientRequest;
import com.hazelcast.client.SecureRequest;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.security.permission.ActionConstants;
import com.hazelcast.security.permission.AtomicLongPermission;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogService;

import java.security.Permission;

public abstract class WriteRequest extends PartitionClientRequest implements Portable, SecureRequest {

    protected String name;

    protected WriteRequest() {
    }

    protected WriteRequest(String name) {
        this.name = name;
    }

    @Override
    protected int getPartition() {
        ClientEngine clientEngine = getClientEngine();
        Data key = serializationService.toData(name);
        return clientEngine.getPartitionService().getPartitionId(key);
    }

    @Override
    public String getServiceName() {
        return HyperLogLogService.SERVICE_NAME;
    }

    @Override
    public int getFactoryId() {
        return HyperLogLogPortableFactory.F_ID;
    }


    @Override
    public Permission getRequiredPermission() {
        return new AtomicLongPermission(name, ActionConstants.ACTION_MODIFY);
    }
}
