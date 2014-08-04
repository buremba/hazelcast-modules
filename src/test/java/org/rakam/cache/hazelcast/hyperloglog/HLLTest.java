/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.*;
import org.rakam.util.HLLWrapper;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class HLLTest {
    private static HazelcastInstance client0;

    static final String name = "COUNTER";
    static HazelcastInstance server0;
    static HazelcastInstance server1;
    private static HyperLogLog hll0;
    private static HyperLogLog hll1;
    private static HyperLogLog hllClient;

    @BeforeClass
    public static void init() {
        server0 = Hazelcast.newHazelcastInstance();
        server1 = Hazelcast.newHazelcastInstance();

        client0 = HazelcastClient.newHazelcastClient();
        hllClient = client0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);

    }

    @AfterClass
    public static void destroy() {
        Hazelcast.shutdownAll();
    }

    @Before
    @After
    public void clear() throws IOException {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll1 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll0.reset();
        hll1.reset();
    }

    @Test
    public void cardinality_testAcrossServers() throws Exception {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll1 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll0.add("TEST");

        assertEquals(1, hll0.cardinality());
        assertEquals(1, hll1.cardinality());
        assertEquals(1, hllClient.cardinality());
    }

    @Test
    public void destroy_testAcrossServers() throws Exception {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll1 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);

        hll0.add("TEST0");
        hll0.add("TEST1");
        hll0.reset();
        hll0.add("TEST0");

        assertEquals(1, hll0.cardinality());
        assertEquals(1, hll1.cardinality());
        assertEquals(1, hllClient.cardinality());
    }

    @Test
    public void addAll_testAcrossServers() throws Exception {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll1 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);

        ArrayList<String> list = new ArrayList<String>() {{
            add("TEST0");
            add("TEST1");
            add("TEST2");
        }};
        hll0.addAll(list);
        hll1.addAll(list);

        assertEquals(3, hll0.cardinality());
        assertEquals(3, hll1.cardinality());
        assertEquals(3, hllClient.cardinality());
    }

    @Test
    public void union_testAcrossServers() throws Exception {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);
        hll1 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);

        hll0.add("TEST1");
        HLLWrapper hll = new HLLWrapper();
        hll.add("TEST0");
        hll0.union(hll);

        assertEquals(2, hll0.cardinality());
        assertEquals(2, hll1.cardinality());
        assertEquals(2, hllClient.cardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_whenCalledWithNullFunction() {
        hll0 = server0.getDistributedObject(HyperLogLogService.SERVICE_NAME, name);

        hll0.add(null);
    }

}
