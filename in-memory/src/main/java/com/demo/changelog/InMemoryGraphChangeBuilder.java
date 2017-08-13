package com.demo.changelog;

import com.demo.graph.api.GraphChangeBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InMemoryGraphChangeBuilder implements GraphChangeBuilder<GraphChange> {

    private GraphChange createNodeAtLocation(List<String> location, String name) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = ChangeData.of(name, location);
        return new GraphChange(ChangeType.ADD_CHILD, data);
    }

    private GraphChange removeNodeAtLocation(List<String> location, String name) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = ChangeData.of(name, location);
        return new GraphChange(ChangeType.REMOVE_CHILD, data);
    }

    @Override
    public GraphChange init(String graphName) {
        Objects.requireNonNull(graphName);
        ChangeData data = ChangeData.of(graphName, Collections.emptyList());
        return new GraphChange(ChangeType.CREATE_ROOT, data);
    }

    @Override
    public GraphChange remove(List<String> location, String name) {
        return removeNodeAtLocation(location, name);
    }

    @Override
    public GraphChange add(List<String> location, String name) {
        return createNodeAtLocation(location, name);
    }
}
