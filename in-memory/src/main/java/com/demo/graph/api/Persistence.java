package com.demo.graph.api;

import java.util.List;
import java.util.Set;

public interface Persistence<T> {
        List<T> getByName(String name);
        Set<String> getNames();
        void store(String graph, T change);
        void clear(String graph);

}
