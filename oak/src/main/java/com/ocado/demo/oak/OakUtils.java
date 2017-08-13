package com.ocado.demo.oak;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OakUtils {

    private static final String ISLES = "isles";

    protected List<String> mapChildrenToListOfStrings(Node parent) {
        List<String> titles = new ArrayList<>();
        NodeIterator nodes = getChildNodes(parent);
        nodes.forEachRemaining(addNodeName(titles));
        return titles;
    }

    protected Node getOrCreateIslesNode(Session session) {
        try {
            return getRootNode(session, ISLES);
        } catch (RuntimeException e) {
            createNodeInRoot(session, ISLES);
            return getRootNode(session, ISLES);
        }
    }

    private NodeIterator getChildNodes(Node parent) {
        try {
            return parent.getNodes();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Consumer addNodeName(List<String> titles) {
        return node -> {
            try {
                String title = ((Node) node).getName();
                titles.add(title);
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void createNodeInRoot(Session session, String isles) {
        Node root = getRoot(session);
        try {
            root.addNode(isles);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getRootNode(Session session, String graphName) {
        Node root = getRoot(session);
        try {
            return root.getNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getRoot(Session session) {
        try {
            return session.getRootNode();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
