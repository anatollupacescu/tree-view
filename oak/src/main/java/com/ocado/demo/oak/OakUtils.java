package com.ocado.demo.oak;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class OakUtils {

    private static final String ISLES = "isles";

    protected OakNode getOrCreateIslesNode(Session session) {
        try {
            return getRootNode(session, ISLES);
        } catch (NodeNotFoundException e) {
            createNodeInRoot(session, ISLES);
            return getRootNode(session, ISLES);
        }
    }

    private void createNodeInRoot(Session session, String isles) {
        OakNode root = getRoot(session);
        root.addChild(OakNode.withName(isles));
    }

    private OakNode getRootNode(Session session, String graphName) {
        OakNode root = getRoot(session);
        return root.getChild(graphName);
    }

    private OakNode getRoot(Session session) {
        try {
            Node node = session.getRootNode();
            return new OakNode(node);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
