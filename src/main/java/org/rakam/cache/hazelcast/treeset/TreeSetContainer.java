/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

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


import com.hazelcast.collection.CollectionItem;
import com.hazelcast.collection.TxCollectionItem;
import com.hazelcast.collection.set.SetContainer;
import com.hazelcast.config.SetConfig;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.NodeEngine;

import java.io.IOException;
import java.util.*;

public class TreeSetContainer extends SetContainer {

    private static final int INITIAL_CAPACITY = 1000;
    private Set<CollectionItem> itemSet;
    private SetConfig config;
    private long idGenerator;

    public TreeSetContainer() {
    }

    public TreeSetContainer(String name, NodeEngine nodeEngine) {
        super(name, nodeEngine);
    }

    @Override
    protected SetConfig getConfig() {
        if (config == null) {
            config = nodeEngine.getConfig().findSetConfig(name);
        }
        return config;
    }

    @Override
    protected Map<Long, Data> addAll(List<Data> valueList) {
        final int size = valueList.size();
        final Map<Long, Data> map = new HashMap<Long, Data>(size);
        List<CollectionItem> list = new ArrayList<CollectionItem>(size);
        for (Data value : valueList) {
            final long itemId = nextId();
            final CollectionItem item = new OrderedCollectionItem(itemId, value);
            if (!getCollection().contains(item)) {
                list.add(item);
                map.put(itemId, value);
            }
        }
        getCollection().addAll(list);

        return map;
    }

    @Override
    protected long add(Data value) {
        final CollectionItem item = new OrderedCollectionItem(nextId(), value);
        if (getCollection().add(item)) {
            return item.getItemId();
        }
        return -1;
    }

    @Override
    public void commitAddBackup(long itemId, Data value) {
        CollectionItem item = txMap.remove(itemId);
        if (item == null) {
            item = new OrderedCollectionItem(itemId, value);
        }
        getMap().put(itemId, item);
    }

    public void readData(ObjectDataInput in) throws IOException {
        name = in.readUTF();
        final int collectionSize = in.readInt();
        final Collection<CollectionItem> collection = getCollection();
        for (int i = 0; i < collectionSize; i++) {
            final CollectionItem item = new OrderedCollectionItem();
            item.readData(in);
            collection.add(item);
            setId(item.getItemId());
        }

        final int txMapSize = in.readInt();
        for (int i = 0; i < txMapSize; i++) {
            final TxCollectionItem txCollectionItem = new TxCollectionItem();
            txCollectionItem.readData(in);
            txMap.put(txCollectionItem.getItemId(), txCollectionItem);
            setId(txCollectionItem.getId());
        }
    }

    void setId(long itemId) {
        idGenerator = Math.max(itemId + 1, idGenerator);
    }

    public long nextId() {
        return idGenerator++;
    }

    @Override
    protected void addBackup(long itemId, Data value) {
        final CollectionItem item = new OrderedCollectionItem(itemId, value);
        getMap().put(itemId, item);
    }

    @Override
    protected void addAllBackup(Map<Long, Data> valueMap) {
        Map<Long, CollectionItem> map = new HashMap<Long, CollectionItem>(valueMap.size());
        for (Map.Entry<Long, Data> entry : valueMap.entrySet()) {
            final long itemId = entry.getKey();
            map.put(itemId, new OrderedCollectionItem(itemId, entry.getValue()));
        }
        getMap().putAll(map);

    }

    @Override
    public Set<CollectionItem> getCollection() {
        if (itemSet == null) {
            if (itemMap != null && !itemMap.isEmpty()) {
                itemSet = new TreeSet<CollectionItem>(itemMap.values());
                itemMap.clear();
            } else {
                itemSet = new TreeSet<CollectionItem>();
            }
            itemMap = null;
        }
        return itemSet;
    }

    @Override
    protected Map<Long, CollectionItem> getMap() {
        if (itemMap == null) {
            if (itemSet != null && !itemSet.isEmpty()) {
                itemMap = new HashMap<Long, CollectionItem>(itemSet.size());
                for (CollectionItem item : itemSet) {
                    itemMap.put(item.getItemId(), item);
                }
                itemSet.clear();
            } else {
                itemMap = new HashMap<Long, CollectionItem>(INITIAL_CAPACITY);
            }
            itemSet = null;
        }
        return itemMap;
    }

    @Override
    protected void onDestroy() {
        if (itemSet != null) {
            itemSet.clear();
        }
    }

    public class OrderedCollectionItem extends CollectionItem {
        public OrderedCollectionItem(long itemId, Data value) {
            this.itemId = itemId;
            this.value = value;

        }

        public OrderedCollectionItem() {

        }

        @Override
        public int compareTo(CollectionItem o) {
            return ((Comparable) nodeEngine.toObject(this.getValue())).compareTo(nodeEngine.toObject(o.getValue()));
        }
    }
}
