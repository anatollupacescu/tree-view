package com.demo.controller;

import com.demo.graph.Graph;
import com.demo.graph.Node;
import com.demo.persistence.ChangeManager;
import com.demo.persistence.GraphManager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphController {

    GraphManager manager = new GraphManager();
    ChangeManager changeManager = new ChangeManager(manager);

    public Set<String> listGraphNames() {
        return manager.getNames();
    }

    public List<String> listNodesAtLocation(String name, List<String> location) {
        Graph graph = manager.getGraphByName(name);
        List<Node> childrenAtLocation = graph.navigate(location).getChildren();
        return childrenAtLocation.stream().map(Node::getName).collect(Collectors.toList());
    }

    public void createGraph(String name) {
        manager.createGraph(name);
    }

    public void createNodeAtLocation(String graphName, List<String> location, String title) {
        changeManager.createNodeAtLocation(graphName, location, title);
    }

    public void removeGraph(String graphName) {
        manager.removeGraph(graphName);
    }

    public void removeNodeAtLocation(String graphName, List<String> location, String title) {
        changeManager.removeNodeAtLocation(graphName, location, title);
    }
}
