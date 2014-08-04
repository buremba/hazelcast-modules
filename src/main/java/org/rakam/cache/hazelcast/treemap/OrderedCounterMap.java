package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 19:49.
 */
public class OrderedCounterMap implements Iterable<Counter>, DataSerializable {
    Set<Counter> set = Collections.newSetFromMap(new ConcurrentSkipListMap());
    Map<String, Counter> map = new ConcurrentHashMap<String, Counter>();

    public long increment(String key, long by) {
        Counter c = map.get(key);

        if(c!=null) {
            return map.get(key).increment();
        }else {
            c = new Counter(key, by);
            map.put(c.id, c);
            set.add(c);
        }
        return by;
    }

    public Map<String, Long> getAll() {
        Map<String, Long> ret = new LinkedHashMap(set.size());
        Iterator<Counter> it = set.iterator();
        while(it.hasNext()) {
            Counter next = it.next();
            ret.put(next.id, next.get());
        }
        return ret;
    }

    public Map<String, Long> getTopItems(int numberOfItems) {
        Map<String, Long> ret = new LinkedHashMap(set.size());
        Iterator<Counter> it = set.iterator();
        int i = 0;
        while(it.hasNext() && numberOfItems>i) {
            Counter next = it.next();
            ret.put(next.id, next.get());
            i++;
        }
        return ret;
    }


    @Override
    public Iterator<Counter> iterator() {
        return set.iterator();
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(set.size());
        Iterator<Counter> it = set.iterator();
        while(it.hasNext()) {
            out.writeObject(it.next());
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int len = in.readInt();
        for(int i=0; i<len; i++) {
            Counter c = in.readObject();
            map.put(c.id, c);
            set.add(c);
        }
    }
}
