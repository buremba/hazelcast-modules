package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.core.DistributedObject;

import java.util.Map;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 01:06.
 */
public interface ITreeMap extends DistributedObject {
    public void increment(String key, long by);

    Map<String, Long> getAll();
    Map<String, Long> getTopItems(int numberOfElements);
}
