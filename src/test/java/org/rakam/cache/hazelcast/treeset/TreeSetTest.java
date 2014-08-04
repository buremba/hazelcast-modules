/**
 * Created by buremba <Burak Emre Kabakcı> on 11/07/14.
 */

package org.rakam.cache.hazelcast.treeset;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.assertTrue;

public class TreeSetTest {
    private static HazelcastInstance client0;

    static final String name = "comparableset";
    static HazelcastInstance server0;
    private static ITreeSet set0;

    @BeforeClass
    public static void init() {
        server0 = Hazelcast.newHazelcastInstance();
        client0 = HazelcastClient.newHazelcastClient();

        set0 = server0.getDistributedObject(TreeSetService.SERVICE_NAME, name);
    }

    @AfterClass
    public static void destroy() {
        Hazelcast.shutdownAll();
    }

    @Before
    @After
    public void clear() throws IOException {
        set0.destroy();
    }

    @Test
    public void server_test() throws Exception {

        Set s = set0;
        s.add(new Author("AB"));
        s.add(new Author("AC"));
        s.add(new Author("AA"));
        s.add(new Author("A0"));

        Iterator<Comparable> it = s.iterator();
        Comparable previous = null;
        Comparable now;
        while (it.hasNext()) {
            now = it.next();
            if (previous != null)
                assertTrue(previous.compareTo(now) <= 0);
            previous = now;
        }
    }

    @Test
    public void customitem_test() throws Exception {

        Set s = set0;
        s.add(new OrderedGroupBy("AB"));
        s.add(new OrderedGroupBy("AC"));
        s.add(new OrderedGroupBy("AA"));
        s.add(new OrderedGroupBy("A0"));

        Iterator<Comparable> it = s.iterator();
        Comparable previous = null;
        Comparable now;
        while (it.hasNext()) {
            now = it.next();
            if (previous != null)
                assertTrue(previous.compareTo(now) <= 0);
            previous = now;
        }
    }


    @Test
    public void client_test() throws Exception {

        ITreeSet s = client0.getDistributedObject(TreeSetService.SERVICE_NAME, name);
        s.add(new Author("AB"));
        s.add(new Author("AC"));
        s.add(new Author("AA"));
        s.add(new Author("A0"));

        Iterator<Comparable> it = s.iterator();
        Comparable previous = null;
        Comparable now;
        while (it.hasNext()) {
            now = it.next();
            if (previous != null)
                assertTrue(previous.compareTo(now) <= 0);
            previous = now;
        }
    }

}
