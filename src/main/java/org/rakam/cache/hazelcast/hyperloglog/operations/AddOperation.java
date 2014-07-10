package org.rakam.cache.hazelcast.hyperloglog.operations; /**
 * Created by buremba on 08/07/14.
 */
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
import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.HyperLogLogBackupAwareOperation;
import org.rakam.util.HLLWrapper;

import java.io.IOException;

public class AddOperation extends HyperLogLogBackupAwareOperation {

    private String item;

    public AddOperation() {
    }

    public AddOperation(String name, String item) {
        super(name);
        this.item = item;
    }

    @Override
    public void run() throws IllegalArgumentException {
        HLLWrapper number = getHLL();
        number.add(item);
    }

    @Override
    public int getId() {
        return HyperLogLogSerializerFactory.ADD;
    }

    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeUTF(item);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        item = in.readUTF();
    }

    @Override
    public Operation getBackupOperation() {
        return new AddBackupOperation(name, item);
    }
}
