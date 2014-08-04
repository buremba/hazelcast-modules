/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treemap.client;


import com.hazelcast.client.spi.ClientProxy;
import com.hazelcast.client.spi.ClientProxyFactory;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

public class ClientTreeMapProxyFactory implements ClientProxyFactory {

    @Override
    public ClientProxy create(String s) {
        return new ClientTreeMapProxy("ClientTreeMapProxy", TreeMapService.SERVICE_NAME, s);
    }
}
