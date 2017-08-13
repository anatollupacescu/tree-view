package com.ocado.demo.oak;

public class NodeNotFoundException extends RuntimeException {

    public NodeNotFoundException(String graphName) {
        super("Node not found: " + graphName);
    }
}
