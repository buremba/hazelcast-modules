package org.rakam.cache.hazelcast.treemap.client;

import com.hazelcast.nio.serialization.Portable;

/**
 * Created by buremba <Burak Emre Kabakcı> on 19/07/14 20:04.
 */
public class TreeMapPortableFactory implements com.hazelcast.nio.serialization.PortableFactory {

    static final int F_ID = 103;

    public static final int ADD = 1;
    public static final int RESET = 6;


    @Override
    public Portable create(int classId) {
        switch (classId) {
            case ADD:
                return new IncrementByRequest();
            case RESET:
                return new GetRequest();
            default:
                return null;
        }
    }
}