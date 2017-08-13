package com.demo.api;

import java.util.List;
import java.util.Set;

public class Api {

    public interface GraphManager {
        Set<String> getNames();

        void remove(String graphName);

        void create(String graphName);
    }

    public interface NodeManager {
        List<String> list(String graphName, List<String> location);

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
