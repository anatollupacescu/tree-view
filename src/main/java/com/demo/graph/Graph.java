package com.demo.graph;

import java.util.Objects;

public class Graph extends Node {

    private Graph(String name) {
        super(name, null);
    }

    public static Graph withName(String name) {
        Objects.requireNonNull(name);
        return new Graph(name);
    }

    @Override
    public Node getParent() {
        return null;
    }
}
