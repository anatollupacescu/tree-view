package com.demo.changelog;

import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.List;
import java.util.Objects;

public class GraphBuilder {

    public Graph build(String name, List<GraphChange> changes) {
        Graph graph = Graph.withName(name);
        Objects.requireNonNull(changes);
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
            default:
                throw new IllegalArgumentException();
        }
    }

    private void removeChild(Graph graph, GraphChange change) {
        ChangeData data = change.getData();
        String name = (String) data.get(ChangeDataParam.NAME);
        List<String> parentNodePath = (List<String>) data.get(ChangeDataParam.PARENT);
        Node parentNode;
        if (parentNodePath == null || parentNodePath.isEmpty()) {
            parentNode = graph;
        } else {
            parentNode = graph.navigate(parentNodePath);
        }
        Node node = new Node(name, parentNode);
        parentNode.removeChild(node);
    }

    private void addChild(Graph graph, GraphChange change) {
        ChangeData data = change.getData();
        String name = (String) data.get(ChangeDataParam.NAME);
        List<String> parentNodePath = (List<String>) data.get(ChangeDataParam.PARENT);
        Node parentNode;
        if (parentNodePath == null) {
            parentNode = graph;
        } else {
            parentNode = graph.navigate(parentNodePath);
        }
        Node node = new Node(name, parentNode);
        parentNode.addChild(node);
    }
}
