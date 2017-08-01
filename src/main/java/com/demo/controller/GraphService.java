package com.demo.controller;

import com.demo.changelog.GraphBuilder;
import com.demo.changelog.GraphChange;
import com.demo.graph.Graph;
import com.demo.graph.Node;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphService {

    private final GraphBuilder graphBuilder;
    private final ChangeService changeService;

    public GraphService(GraphBuilder graphBuilder, ChangeService changeService) {
        this.graphBuilder = graphBuilder;
        this.changeService = changeService;
    }

    public Set<String> listGraphNames() {
        return changeService.getNames();
    }

    public List<String> listNodesAtLocation(String name, List<String> location) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(location);
        Graph graph = build(name);
        List<Node> childrenAtLocation = graph.navigate(location).getChildren();
        return childrenAtLocation.stream().map(Node::getName).collect(Collectors.toList());
    }

    private Graph build(String graph) {
        List<GraphChange> changes = changeService.getChangesOrFail(graph);
        return graphBuilder.build(graph, changes);
    }

    public void createNodeAtLocation(String graphName, List<String> location, String title) {
        Objects.requireNonNull(graphName);
        changeService.createNodeAtLocation(graphName, location, title);
    }

    public void removeNodeAtLocation(String graphName, List<String> location, String title) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        changeService.removeNodeAtLocation(graphName, location, title);
    }
}
