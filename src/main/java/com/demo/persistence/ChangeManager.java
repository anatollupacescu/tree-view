package com.demo.persistence;

import com.demo.changelog.*;
import com.demo.graph.Graph;
import com.demo.graph.Node;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChangeManager {

    private final MultiValueMap persistence = new LinkedMultiValueMap<Graph, GraphChange>();
    private final GraphManager graphManager = new GraphManager();

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

    public void removeNodeAtLocation(String graphName, List<String> location) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.LOCATION, location);
        GraphChange change = new GraphChange(ChangeType.REMOVE_CHILD, data);
        Graph graph = graphManager.getGraphByName(graphName);
        persistence.add(graph, change);
    }


    public Graph build() {
        
        return graph;
    }
}
