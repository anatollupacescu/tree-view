package com.demo.persistence;

import com.demo.api.Api;
import com.demo.changelog.GraphChange;

import java.util.*;

public class ChangePersistence implements Api.Persistence<GraphChange> {

    private Map<String, List<GraphChange>> changes = new HashMap<>();

    @Override
    public List<GraphChange> getChangesByName(String name) {
        return Optional.ofNullable(changes.get(name)).orElse(Collections.emptyList());
    }

    @Override
    public Set<String> listNames() {
        return new HashSet<>(changes.keySet());
    }

    @Override
    public void storeChange(String graph, GraphChange change) {
        List<GraphChange> graphChanges = changes.get(graph);
        if(graphChanges == null) {
            graphChanges = new ArrayList<>();
            changes.put(graph, graphChanges);
        }
        graphChanges.add(change);
    }
}
