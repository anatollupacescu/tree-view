package com.ocado.demo.oak;

import com.demo.api.Api;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;

import javax.jcr.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OakGraphController implements Api.GraphController, AutoCloseable {

    private final Api.GraphManager manager;
    private final Api.NodeManager nodeManager;
    private final Session session;

    public OakGraphController(NodeStore ns, SimpleCredentials admin) {
        Repository repo = new Jcr(ns).createRepository();
        try {
            this.session = repo.login(admin);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        manager = new OakGraphManager(session);
        nodeManager = new OakNodeManager(session);
    }

    @Override
    public Set<String> getNames() {
        return manager.getNames();
    }

    @Override
    public void remove(String graphName) {
        Objects.requireNonNull(graphName);
        manager.remove(graphName);
    }

    @Override
    public void create(String graphName) {
        Objects.requireNonNull(graphName);
        manager.create(graphName);
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
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
    public void close() {
        session.logout();
    }
}
