package com.demo.api;

import com.demo.changelog.GraphChange;
import com.demo.graph.Graph;

import java.util.List;
import java.util.Set;

public class Api {

    public interface Persistence<T> {
        List<T> getByName(String name);
        Set<String> getNames();
        void store(String graph, T change);
    }

    public interface GraphBuilder {
        Graph build(List<GraphChange> changes);
    }

    public interface GraphViewer {
       List<String> list(Graph graph, List<String> location);
    }

    public interface GraphChangeService {
        GraphChange remove(List<String> location, String name);
        GraphChange add(List<String> location, String name);
    }

    public interface GraphController {
        Set<String> getNames();
        List<String> list(String graphName, List<String> location);
        void add(String graphName, List<String> location, String changeData);
        void remove(String graphName, List<String> location, String changeData);
    }

    public static class GraphNotFoundException extends RuntimeException { }
}
