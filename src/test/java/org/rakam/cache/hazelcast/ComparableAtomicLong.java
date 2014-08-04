package org.rakam.cache.hazelcast;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 06:38.
 */
public class ComparableAtomicLong extends AtomicLong implements Comparable<Number> {
    private final String key;

    public ComparableAtomicLong(String key, int i) {
        super(i);
        this.key = key;
    }

    @Override
    public int compareTo(Number number) {
        return this.intValue() - number.intValue();
    }
}
