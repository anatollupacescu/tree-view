package com.ocado.demo.oak;

import com.demo.api.Api;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OakGraphManager extends OakUtils implements Api.GraphManager {

    private final Session session;

    public OakGraphManager(Session session) {
        this.session = session;
    }

    @Override
    public Set<String> getNames() {
        Node islesNode = getOrCreateIslesNode(session);
        List<String> names = mapChildrenToListOfStrings(islesNode);
        return new HashSet<>(names);
    }

    @Override
    public void remove(String graphName) {
        Node root = getOrCreateIslesNode(session);
        try {
            root.getNode(graphName).remove();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String graphName) {
        Node root = getOrCreateIslesNode(session);
        try {
            root.addNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
