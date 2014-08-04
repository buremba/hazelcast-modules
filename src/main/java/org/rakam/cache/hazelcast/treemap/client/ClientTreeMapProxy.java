/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treemap.client;

import com.hazelcast.client.ClientRequest;
import com.hazelcast.client.spi.ClientProxy;
import com.hazelcast.nio.serialization.Data;
import org.rakam.cache.hazelcast.treemap.ITreeMap;

import java.util.Map;

public class ClientTreeMapProxy extends ClientProxy implements ITreeMap {

    private String name;
    private volatile Data key;

    public ClientTreeMapProxy(String instanceName, String serviceName, String objectId) {
        super(instanceName, serviceName, objectId);
        this.name = objectId;
    }

    @Override
    protected void onDestroy() {

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
        return "TreeMap{" + "name='" + name + '\'' + '}';
    }

    @Override
    public void increment(String key, long by) {
        IncrementByRequest operation = new IncrementByRequest(name, key, by);
        invoke(operation);
    }

    @Override
    public Map<String, Long> getAll() {
        GetRequest operation = new GetRequest(name, null);
        return invoke(operation);
    }

    @Override
    public Map<String, Long> getTopItems(int numberOfElements) {
        GetRequest operation = new GetRequest(name, numberOfElements);
        return invoke(operation);
    }
}

