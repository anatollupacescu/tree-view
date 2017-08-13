package com.demo.persistence;

import com.demo.changelog.GraphChange;
import com.demo.graph.api.Persistence;

import java.util.*;

public class InMemoryChangePersistence implements Persistence<GraphChange> {

    private final Map<String, List<GraphChange>> changes = new HashMap<>();

    @Override
    public List<GraphChange> getByName(String name) {
        return Optional.ofNullable(changes.get(name)).orElse(Collections.emptyList());
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<>(changes.keySet());
    }

    @Override
    public void store(String graph, GraphChange change) {
        List<GraphChange> graphChanges = changes.computeIfAbsent(graph, k -> new ArrayList<>());
        graphChanges.add(change);
    }

    @Override
    public void clear(String graph) {
        changes.remove(graph);
    }
}
