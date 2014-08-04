package org.rakam.cache.hazelcast;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 05:27.
 */

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.query.Predicates;
import com.hazelcast.query.SqlPredicate;
import com.hazelcast.query.impl.Index;
import com.hazelcast.query.impl.QueryContext;
import com.hazelcast.query.impl.QueryableEntry;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

public class Executore {



    static {
        System.setSecurityManager(null);


    }

    public static void main(String[] args) throws Exception {

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(new Config());
        HazelcastInstance hz1 = Hazelcast.newHazelcastInstance(new Config());
        IMap<String, ComparableAtomicLong> map = hz.getMap("map");
        map.addIndex("this", true);

        for (int k = 0; k < 10; k++) {
            String substring = UUID.randomUUID().toString().substring(10);
            map.put("fdsfds:"+substring, new ComparableAtomicLong(substring, Math.abs(new Random().nextInt()/10000)));
        }

        Collection<ComparableAtomicLong> values = map.values(new LimitPredicate("this", 10));
        for(ComparableAtomicLong l : map.values(new SqlPredicate("this>0"))) {
            System.out.println(l.get());
        }
        IExecutorService executor = hz.getExecutorService("executor");
        Map<Member, Future<Integer>> result = executor.submitToAllMembers(new SumTask());
        int sum = 0;
        for (Future<Integer> future : result.values())
            sum += future.get();

        System.out.println("Result: " + sum);
    }


    public static class LimitPredicate extends Predicates.AbstractPredicate {
        protected Integer limit;
        protected Integer cursor;

        public LimitPredicate() {
        }

        public LimitPredicate(String attribute, Integer limit) {
            super(attribute);
            this.limit = limit;
        }

        public Set<QueryableEntry> filter(QueryContext queryContext) {
            Index index = getIndex(queryContext);

            //index.indexStore
            return index.getRecords(new Comparable() {
                @Override
                public int compareTo(Object o) {
                    return 0;
                }
            });
        }

        public boolean apply(Map.Entry mapEntry) {
            return cursor-->0;
        }

        public void writeData(ObjectDataOutput out) throws IOException {
            super.writeData(out);
            out.writeInt(limit);
        }

        public void readData(ObjectDataInput in) throws IOException {
            super.readData(in);
            limit = in.readInt();
            cursor = limit;
        }

        @Override
        public String toString() {
            return attribute + " limit ";
        }
    }
}
