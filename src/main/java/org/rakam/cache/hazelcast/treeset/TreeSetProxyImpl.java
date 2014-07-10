/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.collection.set.SetProxyImpl;
import com.hazelcast.config.CollectionConfig;
import com.hazelcast.spi.NodeEngine;

public class TreeSetProxyImpl<E> extends SetProxyImpl implements ITreeSet {

    public TreeSetProxyImpl(String name, NodeEngine nodeEngine, TreeSetService service) {
        super(name, nodeEngine, service);
    }

    @Override
    protected CollectionConfig getConfig(NodeEngine nodeEngine) {
        return nodeEngine.getConfig().findSetConfig(name);
    }

    @Override
    public String getServiceName() {
        return TreeSetService.SERVICE_NAME;
    }

}