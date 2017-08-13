package com.demo.graph.api;

import java.util.List;

public interface GraphChangeBuilder<T> {
        T init(String graphName);
        T remove(List<String> location, String name);
        T add(List<String> location, String name);
}
