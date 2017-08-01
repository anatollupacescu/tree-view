package com.demo.persistence;

import com.demo.api.Api;
import com.demo.changelog.GraphChange;
import com.demo.controller.ChangeService;
import com.demo.graph.Graph;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ChangePersistence extends LinkedMultiValueMap<String, GraphChange> implements Api.Persistence<GraphChange> {

    @Override
    public List<GraphChange> getChangesByName(String name) {
        return Optional.ofNullable(super.get(name)).orElse(Collections.emptyList());
    }

    @Override
    public Set<String> listNames() {
        return super.keySet();
    }

    @Override
    public void storeChange(String graph, GraphChange change) {
        super.add(graph, change);
    }
}
