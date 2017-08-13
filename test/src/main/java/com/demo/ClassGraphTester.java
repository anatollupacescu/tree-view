package com.demo;

import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

public class ClassGraphTester extends GraphTester {

    private final static Map<String, Graph> graphs = new HashMap<>();

    @Override
    public Set<String> getNames() {
        return new HashSet<>(graphs.keySet());
    }

    @Override
    public void remove(String graphName) {
        graphs.remove(graphName);
    }

    @Override
    public void create(String graphName) {
        graphs.put(graphName, Graph.withName(graphName));
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        Graph graph = graphs.get(graphName);
        Node node = graph.navigate(location);
        return node.getChildren().stream().map(Node::getName).collect(Collectors.toList());
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        Graph graph = graphs.get(graphName);
        Node parent = graph.navigate(location);
        parent.addChild(nodeName);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        Graph graph = graphs.get(graphName);
        Node parent = graph.navigate(location);
        parent.removeChild(nodeName);
    }
}
