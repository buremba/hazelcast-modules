package org.rakam.cache.hazelcast.customMap;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 23:11.
 */
public interface OrderingCounterMap<String, ComparableAtomicLong> {
    void incrementCounter(String key, long incrementBy);
}
