/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.nio.serialization.Portable;

public class HyperLogLogPortableFactory implements com.hazelcast.nio.serialization.PortableFactory {

    static final int F_ID = 100;

    public static final int ADD = 1;
    public static final int ADD_ALL = 2;
    public static final int CARDINALITY = 4;
    public static final int UNION = 5;


    @Override
    public Portable create(int classId) {
        switch (classId) {
            case ADD:
                return new AddRequest();
            case ADD_ALL:
                return new AddAllRequest();
            case CARDINALITY:
                return new CardinalityRequest();
            case UNION:
                return new UnionRequest();
            default:
                return null;
        }
    }
}
