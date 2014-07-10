package org.rakam.cache.hazelcast.hyperloglog.operations;

/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.hazelcast.spi.AbstractOperation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogService;
import org.rakam.util.HLLWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CounterReplicationOperation extends AbstractOperation implements IdentifiedDataSerializable {

    private Map<String, byte[]> migrationData;

    public CounterReplicationOperation() {
    }

    public CounterReplicationOperation(Map<String, byte[]> migrationData) {
        this.migrationData = migrationData;
    }

    @Override
    public void run() throws Exception {
        HyperLogLogService atomicLongService = getService();
        for (Map.Entry<String, byte[]> longEntry : migrationData.entrySet()) {
            String name = longEntry.getKey();
            HLLWrapper number = atomicLongService.getHLL(name);
            byte[] value = longEntry.getValue();
            number.set(value);
        }
    }

    @Override
    public String getServiceName() {
        return HyperLogLogService.SERVICE_NAME;
    }

    @Override
    public int getFactoryId() {
        return HyperLogLogSerializerFactory.F_ID;
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.REPLICATION;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        out.writeInt(migrationData.size());
        for (Map.Entry<String, byte[]> entry : migrationData.entrySet()) {
            out.writeUTF(entry.getKey());
            byte[] val = entry.getValue();
            out.writeInt(val.length);
            out.write(val);
        }
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        int mapSize = in.readInt();
        migrationData = new HashMap<String, byte[]>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            String name = in.readUTF();
            int number = in.readInt();
            byte[] a = new byte[number];
            for (int idx = 0; idx < number; idx++)
                a[idx] = in.readByte();
            migrationData.put(name, a);
        }
    }
}
