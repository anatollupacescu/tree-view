package com.demo.graph;

import com.demo.api.Api;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Node implements Api.GraphNode<Node> {

    private final String name;
    private final Node parent;
    private final List<Node> children = new ArrayList<>();

    Node(String name, Node parent) {
        this.name = name;
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void addChild(String name) {
        Node node = new Node(name, this);
        addChild(node);
    }

    @Override
    public void addChild(Node node) {
        Objects.requireNonNull(node);
        if (children.contains(node)) {
            throw new NodeNotFoundException();
        }
        children.add(node);
    }

    @Override
    public void removeChild(Node node) {
        Objects.requireNonNull(node);
        if (!children.remove(node)) {
            throw new NodeNotFoundException();
        }
    }

    public void removeChild(String name) {
        Node node = new Node(name, this);
        removeChild(node);
    }

    @Override
    public Set<Node> getChildren() {
        return new HashSet<>(children);
    }

    @Override
    public Set<String> getChildrenNames() {
        return children.stream().map(Node::getName).collect(Collectors.toSet());
    }

    @Override
    public Node getChild(String name) {
        return findChildByNameOrThrow(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public Node navigate(List<String> location) {
        Objects.requireNonNull(location);
        Node destination = this;
        if (location.isEmpty()) return destination;
        for (String name : location) {
            destination = destination.findChild(byName(name))
                    .orElseThrow(NodeNotFoundException::new);
        }
        return destination;
    }

    public static class NodeNotFoundException extends RuntimeException {

        public NodeNotFoundException() {
            super("Node not found");
        }
    }
    private Optional<Node> findChildByName(String name) {
        return findChild(byName(name));
    }

    private Optional<Node> findChild(Predicate<Node> pred) {
        return children.stream().filter(pred).findAny();
    }

    private Node findChildByNameOrThrow(String name) {
        return findChildByName(name).orElseThrow(IllegalArgumentException::new);
    }

    private Predicate<Node> byName(String name) {
        if (name == null) return (node -> false);
        return (node -> name.equalsIgnoreCase(node.getName()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name) &&
                Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parent);
    }
}
