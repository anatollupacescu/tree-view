package com.ocado.demo.oak;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class OakUtils {

    private static final String ISLES = "isles";

    protected OakNode getOrCreateIslesNode(Session session) {
        try {
            return getIslesNode(session);
        } catch (NodeNotFoundException e) {
            createIslesNode(session);
            return getIslesNode(session);
        }
    }

    private void createIslesNode(Session session) {
        OakNode root = getRoot(session);
        root.addChild(OakNode.withName(ISLES));
    }

    private OakNode getIslesNode(Session session) {
        OakNode root = getRoot(session);
        return root.getChild(ISLES);
    }

    private OakNode getRoot(Session session) {
        try {
            Node node = session.getRootNode();
            return OakNode.withDelegate(node);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
