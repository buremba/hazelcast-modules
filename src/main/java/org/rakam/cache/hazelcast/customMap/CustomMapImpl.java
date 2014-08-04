package org.rakam.cache.hazelcast.customMap;

import com.hazelcast.map.MapService;
import com.hazelcast.map.operation.BasePutOperation;
import com.hazelcast.map.operation.BaseRemoveOperation;
import com.hazelcast.map.operation.GetOperation;
import com.hazelcast.map.operation.KeyBasedMapOperation;
import com.hazelcast.map.proxy.MapProxyImpl;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.OperationService;
import com.hazelcast.spi.RemoteService;
import com.hazelcast.util.ExceptionUtil;
import com.hazelcast.util.ThreadUtil;

import java.util.concurrent.Future;

import static com.hazelcast.map.MapService.SERVICE_NAME;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 23:08.
 */
public class CustomMapImpl extends MapProxyImpl implements OrderingCounterMap<String, ComparableAtomicLong> {
    public CustomMapImpl(String name, MapService mapService, NodeEngine nodeEngine) {
        super(name, mapService, nodeEngine);
        RemoteService service = getService();
    }

    private void invalidateNearCache(Data key) {
        if (key == null) {
            return;
        }
        //getService().invalidateNearCache(name, key);
    }

    @Override
    public void incrementCounter(String key, long incrementBy) {
        /*
        MapService service = getService();
        Data dataKey = service.toData(key, partitionStrategy);
        IncrementByOperation operation = new IncrementByOperation(name, dataKey, incrementBy);

        invokeOperation(dataKey, operation);
        invalidateNearCache(dataKey);
        */

    }

    private Object invokeOperation(Data key, KeyBasedMapOperation operation) {
        final NodeEngine nodeEngine = getNodeEngine();
        int partitionId = nodeEngine.getPartitionService().getPartitionId(key);
        operation.setThreadId(ThreadUtil.getThreadId());
        try {
            Future f;
            Object o;
            OperationService operationService = nodeEngine.getOperationService();
            if (mapConfig.isStatisticsEnabled()) {
                long time = System.currentTimeMillis();
                f = operationService
                        .createInvocationBuilder(SERVICE_NAME, operation, partitionId)
                        .setResultDeserialized(false)
                        .invoke();
                o = f.get();
                if (operation instanceof BasePutOperation)
                    localMapStats.incrementPuts(System.currentTimeMillis() - time);
                else if (operation instanceof BaseRemoveOperation)
                    localMapStats.incrementRemoves(System.currentTimeMillis() - time);
                else if (operation instanceof GetOperation)
                    localMapStats.incrementGets(System.currentTimeMillis() - time);

            } else {
                f = operationService.createInvocationBuilder(SERVICE_NAME, operation, partitionId)
                        .setResultDeserialized(false).invoke();
                o = f.get();
            }
            return o;
        } catch (Throwable t) {
            throw ExceptionUtil.rethrow(t);
        }
    }
}
