package com.demo.treeview;

import com.demo.api.Api;
import com.demo.changelog.InMemoryGraphBuilder;
import com.demo.changelog.InMemoryGraphChangeBuilder;
import com.demo.controller.InMemoryGraphController;
import com.demo.controller.NodeGraphViewer;
import com.demo.graph.api.GraphChangeBuilder;
import com.demo.graph.api.GraphViewer;
import com.demo.persistence.ChangePersistence;
import com.ocado.demo.oak.OakGraphController;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.jcr.SimpleCredentials;

@SpringBootApplication
public class TreeViewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeViewDemoApplication.class, args);
    }

    @Bean
    public SimpleCredentials admin() {
        return new SimpleCredentials("admin", "admin".toCharArray());
    }

    @Bean
    public NodeStore nodeStore() {
        return new MemoryNodeStore();
    }

    @Bean
    public Api.GraphController graphControllerOak(NodeStore nodeStore, SimpleCredentials admin) {
        return new OakGraphController(nodeStore, admin);
    }

    @Bean
    public Api.GraphController graphController() {
        ChangePersistence persistence = new ChangePersistence();
        InMemoryGraphBuilder inMemoryGraphBuilder = new InMemoryGraphBuilder();
        GraphViewer viewer = new NodeGraphViewer();
        GraphChangeBuilder changeService = new InMemoryGraphChangeBuilder();
        return new InMemoryGraphController(persistence, changeService, inMemoryGraphBuilder, viewer);
    }
}
