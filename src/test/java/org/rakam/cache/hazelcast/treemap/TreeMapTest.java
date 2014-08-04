package org.rakam.cache.hazelcast.treemap;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 03:31.
 */
public class TreeMapTest {

    private static HazelcastInstance server0;

    @BeforeClass
    public static void init() {
        server0 = Hazelcast.newHazelcastInstance();
    }

    @AfterClass
    public static void destroy() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void server_test() throws Exception {
       ITreeMap map = server0.getDistributedObject(TreeMapService.SERVICE_NAME, "deneme");
       map.increment("fsdfsd", 3);

       Map<String, Long> it = map.getAll();
       for(Map.Entry<String, Long> i : it.entrySet())
            System.out.println(i);


    }

}
