package com.demo.graph.api;

import java.util.List;

public interface GraphBuilder<T> {
        GraphNode build(List<T> changes);

}
