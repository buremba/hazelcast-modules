package org.rakam.cache.hazelcast.customMap;

import com.hazelcast.concurrent.lock.LockService;
import com.hazelcast.concurrent.lock.LockStoreInfo;
import com.hazelcast.map.MapContainer;
import com.hazelcast.map.MapService;
import com.hazelcast.spi.NodeEngine;
import com.hazelcast.spi.ObjectNamespace;
import com.hazelcast.util.ConstructorFunction;

import java.util.Properties;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 23:08.
 */
public class CustomMapService extends MapService {
    public final static String SERVICE_NAME = "rakam:counterMapService";

    public CustomMapService(NodeEngine nodeEngine) {
        super(nodeEngine);
    }

    public void init(final NodeEngine nodeEngine, Properties properties) {
        try {
            super.init(nodeEngine, properties);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        final LockService lockService = nodeEngine.getSharedService(LockService.SERVICE_NAME);
        if (lockService != null) {
            lockService.registerLockStoreConstructor(SERVICE_NAME, new ConstructorFunction<ObjectNamespace, LockStoreInfo>() {
                public LockStoreInfo createNew(final ObjectNamespace key) {
                    final MapContainer mapContainer = getMapContainer(key.getObjectName());
                    return new LockStoreInfo() {
                        public int getBackupCount() {
                            return mapContainer.getBackupCount();
                        }

                        public int getAsyncBackupCount() {
                            return mapContainer.getAsyncBackupCount();
                        }
                    };
                }
            });
        }
    }

}
