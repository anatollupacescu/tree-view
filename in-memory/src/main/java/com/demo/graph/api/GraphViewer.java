package com.demo.graph.api;

import java.util.List;

public interface GraphViewer<T> {
        List<String> list(GraphNode<T> graph, List<String> location);

}
