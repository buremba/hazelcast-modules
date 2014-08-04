package org.rakam.cache.hazelcast.treemap.operations;

import org.rakam.cache.hazelcast.treemap.TreeMapService;

import java.util.Map;

/**
 * Created by buremba <Burak Emre Kabakcı> on 15/07/14 02:34.
 */
public class GetOperation extends TreeMapBaseOperation {
    private Integer numberOfElements = null;
    private Map<String, Long> returnValue;

    public GetOperation(String name) {
        super(name);
    }

    public GetOperation() {

    }

    public GetOperation(String name, Integer numberOfElements) {
        super(name);
        this.numberOfElements = numberOfElements;
    }

    @Override
    public int getId() {
        return TreeMapSerializerFactory.GET;
    }

    @Override
    public void run() throws Exception {
        TreeMapService service = getService();
        if(numberOfElements==null)
            this.returnValue = service.getHLL(name).getAll();
        else
            this.returnValue = service.getHLL(name).getTopItems(numberOfElements);
    }

    @Override
    public Object getResponse() {
        return returnValue;
    }
}
