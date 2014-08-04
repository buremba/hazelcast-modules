package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 20:05.
 */


public final class Counter implements Comparable<Counter>, DataSerializable, Serializable {
    public String id;
    public AtomicLong counter;

    Counter(String id, long counter) {
        this.id = id;
        this.counter = new AtomicLong(counter);
    }

    Counter(String id) {
        this.id = id;
        this.counter = new AtomicLong(0);
    }

    public long increment() {
        return counter.incrementAndGet();
    }

    public long get() {
        return counter.get();
    }

    public long incrementBy(long l) {
        return counter.addAndGet(l);
    }

    public boolean equals(Object obj) {
        if(obj instanceof Counter)
            return ((Counter) obj).id.equals(id);
        else
            return false;
    }

    @Override
    public int compareTo(Counter counter) {
        if(counter.id.equals(id))
            return 0;
        int gap = counter.counter.intValue() - this.counter.intValue();
        return gap>=0 ? gap+1 :gap-1;
    }

    public String toString() {
        return "Counter {id: "+id+", counter: "+counter+"}";
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeLong(counter.longValue());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readUTF();
        counter = new AtomicLong(in.readLong());
    }
}