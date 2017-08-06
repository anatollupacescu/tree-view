package com.demo.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Node implements GraphNode {

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
    public List<Node> getChildren() {
        return new ArrayList<>(children);
    }

    public String getName() {
        return name;
    }

    private Optional<Node> findChild(Predicate<Node> pred) {
        return children.stream().filter(pred).findAny();
    }

    @Override
    public Optional<Node> findChildByName(String name) {
        return findChild(byName(name));
    }

    public Node findChildByNameOrThrow(String name) {
        return findChildByName(name).orElseThrow(() -> new IllegalArgumentException());
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
