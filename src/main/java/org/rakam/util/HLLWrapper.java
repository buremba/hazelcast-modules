/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.util;

import net.agkn.hll.HLL;
import org.rakam.cache.hazelcast.hyperloglog.MurmurHash3;

import java.util.Collection;

public class HLLWrapper {
    final private static int SEED = 123456;
    private HLL hll;

    public HLLWrapper() {
        hll = new HLL(13/*log2m*/, 5/*registerWidth*/);
    }

    public HLLWrapper(byte[] bytes) {
        hll = HLL.fromBytes(bytes);
    }

    public long cardinality() {
        return hll.cardinality();
    }

    public void union(HLLWrapper hll) {
        this.hll.union(hll.hll);
    }

    public void addAll(Collection<String> coll) {
        for (String a : coll) {
            byte[] s = a.getBytes();
            hll.addRaw(MurmurHash3.murmurhash3x8632(s, 0, s.length, SEED));
        }
    }

    public void add(String obj) {
        if (obj == null)
            throw new IllegalArgumentException();
        byte[] s = obj.getBytes();

        hll.addRaw(MurmurHash3.murmurhash3x8632(s, 0, s.length, SEED));
    }

    public void set(byte[] bytes) {
        hll = HLL.fromBytes(bytes);
    }

    public byte[] bytes() {
        return hll.toBytes();
    }
}