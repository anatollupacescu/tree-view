package com.ocado.demo.oak;

import com.demo.api.Api;
import com.demo.api.UserPass;

import javax.jcr.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OakGraphController implements Api.GraphController {

    private final Repository repository;

    private Api.GraphManager graphManager;
    private Api.NodeManager nodeManager;
    private Session session;

    public OakGraphController(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Set<String> getNames() {
        return graphManager.getNames();
    }

    @Override
    public void remove(String graphName) {
        Objects.requireNonNull(graphName);
        graphManager.remove(graphName);
    }

    @Override
    public void create(String graphName) {
        Objects.requireNonNull(graphName);
        graphManager.create(graphName);
    }

    @Override
    public Set<String> list(String graphName, List<String> location) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        return nodeManager.list(graphName, location);
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        nodeManager.add(graphName, location, nodeName);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        nodeManager.remove(graphName, location, nodeName);
    }

    @Override
        public void login(UserPass userPass) {
        try {
            SimpleCredentials credentials = new SimpleCredentials(userPass.getUsername(), userPass.getPassword().toCharArray());
            this.session = repository.login(credentials);
            this.graphManager = new OakGraphManager(session);
            this.nodeManager = new OakNodeManager(session);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout() {
        try {
            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        session.logout();
    }

    @Override
    public void close() {
        logout();
    }
}
