package com.demo.controller;

import com.demo.api.Api;
import com.demo.changelog.GraphChange;
import com.demo.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphController implements Api.GraphController {

    private final Api.Persistence<GraphChange> persistence;
    private final Api.GraphChangeService graphChangeService;
    private final Api.GraphBuilder builder;
    private final Api.GraphViewer viewer;

    public GraphController(Api.Persistence<GraphChange> persistence,
                           Api.GraphChangeService graphChangeService,
                           Api.GraphBuilder builder,
                           Api.GraphViewer viewer) {
        this.persistence = persistence;
        this.graphChangeService = graphChangeService;
        this.builder = builder;
        this.viewer = viewer;
    }

    @Override
    public Set<String> getNames() {
        return persistence.listNames();
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        List<GraphChange> changesByName = persistence.getChangesByName(graphName);
        Graph graph = builder.buildGraph(changesByName);
        return viewer.listNodeTitlesAtLocation(graph, location);
    }

    @Override
    public void add(String graphName, List<String> location, String changeData) {
        GraphChange change = graphChangeService.add(location, changeData);
        List<GraphChange> changesByName = persistence.getChangesByName(graphName);
        validateChange(changesByName, change);
        persistence.storeChange(graphName, change);
    }

    @Override
    public void remove(String graphName, List<String> location, String changeData) {
        GraphChange change = graphChangeService.remove(location, changeData);
        List<GraphChange> changesByName = persistence.getChangesByName(graphName);
        checkGraphIsPresent(changesByName);
        validateChange(changesByName, change);
        persistence.storeChange(graphName, change);
    }

    private void checkGraphIsPresent(List<GraphChange> changesByName) {
        if(changesByName.isEmpty()) throw new Api.GraphNotFoundException();
    }

    private void validateChange(List<GraphChange> graphChanges, GraphChange change) {
        List<GraphChange> changes = new ArrayList<>(graphChanges);
        changes.add(change);
        builder.buildGraph(changes);
    }
}
