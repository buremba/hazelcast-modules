/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.collection.CollectionContainer;
import com.hazelcast.collection.set.SetContainer;
import com.hazelcast.collection.set.SetReplicationOperation;
import com.hazelcast.collection.set.SetService;
import com.hazelcast.collection.txn.TransactionalSetProxy;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.Operation;
import com.hazelcast.spi.PartitionReplicationEvent;
import com.hazelcast.transaction.impl.TransactionSupport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TreeSetService extends SetService {

    public static final String SERVICE_NAME = "rakam:treeSetService";

    private final ConcurrentMap<String, TreeSetContainer> containerMap = new ConcurrentHashMap<String, TreeSetContainer>();

    public TreeSetService(NodeEngine nodeEngine) {
        super(nodeEngine);
    }

    @Override
    public SetContainer getOrCreateContainer(String name, boolean backup) {
        TreeSetContainer container = containerMap.get(name);
        if (container == null) {
            container = new TreeSetContainer(name, nodeEngine);
            final TreeSetContainer current = containerMap.putIfAbsent(name, container);
            if (current != null) {
                container = current;
            }
        }
        return container;
    }

    @Override
    public Map<String, ? extends CollectionContainer> getContainerMap() {
        return containerMap;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public DistributedObject createDistributedObject(String objectId) {
        return new TreeSetProxyImpl(objectId, nodeEngine, this);
    }

    @Override
    public TransactionalSetProxy createTransactionalObject(String name, TransactionSupport transaction) {

        return new TransactionalSetProxy(name, transaction, nodeEngine, this);
    }

    @Override
    public Operation prepareReplicationOperation(PartitionReplicationEvent event) {
        final Map<String, CollectionContainer> migrationData = getMigrationData(event);
        return migrationData.isEmpty()
                ? null
                : new SetReplicationOperation(migrationData, event.getPartitionId(), event.getReplicaIndex());
    }
}
