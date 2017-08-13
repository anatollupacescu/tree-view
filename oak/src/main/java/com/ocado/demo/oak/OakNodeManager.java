package com.ocado.demo.oak;

import com.demo.api.Api;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;

public class OakNodeManager extends OakUtils implements Api.NodeManager {

    private final Session session;

    public OakNodeManager(Session session) {
        this.session = session;
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        Node isle = getIsle(session, graphName);
        Node children = navigateToLocation(isle, location);
        return mapChildrenToListOfStrings(children);
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        Node isle = getIsle(session, graphName);
        Node parent = navigateToLocation(isle, location);
        addChild(parent, nodeName);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        Node isle = getIsle(session, graphName);
        Node parent = navigateToLocation(isle, location);
        removeChild(parent, nodeName);
    }

    private void addChild(Node parent, String nodeName) {
        try {
            parent.addNode(nodeName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeChild(Node parent, String nodeName) {
        try {
            parent.getNode(nodeName).remove();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getIsle(Session session, String name) {
        Node rootNode = getOrCreateIslesNode(session);
        return getChildNode(rootNode, name);
    }

    private Node navigateToLocation(Node rootNode, List<String> location) {
        if (location.isEmpty()) return rootNode;
        for (String node : location) {
            rootNode = getChildNode(rootNode, node);
        }
        return rootNode;
    }

    private Node getChildNode(Node rootNode, String graphName) {
        try {
            return rootNode.getNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
