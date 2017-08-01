package com.demo.controller;

import com.demo.changelog.ChangeData;
import com.demo.changelog.ChangeDataParam;
import com.demo.changelog.ChangeType;
import com.demo.changelog.GraphChange;
import com.demo.persistence.ChangePersistence;

import java.util.*;

public class ChangeService {

    private final ChangePersistence persistence;
    private final GraphValidator validator;

    public ChangeService(ChangePersistence persistence, GraphValidator validator) {
        this.persistence = persistence;
        this.validator = validator;
    }

    public void createNodeAtLocation(String graph, List<String> location, String title) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(title);
        ChangeData data = buildNameParentData(title, location);
        GraphChange change = new GraphChange(ChangeType.ADD_CHILD, data);
        saveValidChange(graph, change);
    }

    public void removeNodeAtLocation(String graph, List<String> location, String name) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = buildNameParentData(name, location);
        GraphChange change = new GraphChange(ChangeType.REMOVE_CHILD, data);
        saveValidChange(graph, change);
    }

    private ChangeData buildNameParentData(String title, List<String> location) {
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.NAME, title);
        data.put(ChangeDataParam.PARENT, location);
        return data;
    }

    private void saveValidChange(String graph, GraphChange change) {
        validateChange(graph, change);
        persistence.add(graph, change);
    }

    private void validateChange(String name, GraphChange change) {
        List<GraphChange> changeList = persistence.get(name);
        validator.validate(changeList, change);
    }

    public List<GraphChange> getChangesOrFail(String graph) {
        List<GraphChange> changes = persistence.get(graph);
        if (changes == null) {
            throw new GraphNotFoundException(graph);
        }
        return new ArrayList<>(changes);
    }

    public Set<String> getNames() {
        return new HashSet<>(persistence.keySet());
    }

    public class GraphNotFoundException extends RuntimeException {
        public GraphNotFoundException(String graph) {
            super(String.format("No graph with name '%s' declared", graph));
        }
    }
}
