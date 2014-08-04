package org.rakam.cache.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Created by buremba <Burak Emre Kabakcı> on 20/07/14 05:12.
 */
public class Executor {
    private static HazelcastInstance server0;
    private static HazelcastInstance server1;

    @BeforeClass
    public static void init() {
        server0 = Hazelcast.newHazelcastInstance();
        server1 = Hazelcast.newHazelcastInstance();

        Hazelcast.newHazelcastInstance();
        Hazelcast.newHazelcastInstance();
    }

    @AfterClass
    public static void destroy() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void server_dd() throws Exception {
        final HazelcastInstance hz1 = Hazelcast.newHazelcastInstance();
        final HazelcastInstance hz2 = Hazelcast.newHazelcastInstance();
        final HazelcastInstance hz3 = Hazelcast.newHazelcastInstance();

        for(int i=0; i<1000; i++) {
            server0.getAtomicLong(Integer.toString(i)).incrementAndGet();
        }
        Map<String, Integer> map = server0.getMap("map");
        for (int k = 0; k < 5; k++)
            map.put(UUID.randomUUID().toString(), 1);
        Map<Member, Future<Integer>> executor = server0.getExecutorService("executor").submitToAllMembers(new SumTask());
        for(Map.Entry<Member, Future<Integer>> member : executor.entrySet()) {
            member.getValue().get();
        }

    }
}
