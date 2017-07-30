package com.demo.controller;

import com.demo.changelog.*;
import com.demo.graph.Graph;
import com.demo.persistence.ChangePersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChangeService {

    private final ChangePersistence persistence;

    public ChangeService(ChangePersistence persistence) {
        this.persistence = persistence;
    }

    public void createNodeAtLocation(String graph, List<String> location, String title) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(title);
        ChangeData data = buildNameParentData(title, location);
        GraphChange change = new GraphChange(ChangeType.ADD_CHILD, data);
        saveValidChange(graph, change);
    }

    private ChangeData buildNameParentData(String title, List<String> location) {
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.NAME, title);
        data.put(ChangeDataParam.PARENT, location);
        return data;
    }

    public void removeNodeAtLocation(String graph, List<String> location, String name) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = buildNameParentData(name, location);
        GraphChange change = new GraphChange(ChangeType.REMOVE_CHILD, data);
        saveValidChange(graph, change);
    }

    public Graph build(String graph) {
        List<GraphChange> changes = getChangesOrFail(graph);
        return new GraphBuilder(graph).build(changes);
    }

    private void saveValidChange(String graph, GraphChange change) {
        validateChange(graph, change);
        persistence.add(graph, change);
    }

    private void validateChange(String name, GraphChange change) {
        List<GraphChange> changeList = persistence.get(name);
        List<GraphChange> changeListToValidate = safeCreateCopy(changeList);
        changeListToValidate.add(change);
        new GraphBuilder(name).build(changeListToValidate);
    }

    private List<GraphChange> safeCreateCopy(List<GraphChange> changeList) {
        if(changeList == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(changeList);
        }
    }

    private List<GraphChange> getChangesOrFail(String graph) {
        List<GraphChange> changes = persistence.get(graph);
        if(changes == null) {
            throw new GraphNotFoundException(graph);
        }
        return new ArrayList<>(changes);
    }

    public Set<String> getNames() {
        return persistence.keySet();
    }

    public class GraphNotFoundException extends RuntimeException {
        public GraphNotFoundException(String graph) {
            super(String.format("No graph with name '%s' declared", graph));
        }
    }
}
