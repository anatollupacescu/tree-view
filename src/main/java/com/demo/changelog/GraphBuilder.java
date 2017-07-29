package com.demo.changelog;

import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.List;
import java.util.Objects;

public class GraphBuilder {

    private final Graph graph;

    public GraphBuilder(Graph graph) {
        this.graph = graph;
    }

    Graph build(List<GraphChange> changes) {
        Objects.requireNonNull(changes);
        for (GraphChange change : changes) {
            applyChange(change);
        }
        return graph;
    }

    private void applyChange(GraphChange change) {
        switch (change.getType()) {
            case ADD_CHILD:
                addChild(change);
                break;
            case REMOVE_CHILD:
                removeChild(change);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void removeChild(GraphChange change) {
        ChangeData data = change.getData();
        String name = (String) data.get("name");
        String parentNodeName = (String) data.get("parent");
        Node parentNode = graph.findChildByName(parentNodeName).orElse(graph);
        Node node = new Node(name, parentNode);
        parentNode.removeChild(node);
    }

    private void addChild(GraphChange change) {
        ChangeData data = change.getData();
        String name = (String) data.get("name");
        String parentNodeName = (String) data.get("parent");
        Node parentNode;
        if(parentNodeName == null) {
            parentNode = graph;
        } else {
            parentNode = graph.findChildByNameOrThrow(parentNodeName);
        }
        Node node = new Node(name, parentNode);
        parentNode.addChild(node);
    }
}
