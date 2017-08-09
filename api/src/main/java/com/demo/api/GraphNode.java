package com.demo.api;

import java.util.List;
import java.util.Optional;

public interface GraphNode<T> {

    void addChild(T node);

    void removeChild(T node);

    List<T> getChildren();

    Optional<T> findChildByName(String name);

    T navigate(List<String> location);

    default int getChildrenCount() {
        return getChildren().size();
    }
}
