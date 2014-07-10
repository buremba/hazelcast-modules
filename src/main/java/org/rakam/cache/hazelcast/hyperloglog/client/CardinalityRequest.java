/**
 * Created by buremba <Burak Emre Kabakcı> on 10/07/14.
 */

package org.rakam.cache.hazelcast.hyperloglog.client;

import com.hazelcast.spi.Operation;
import org.rakam.cache.hazelcast.hyperloglog.operations.CardinalityOperation;

public class CardinalityRequest extends ReadRequest {
    public CardinalityRequest(String name) {
        super(name);
    }

    public CardinalityRequest() {

    }

    @Override
    protected Operation prepareOperation() {
        return new CardinalityOperation(name);
    }

    @Override
    public int getClassId() {
        return HyperLogLogPortableFactory.CARDINALITY;
    }
}
