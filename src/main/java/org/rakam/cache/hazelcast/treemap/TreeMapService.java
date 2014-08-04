package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.partition.InternalPartitionService;
import com.hazelcast.partition.MigrationEndpoint;
import com.hazelcast.spi.*;
import com.hazelcast.util.ConstructorFunction;
import org.rakam.cache.hazelcast.treemap.operations.TreeMapReplicationOperation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.hazelcast.partition.strategy.StringPartitioningStrategy.getPartitionKey;
import static com.hazelcast.util.ConcurrencyUtil.getOrPutIfAbsent;

/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14 17:47.
 */
public class TreeMapService implements ManagedService, RemoteService, MigrationAwareService {

    public static final String SERVICE_NAME = "rakam:treeMapService";

    private NodeEngine nodeEngine;
    private final ConcurrentMap<String, OrderedCounterMap> containers = new ConcurrentHashMap<String, OrderedCounterMap>();
    private final ConstructorFunction<String, OrderedCounterMap> CountersConstructorFunction =
            new ConstructorFunction<String, OrderedCounterMap>() {
                public OrderedCounterMap createNew(String key) {
                    return new OrderedCounterMap();
                }
            };

    public TreeMapService() {
    }

    public OrderedCounterMap getHLL(String name) {
        return getOrPutIfAbsent(containers, name, CountersConstructorFunction);
    }

    public void setHLL(String name, OrderedCounterMap map) {
        containers.put(name, map);
    }

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        this.nodeEngine = nodeEngine;
    }

    @Override
    public void reset() {
        containers.clear();
    }

    @Override
    public void shutdown(boolean terminate) {
        reset();
    }

    @Override
    public TreeMapProxy createDistributedObject(String name) {
        return new TreeMapProxy(name, nodeEngine, this);
    }

    @Override
    public void destroyDistributedObject(String name) {
        containers.remove(name);
    }

    @Override
    public void beforeMigration(PartitionMigrationEvent partitionMigrationEvent) {
    }

    @Override
    public Operation prepareReplicationOperation(PartitionReplicationEvent event) {
        Map<String, OrderedCounterMap> data = new HashMap();
        int partitionId = event.getPartitionId();
        for (String name : containers.keySet()) {
            if (partitionId == getPartitionId(name)) {
                OrderedCounterMap number = containers.get(name);
                data.put(name, number);
            }
        }
        return data.isEmpty() ? null : new TreeMapReplicationOperation(data);
    }

    private int getPartitionId(String name) {
        InternalPartitionService partitionService = nodeEngine.getPartitionService();
        String partitionKey = getPartitionKey(name);
        return partitionService.getPartitionId(partitionKey);
    }

    @Override
    public void commitMigration(PartitionMigrationEvent partitionMigrationEvent) {
        if (partitionMigrationEvent.getMigrationEndpoint() == MigrationEndpoint.SOURCE) {
            removePartition(partitionMigrationEvent.getPartitionId());
        }
    }

    @Override
    public void rollbackMigration(PartitionMigrationEvent partitionMigrationEvent) {
        if (partitionMigrationEvent.getMigrationEndpoint() == MigrationEndpoint.DESTINATION) {
            removePartition(partitionMigrationEvent.getPartitionId());
        }
    }

    @Override
    public void clearPartitionReplica(int partitionId) {
        removePartition(partitionId);
    }

    public void removePartition(int partitionId) {
        final Iterator<String> iterator = containers.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (getPartitionId(name) == partitionId) {
                iterator.remove();
            }
        }
    }
}
