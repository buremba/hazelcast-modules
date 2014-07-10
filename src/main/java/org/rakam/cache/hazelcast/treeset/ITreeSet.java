/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.treeset;


import com.hazelcast.core.ICollection;

import java.util.Set;

public interface ITreeSet<E> extends Set<E>, ICollection<E> {

}

