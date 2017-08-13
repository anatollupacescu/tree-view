package com.ocado.demo.oak;

import com.demo.api.Api;

import javax.jcr.Session;
import java.util.List;
import java.util.Set;

public class OakNodeManager extends OakUtils implements Api.NodeManager {

    private final Session session;

    public OakNodeManager(Session session) {
        this.session = session;
    }

    @Override
    public Set<String> list(String graphName, List<String> location) {
        OakNode isle = getIsle(session, graphName);
        OakNode destination = isle.navigate(location);
        return destination.getChildrenNames();
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        OakNode isle = getIsle(session, graphName);
        isle.navigate(location).addChild(OakNode.withName(nodeName));
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        OakNode isle = getIsle(session, graphName);
        isle.navigate(location).removeChild(OakNode.withName(nodeName));
    }

    private OakNode getIsle(Session session, String name) {
        OakNode rootNode = getOrCreateIslesNode(session);
        return rootNode.getChild(name);
    }
}
