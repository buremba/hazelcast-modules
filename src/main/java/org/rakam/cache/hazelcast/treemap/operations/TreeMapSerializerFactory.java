package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 02:36.
 */
public final class TreeMapSerializerFactory implements DataSerializableFactory {

    public static final int F_ID = 103;

    public static final int ADD = 0;
    public static final int GET = 1;
    public static final int REPLICATION = 7;
    public static final int ADD_BACKUP = 2;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case ADD:
                return new IncrementByOperation();
            case ADD_BACKUP:
                return new IncrementByBackupOperation();
            case GET:
                return new GetOperation();
            case REPLICATION:
                return new TreeMapReplicationOperation();
            default:
                return null;
        }
    }
}