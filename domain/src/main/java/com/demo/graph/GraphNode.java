package com.demo.graph;

import java.util.List;
import java.util.Optional;

public interface GraphNode {

    void addChild(Node node);

    void removeChild(Node node);

    List<Node> getChildren();

    Optional<Node> findChildByName(String name);

    Node navigate(List<String> location);

    default int getChildrenCount() {
        return getChildren().size();
    }
}
