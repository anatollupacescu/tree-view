package com.demo.changelog;

import com.demo.graph.Graph;
import com.demo.graph.Node;
import com.demo.graph.api.GraphBuilder;

import java.util.List;
import java.util.Objects;

public class InMemoryGraphBuilder implements GraphBuilder<GraphChange> {

    public Graph build(String name, List<GraphChange> changes) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(changes);
        Graph graph = Graph.withName(name);
        for (GraphChange change : changes) {
            applyChange(graph, change);
        }
        return graph;
    }

    private void applyChange(Graph graph, GraphChange change) {
        switch (change.getType()) {
            case ADD_CHILD:
                addChild(graph, change);
                break;
            case REMOVE_CHILD:
                removeChild(graph, change);
                break;
            case CREATE_ROOT:
                break;
            default:
                throw new IllegalArgumentException("Unknown command");
        }
    }

    private void removeChild(Graph graph, GraphChange change) {
        ChangeData data = change.getData();
        String name = data.getName();
        List<String> parentNodePath = data.getLocation();
        Node parentNode;
        if (parentNodePath == null || parentNodePath.isEmpty()) {
            parentNode = graph;
        } else {
            parentNode = graph.navigate(parentNodePath);
        }
        parentNode.removeChild(name);
    }

    private void addChild(Graph graph, GraphChange change) {
        ChangeData data = change.getData();
        String name = data.getName();
        List<String> parentNodePath = data.getLocation();
        Node parentNode;
        if (parentNodePath == null) {
            parentNode = graph;
        } else {
            parentNode = graph.navigate(parentNodePath);
        }
        parentNode.addChild(name);
    }

    @Override
    public Graph build(List<GraphChange> changes) {
        return build("test", changes);
    }
}
