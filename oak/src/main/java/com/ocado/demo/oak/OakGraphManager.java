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
        OakNode islesNode = getOrCreateIslesNode(session);
        return islesNode.getChildrenNames();
    }

    @Override
    public void remove(String graphName) {
        OakNode root = getOrCreateIslesNode(session);
        root.removeChild(OakNode.withName(graphName));
    }

    @Override
    public void create(String graphName) {
        OakNode root = getOrCreateIslesNode(session);
        root.addChild(OakNode.withName(graphName));
    }
}
