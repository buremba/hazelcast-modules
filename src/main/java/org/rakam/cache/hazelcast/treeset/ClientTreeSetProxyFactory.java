/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.client.spi.ClientProxy;
import com.hazelcast.client.spi.ClientProxyFactory;

public class ClientTreeSetProxyFactory implements ClientProxyFactory {

    @Override
    public ClientProxy create(String s) {
        return new ClientTreeSetProxy("ClientTreeSetProxy", TreeSetService.SERVICE_NAME, s);
    }
}