package org.rakam.cache.hazelcast.treemap.operations;

import com.hazelcast.spi.BackupOperation;
import org.rakam.cache.hazelcast.treemap.OrderedCounterMap;
import org.rakam.cache.hazelcast.treemap.TreeMapService;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 03:23.
 */
public class IncrementByBackupOperation extends TreeMapBaseOperation implements BackupOperation {
    private long by;

    public IncrementByBackupOperation(String name, long by) {
        super(name);
        this.by = by;
    }

    public IncrementByBackupOperation() {

    }

    @Override
    public void run() throws Exception {
        OrderedCounterMap map = ((TreeMapService) getService()).getHLL(name);

        map.increment(name, by);
    }

    @Override
    public int getId() {
        return TreeMapSerializerFactory.ADD_BACKUP;
    }
}
