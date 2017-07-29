package com.demo.persistence;

import com.demo.graph.Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GraphManager {

    final Map<String, Graph> persistence;

    public GraphManager() {
        this.persistence = new HashMap<>();
    }

    public void createGraph(String name) {
        Objects.requireNonNull(name);
        Graph graph = Graph.withName(name);
        persistence.put(name, graph);
    }

    public Set<String> getNames() {
        return persistence.keySet();
    }

    public void removeGraph(String graphName) {
        Objects.requireNonNull(graphName);
        persistence.remove(graphName);
    }

    public Graph getGraphByName(String graphName) {
        Objects.requireNonNull(graphName);
        return persistence.get(graphName);
    }
}
