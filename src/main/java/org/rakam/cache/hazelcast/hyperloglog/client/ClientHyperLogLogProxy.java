/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.client.ClientRequest;
import com.hazelcast.client.spi.ClientProxy;
import com.hazelcast.nio.serialization.Data;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLog;
import org.rakam.util.HLLWrapper;

import java.util.Collection;

public class ClientHyperLogLogProxy extends ClientProxy implements HyperLogLog {

    private String name;
    private volatile Data key;

    public ClientHyperLogLogProxy(String instanceName, String serviceName, String objectId) {
        super(instanceName, serviceName, objectId);
        this.name = objectId;
    }


    protected <T> T invoke(ClientRequest req) {
        return super.invoke(req, getKey());
    }

    private Data getKey() {
        if (key == null) {
            key = toData(name);
        }
        return key;
    }

    @Override
    public String toString() {
        return "HyperLogLog{" + "name='" + name + '\'' + '}';
    }

    @Override
    public long cardinality() {
        CardinalityRequest request = new CardinalityRequest(name);
        return (Long) invoke(request);
    }

    @Override
    public void reset() {
        ResetRequest request = new ResetRequest(name);
        invoke(request);
    }

    @Override
    public void union(HLLWrapper hll) {
        UnionRequest request = new UnionRequest(name, hll);
        invoke(request);
    }

    @Override
    public void addAll(Collection<String> coll) {
        AddAllRequest request = new AddAllRequest(name, coll);
        invoke(request);
    }

    @Override
    public void add(String obj) {
        AddRequest request = new AddRequest(name, obj);
        invoke(request);
    }
}

