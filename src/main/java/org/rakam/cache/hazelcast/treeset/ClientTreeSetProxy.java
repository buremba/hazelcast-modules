/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.client.proxy.AbstractClientCollectionProxy;

public class ClientTreeSetProxy<E> extends AbstractClientCollectionProxy<E> implements ITreeSet<E> {

    public ClientTreeSetProxy(String instanceName, String serviceName, String name) {
        super(instanceName, serviceName, name);
    }

    @Override
    public String toString() {
        return "ITreeSet{" + "name='" + getName() + '\'' + '}';
    }

}