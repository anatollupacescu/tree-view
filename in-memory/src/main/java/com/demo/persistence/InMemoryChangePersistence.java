package com.demo.persistence;

import com.demo.changelog.GraphChange;
import com.demo.graph.Graph;
import com.demo.graph.api.Persistence;

import java.util.*;

public class InMemoryChangePersistence implements Persistence<GraphChange> {

    private final Map<String, List<GraphChange>> changes = new HashMap<>();

    @Override
    public List<GraphChange> getByName(String name) {
        return getByKey(name);
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<>(changes.keySet());
    }

    @Override
    public void store(String graph, GraphChange change) {
        getByKey(graph).add(change);
    }

    private List<GraphChange> getByKey(String key) {
        return changes.computeIfAbsent(key, k -> new ArrayList<>());
    }

    @Override
    public void clear(String graph) {
        changes.remove(graph);
    }
}
