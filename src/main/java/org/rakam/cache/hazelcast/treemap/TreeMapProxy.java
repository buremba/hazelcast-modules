package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.spi.*;
import com.hazelcast.util.ExceptionUtil;
import org.rakam.cache.hazelcast.treemap.operations.GetOperation;
import org.rakam.cache.hazelcast.treemap.operations.IncrementByOperation;

import java.util.Map;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 01:26.
 */
public class TreeMapProxy extends AbstractDistributedObject<TreeMapService> implements ITreeMap {
    private final String name;
    private final int partitionId;

    public TreeMapProxy(String name, NodeEngine nodeEngine, TreeMapService service) {
        super(nodeEngine, service);
        this.name = name;
        this.partitionId = nodeEngine.getPartitionService().getPartitionId(getNameAsPartitionAwareData());
    }

    private <E> InternalCompletableFuture<E> asyncInvoke(Operation operation) {
        try {
            OperationService operationService = getNodeEngine().getOperationService();
            return (InternalCompletableFuture<E>) operationService.invokeOnPartition(
                    TreeMapService.SERVICE_NAME, operation, partitionId);
        } catch (Throwable throwable) {
            throw ExceptionUtil.rethrow(throwable);
        }
    }

    @Override
    public String getServiceName() {
        return TreeMapService.SERVICE_NAME;
    }

    @Override
    public String toString() {
        return "Map{" + "name='" + name + '\'' + '}';
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void increment(String key, long by) {
        asyncIncrementBy(key, by).getSafely();
    }

    public InternalCompletableFuture<Void> asyncIncrementBy(String key, long by) {
        Operation operation = new IncrementByOperation(name, key, by);
        return asyncInvoke(operation);
    }

    @Override
    public Map<String, Long> getAll() {
        Operation operation = new GetOperation(name);
        return (Map<String, Long>) asyncInvoke(operation).getSafely();
    }

    @Override
    public Map<String, Long> getTopItems(int numberOfElements) {
        Operation operation = new GetOperation(name, numberOfElements);
        return (Map<String, Long>) asyncInvoke(operation).getSafely();
    }
}
