package com.demo.graph.api;

import com.demo.api.Api;

import java.util.List;

public interface GraphBuilder<T> {

        Api.GraphNode build(List<T> changes);
}
