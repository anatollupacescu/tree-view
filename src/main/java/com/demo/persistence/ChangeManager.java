package com.demo.persistence;

import com.demo.changelog.*;
import com.demo.graph.Graph;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;

public class ChangeManager {

    private final MultiValueMap persistence = new LinkedMultiValueMap<Graph, GraphChange>();
    private final GraphManager graphManager;

    public ChangeManager(GraphManager graphManager) {
        this.graphManager = graphManager;
    }

    public void createNodeAtLocation(String graphName, List<String> location, String title) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        Objects.requireNonNull(title);
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.NAME, title);
        data.put(ChangeDataParam.PARENT, location);
        GraphChange change = new GraphChange(ChangeType.ADD_CHILD, data);
        Graph graph = graphManager.getGraphByName(graphName);
        persistence.add(graph, change);
    }

    public void removeNodeAtLocation(String graphName, List<String> location, String name) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.PARENT, location);
        data.put(ChangeDataParam.NAME, name);
        GraphChange change = new GraphChange(ChangeType.REMOVE_CHILD, data);
        Graph graph = graphManager.getGraphByName(graphName);
        persistence.add(graph, change);
    }

    public Graph build(String graphName) {
        Graph graph = graphManager.getGraphByName(graphName);
        final GraphBuilder builder = new GraphBuilder(graph);
        List<GraphChange> changes = (List<GraphChange>) persistence.get(graph);
        return builder.build(changes);
    }
}
