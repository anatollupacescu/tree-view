package com.demo;

import com.demo.api.GraphNode;
import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassGraphTester extends GraphTester {

    private final GraphNode<Node> graph;
    private final Set<String> names;

    public ClassGraphTester(String name) {
        this.names = Collections.singleton(name);
        this.graph = Graph.withName(name);
    }

    @Override
    public Set<String> getNames() {
        return names;
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        Node node = graph.navigate(location);
        return node.getChildren().stream().map(Node::getName).collect(Collectors.toList());
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        Node parent = graph.navigate(location);
        parent.addChild(nodeName);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        Node parent = graph.navigate(location);
        parent.removeChild(nodeName);
    }

    @Override
    public int getChildrenCount() {
        return graph.getChildrenCount();
    }
}
