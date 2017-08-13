package com.ocado.demo.api;

import com.demo.api.Api;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;

import javax.jcr.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GraphController implements Api.GraphController, AutoCloseable {

    private static final String ISLES = "isles";

    private final Session session;
    private final NodeStore ns = new MemoryNodeStore();

    public GraphController(SimpleCredentials admin) {
        Repository repo = new Jcr(ns).createRepository();
        try {
            this.session = repo.login(admin);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getNames() {
        Node islesNode = getOrCreateIslesNode();
        List<String> names = mapChildrenToListOfStrings(islesNode);
        return new HashSet<>(names);
    }

    @Override
    public void remove(String graphName) {
        Node root = getOrCreateIslesNode();
        try {
            root.getNode(graphName).remove();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String graphName) {
        Node root = getOrCreateIslesNode();
        try {
            root.addNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        Node isle = getIsle(graphName);
        Node children = navigateToLocation(isle, location);
        return mapChildrenToListOfStrings(children);
    }

    private Node getIsle(String name) {
        Node rootNode = getOrCreateIslesNode();
        return getChildNode(rootNode, name);
    }

    private Node getChildNode(Node rootNode, String graphName) {
        try {
            return rootNode.getNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> mapChildrenToListOfStrings(Node parent) {
        List<String> titles = new ArrayList<>();
        NodeIterator nodes = getChildNodes(parent);
        nodes.forEachRemaining(addNodeName(titles));
        return titles;
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

    private Node navigateToLocation(Node rootNode, List<String> location) {
        if (location.isEmpty()) return rootNode;
        for (String node : location) {
            rootNode = getChildNode(rootNode, node);
        }
        return rootNode;
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        Node isle = getIsle(graphName);
        Node parent = navigateToLocation(isle, location);
        addChild(parent, nodeName);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        Node isle = getIsle(graphName);
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

    private Node getOrCreateIslesNode() {
        try {
            return getRootNode(ISLES);
        } catch (RuntimeException e) {
            createNodeInRoot(ISLES);
            return getRootNode(ISLES);
        }
    }

    private void createNodeInRoot(String isles) {
        Node root = getRoot();
        try {
            root.addNode(isles);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getRootNode(String graphName) {
        Node root = getRoot();
        try {
            return root.getNode(graphName);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Node getRoot() {
        try {
            return session.getRootNode();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        session.logout();
    }
}
