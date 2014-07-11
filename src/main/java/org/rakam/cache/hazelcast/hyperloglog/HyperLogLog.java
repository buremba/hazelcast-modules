/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.core.DistributedObject;
import org.rakam.util.HLLWrapper;

import java.util.Collection;

public interface HyperLogLog extends DistributedObject {
    /**
     * Returns the name of this HyperLogLog instance.
     *
     * @return name of this instance
     */

    String getName();

    /**
     * Returns the cardinality of the HyperLogLog container
     */
    public long cardinality();

    public void reset();

    /**
     * Unions given hll container with the internal one.
     *
     * @param hll the hll container to marge
     */
    public void union(HLLWrapper hll);

    public void addAll(Collection<String> coll);

    public void add(String obj);
}
