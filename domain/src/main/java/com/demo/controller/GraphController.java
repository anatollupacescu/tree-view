package com.demo.controller;

import com.demo.api.Api;
import com.demo.api.GraphNode;
import com.demo.changelog.GraphChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphController implements Api.GraphController {

    private final Api.Persistence<GraphChange> persistence;
    private final Api.GraphChangeBuilder<GraphChange> graphChangeBuilder;
    private final Api.GraphBuilder builder;
    private final Api.GraphViewer viewer;

    public GraphController(Api.Persistence<GraphChange> persistence,
                           Api.GraphChangeBuilder graphChangeBuilder,
                           Api.GraphBuilder builder,
                           Api.GraphViewer viewer) {
        this.persistence = persistence;
        this.graphChangeBuilder = graphChangeBuilder;
        this.builder = builder;
        this.viewer = viewer;
    }

    @Override
    public Set<String> getNames() {
        return persistence.getNames();
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        List<GraphChange> changesByName = persistence.getByName(graphName);
        GraphNode graph = builder.build(changesByName);
        return viewer.list(graph, location);
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        GraphChange change = graphChangeBuilder.add(location, nodeName);
        List<GraphChange> changesByName = persistence.getByName(graphName);
        validateChange(changesByName, change);
        persistence.store(graphName, change);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        GraphChange change = graphChangeBuilder.remove(location, nodeName);
        List<GraphChange> changesByName = persistence.getByName(graphName);
        checkGraphIsPresent(changesByName);
        validateChange(changesByName, change);
        persistence.store(graphName, change);
    }

    private void checkGraphIsPresent(List<GraphChange> changesByName) {
        if (changesByName.isEmpty()) throw new Api.GraphNotFoundException();
    }

    private void validateChange(List<GraphChange> graphChanges, GraphChange change) {
        List<GraphChange> changes = new ArrayList<>(graphChanges);
        changes.add(change);
        builder.build(changes);
    }
}
