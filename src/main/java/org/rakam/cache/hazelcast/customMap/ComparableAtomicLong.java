package org.rakam.cache.hazelcast.customMap;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 06:38.
 */
public class ComparableAtomicLong extends AtomicLong implements Comparable<Number> {
    public ComparableAtomicLong(long i) {
        super(i);
    }

    @Override
    public int compareTo(Number number) {
        return this.intValue() - number.intValue();
    }
}
