package org.rakam.cache.hazelcast.hyperloglog.operations;

import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

public final class HyperLogLogSerializerFactory implements DataSerializableFactory {

    public static final int F_ID = 100;

    public static final int ADD = 0;
    public static final int ADD_BACKUP = 1;
    public static final int ADD_ALL = 2;
    public static final int ADD_ALL_BACKUP = 3;
    public static final int CARDINALITY = 4;
    public static final int UNION = 5;
    public static final int UNION_BACKUP = 6;
    public static final int REPLICATION = 7;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case ADD_BACKUP:
                return new AddBackupOperation();
            case ADD:
                return new AddOperation();
            case ADD_ALL_BACKUP:
                return new AddAllBackupOperation();
            case ADD_ALL:
                return new AddAllOperation();
            case CARDINALITY:
                return new CardinalityOperation();
            case UNION_BACKUP:
                return new UnionBackupOperation();
            case UNION:
                return new UnionOperation();
            case REPLICATION:
                return new CounterReplicationOperation();
            default:
                return null;
        }
    }
}

