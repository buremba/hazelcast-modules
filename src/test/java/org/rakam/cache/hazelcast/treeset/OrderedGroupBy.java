package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

/**
 * Created by buremba <Burak Emre Kabakcı> on 14/07/14 01:16.
 */
public final class OrderedGroupBy implements Comparable<OrderedGroupBy>, DataSerializable {
    private String item;
    private final static HazelcastInstance client0 = HazelcastClient.newHazelcastClient();

    OrderedGroupBy() {

    }

    OrderedGroupBy(String item) {
        this.item = item;
    }
    @Override
    public int compareTo(OrderedGroupBy s) {
        return (int) (client0.getAtomicLong(item).get() - client0.getAtomicLong(s.item).get());
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(item);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        item = in.readUTF();
    }

    public String toString() {
        return item.toString();
    }
}