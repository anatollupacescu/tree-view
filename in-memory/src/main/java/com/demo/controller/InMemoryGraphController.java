package com.demo.controller;

import com.demo.api.Api;
import com.demo.api.UserPass;
import com.demo.graph.api.GraphNode;
import com.demo.changelog.GraphChange;
import com.demo.graph.Node;
import com.demo.graph.api.GraphBuilder;
import com.demo.graph.api.GraphChangeBuilder;
import com.demo.graph.api.GraphViewer;
import com.demo.graph.api.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class InMemoryGraphController implements Api.GraphController {

    private final Persistence<GraphChange> persistence;
    private final GraphChangeBuilder<GraphChange> graphChangeBuilder;
    private final GraphBuilder<GraphChange> builder;
    private final GraphViewer viewer;

    public InMemoryGraphController(Persistence<GraphChange> persistence,
                                   GraphChangeBuilder graphChangeBuilder,
                                   GraphBuilder<GraphChange> builder,
                                   GraphViewer viewer) {
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
    public void remove(String graphName) {
        Objects.requireNonNull(graphName);
        persistence.clear(graphName);
    }

    @Override
    public void create(String graphName) {
        Objects.requireNonNull(graphName);
        GraphChange init = graphChangeBuilder.init(graphName);
        persistence.store(graphName, init);
    }

    @Override
    public Set<String> list(String graphName, List<String> location) {
        List<GraphChange> changesByName = persistence.getByName(graphName);
        GraphNode<Node> graph = builder.build(changesByName);
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

    @Override
    public void login(UserPass userPass) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void close() throws Exception {

    }
}
