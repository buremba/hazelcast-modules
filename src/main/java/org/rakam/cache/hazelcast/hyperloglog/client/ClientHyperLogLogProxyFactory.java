/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;


import com.hazelcast.client.spi.ClientProxy;
import com.hazelcast.client.spi.ClientProxyFactory;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogService;

public class ClientHyperLogLogProxyFactory implements ClientProxyFactory {

    @Override
    public ClientProxy create(String s) {
        return new ClientHyperLogLogProxy("ClientHyperLogLogProxy", HyperLogLogService.SERVICE_NAME, s);
    }
}
