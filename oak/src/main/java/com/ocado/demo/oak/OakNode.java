package com.ocado.demo.oak;

import com.demo.api.Api;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OakNode implements Api.GraphNode<OakNode> {

    private Node delegate;
    private String name;

    private OakNode(Node delegate) {
        this.delegate = delegate;
    }

    private OakNode(String name) {
        this.name = name;
    }

    @Override
    public void addChild(OakNode node) {
        try {
            delegate.addNode(node.getName());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeChild(OakNode node) {
        try {
            delegate.getNode(node.getName()).remove();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<OakNode> getChildren() {
        Set<OakNode> nodes = new HashSet<>();
        try {
            NodeIterator iterator = delegate.getNodes();
            iterator.forEachRemaining(addToCollection(nodes));
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        return nodes;
    }

    private Consumer<Node> addToCollection(Set<OakNode> nodes) {
        return node -> {
            OakNode e = withDelegate(node);
            nodes.add(e);
        };
    }

    @Override
    public Set<String> getChildrenNames() {
        Set<OakNode> childNodes = getChildren();
        return childNodes.stream().map(OakNode::getName).collect(Collectors.toSet());
    }

    @Override
    public OakNode getChild(String name) {
        Node childNode = getChildNode(delegate, name);
        return withDelegate(childNode);
    }

    @Override
    public OakNode navigate(List<String> location) {
        if (location.isEmpty()) return this;
        Node rootNode = delegate;
        for (String node : location) {
            rootNode = getChildNode(rootNode, node);
        }
        return withDelegate(rootNode);
    }

    private Node getChildNode(Node rootNode, String graphName) {
        try {
            return rootNode.getNode(graphName);
        } catch (RepositoryException e) {
            throw new NodeNotFoundException(graphName);
        }
    }

    public String getName() {
        return Optional.ofNullable(delegate).map(this::getNodeName).orElse(name);
    }

    private String getNodeName(Node node) {
        try {
            return node.getName();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    static OakNode withName(String isles) {
        return new OakNode(isles);
    }

    static OakNode withDelegate(Node rootNode) {
        return new OakNode(rootNode);
    }
}
