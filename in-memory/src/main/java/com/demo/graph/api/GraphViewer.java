package com.demo.graph.api;

import java.util.List;
import java.util.Set;

public interface GraphViewer<T> {

    Set<String> list(GraphNode<T> graph, List<String> location);
}
