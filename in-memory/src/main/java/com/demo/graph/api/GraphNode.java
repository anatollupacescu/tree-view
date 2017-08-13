package com.demo.graph.api;

import java.util.List;
import java.util.Optional;

public interface GraphNode<T> {

    void addChild(T node);

    void removeChild(T node);

    List<T> getChildren();

    Optional<T> findChildByName(String name);

    T navigate(List<String> location);
}
