package com.demo.controller;

import com.demo.graph.api.GraphNode;
import com.demo.graph.Node;
import com.demo.graph.api.GraphViewer;

import java.util.List;
import java.util.stream.Collectors;

public class NodeGraphViewer implements GraphViewer<Node> {

    @Override
    public List<String> list(GraphNode<Node> graph, List<String> location) {
        List<Node> childrenAtLocation = graph.navigate(location).getChildren();
        return childrenAtLocation.stream().map(Node::getName).collect(Collectors.toList());
    }
}
