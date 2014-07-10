/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.partition.InternalPartitionService;
import com.hazelcast.partition.MigrationEndpoint;
import com.hazelcast.spi.*;
import com.hazelcast.util.ConstructorFunction;
import org.rakam.cache.hazelcast.hyperloglog.operations.CounterReplicationOperation;
import org.rakam.util.HLLWrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.hazelcast.partition.strategy.StringPartitioningStrategy.getPartitionKey;
import static com.hazelcast.util.ConcurrencyUtil.getOrPutIfAbsent;

public class HyperLogLogService implements ManagedService, RemoteService, MigrationAwareService {

    public static final String SERVICE_NAME = "rakam:hyperLogLogService";

    private NodeEngine nodeEngine;
    private final ConcurrentMap<String, HLLWrapper> numbers = new ConcurrentHashMap<String, HLLWrapper>();
    private final ConstructorFunction<String, HLLWrapper> CountersConstructorFunction =
            new ConstructorFunction<String, HLLWrapper>() {
                public HLLWrapper createNew(String key) {
                    return new HLLWrapper();
                }
            };

    public HyperLogLogService() {
    }

    public HLLWrapper getHLL(String name) {
        return getOrPutIfAbsent(numbers, name, CountersConstructorFunction);
    }

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        this.nodeEngine = nodeEngine;
    }

    @Override
    public void reset() {
        numbers.clear();
    }

    @Override
    public void shutdown(boolean terminate) {
        reset();
    }

    @Override
    public HyperLogLogProxy createDistributedObject(String name) {
        return new HyperLogLogProxy(name, nodeEngine, this);
    }

    @Override
    public void destroyDistributedObject(String name) {
        numbers.remove(name);
    }

    @Override
    public void beforeMigration(PartitionMigrationEvent partitionMigrationEvent) {
    }

    @Override
    public Operation prepareReplicationOperation(PartitionReplicationEvent event) {
        if (event.getReplicaIndex() > 1) {
            return null;
        }

        Map<String, byte[]> data = new HashMap<String, byte[]>();
        int partitionId = event.getPartitionId();
        for (String name : numbers.keySet()) {
            if (partitionId == getPartitionId(name)) {
                HLLWrapper number = numbers.get(name);
                data.put(name, number.bytes());
            }
        }
        return data.isEmpty() ? null : new CounterReplicationOperation(data);
    }

    private int getPartitionId(String name) {
        InternalPartitionService partitionService = nodeEngine.getPartitionService();
        String partitionKey = getPartitionKey(name);
        return partitionService.getPartitionId(partitionKey);
    }

    @Override
    public void commitMigration(PartitionMigrationEvent partitionMigrationEvent) {
        if (partitionMigrationEvent.getMigrationEndpoint() == MigrationEndpoint.SOURCE) {
            removeNumber(partitionMigrationEvent.getPartitionId());
        }
    }

    @Override
    public void rollbackMigration(PartitionMigrationEvent partitionMigrationEvent) {
        if (partitionMigrationEvent.getMigrationEndpoint() == MigrationEndpoint.DESTINATION) {
            removeNumber(partitionMigrationEvent.getPartitionId());
        }
    }

    @Override
    public void clearPartitionReplica(int partitionId) {
        removeNumber(partitionId);
    }

    public void removeNumber(int partitionId) {
        final Iterator<String> iterator = numbers.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (getPartitionId(name) == partitionId) {
                iterator.remove();
            }
        }
    }
}