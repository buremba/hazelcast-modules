/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.spi.*;
import com.hazelcast.util.ExceptionUtil;
import org.rakam.cache.hazelcast.hyperloglog.operations.AddAllOperation;
import org.rakam.cache.hazelcast.hyperloglog.operations.AddOperation;
import org.rakam.cache.hazelcast.hyperloglog.operations.CardinalityOperation;
import org.rakam.cache.hazelcast.hyperloglog.operations.UnionOperation;
import org.rakam.util.HLLWrapper;

import java.util.Collection;

public class HyperLogLogProxy extends AbstractDistributedObject<HyperLogLogService> implements AsyncHyperLogLog {

    private final String name;
    private final int partitionId;

    public HyperLogLogProxy(String name, NodeEngine nodeEngine, HyperLogLogService service) {
        super(nodeEngine, service);
        this.name = name;
        this.partitionId = nodeEngine.getPartitionService().getPartitionId(getNameAsPartitionAwareData());
    }

    private <E> InternalCompletableFuture<E> asyncInvoke(Operation operation) {
        try {
            OperationService operationService = getNodeEngine().getOperationService();
            return (InternalCompletableFuture<E>) operationService.invokeOnPartition(
                    HyperLogLogService.SERVICE_NAME, operation, partitionId);
        } catch (Throwable throwable) {
            throw ExceptionUtil.rethrow(throwable);
        }
    }

    @Override
    public String getServiceName() {
        return HyperLogLogService.SERVICE_NAME;
    }

    @Override
    public String toString() {
        return "HyperLogLog{" + "name='" + name + '\'' + '}';
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPartitionId() {
        return partitionId;
    }

    @Override
    public long cardinality() {
        return asyncCardinality().getSafely();
    }

    @Override
    public void union(HLLWrapper hll) {
        asyncUnion(hll).getSafely();
    }

    @Override
    public void addAll(Collection<String> coll) {
        asyncAddAll(coll).getSafely();
    }

    @Override
    public void add(String obj) {
        asyncAdd(obj).getSafely();
    }

    @Override
    public InternalCompletableFuture<Void> asyncAdd(String item) {
        Operation operation = new AddOperation(name, item);
        return asyncInvoke(operation);
    }

    @Override
    public InternalCompletableFuture<Long> asyncCardinality() {
        CardinalityOperation operation = new CardinalityOperation(name);
        return asyncInvoke(operation);
    }

    @Override
    public InternalCompletableFuture<Void> asyncUnion(HLLWrapper hll) {
        UnionOperation operation = new UnionOperation(name, hll);
        return asyncInvoke(operation);
    }

    @Override
    public InternalCompletableFuture<Void> asyncAddAll(Collection<String> coll) {
        Operation operation = new AddAllOperation(name, coll);
        return asyncInvoke(operation);
    }
}