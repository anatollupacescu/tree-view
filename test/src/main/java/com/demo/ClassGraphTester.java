package com.demo;

import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassGraphTester extends GraphTester {

    private final Graph graph;

    public ClassGraphTester(String graph) {
        this.graph = Graph.withName(graph);
    }

    @Override
    public Set<String> getNames() {
        return Collections.singleton(graph.getName());
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        Node node = graph.navigate(location);
        return node.getChildren().stream().map(Node::getName).collect(Collectors.toList());
    }

    @Override
    public void add(String graphName, List<String> location, String name) {
        Node parent = graph.navigate(location);
        parent.addChild(name);
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
