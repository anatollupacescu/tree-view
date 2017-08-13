package com.demo.api;

import java.util.List;
import java.util.Set;

public class Api {

    public interface GraphNode<T> {

        void addChild(T node);

        void removeChild(T node);

        Set<T> getChildren();

        Set<String> getChildrenNames();

        T getChild(String name);

        T navigate(List<String> location);
    }

    public interface GraphManager {
        Set<String> getNames();

        void remove(String graphName);

        void create(String graphName);
    }

    public interface NodeManager {
        Set<String> list(String graphName, List<String> location);

        void add(String graphName, List<String> location, String nodeName);

        void remove(String graphName, List<String> location, String nodeName);
    }

    public interface SessionManager {
        void login(UserPass userPass);

        void logout();
    }

    public interface GraphController extends GraphManager, NodeManager, SessionManager, AutoCloseable {
    }

    public static class GraphNotFoundException extends RuntimeException {
    }
}
