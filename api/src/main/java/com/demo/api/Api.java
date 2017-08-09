package com.demo.api;

import java.util.List;
import java.util.Set;

public class Api {

    public interface Persistence<T> {
        List<T> getByName(String name);

        Set<String> getNames();

        void store(String graph, T change);
    }

    public interface GraphBuilder<T> {
        GraphNode build(List<T> changes);
    }

    public interface GraphViewer<T> {
        List<String> list(GraphNode<T> graph, List<String> location);
    }

    public interface GraphChangeBuilder<T> {
        T remove(List<String> location, String name);

        T add(List<String> location, String name);
    }

    public interface GraphController {
        Set<String> getNames();

        List<String> list(String graphName, List<String> location);

        void add(String graphName, List<String> location, String nodeName);

        void remove(String graphName, List<String> location, String nodeName);
    }

    public static class GraphNotFoundException extends RuntimeException {
    }
}
