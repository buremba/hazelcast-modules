/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */
package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.core.ICompletableFuture;
import org.rakam.util.HLLWrapper;

import java.util.Collection;

public interface AsyncHyperLogLog extends HyperLogLog {
    ICompletableFuture<Void> asyncAdd(String item);

    ICompletableFuture<Long> asyncCardinality();

    ICompletableFuture<Void> asyncUnion(HLLWrapper hll);

    ICompletableFuture<Void> asyncAddAll(Collection<String> coll);
}
