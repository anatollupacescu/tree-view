package com.demo.treeview;

import com.demo.api.Api;
import com.demo.changelog.GraphBuilder;
import com.demo.controller.ChangeService;
import com.demo.controller.GraphController;
import com.demo.controller.GraphViewer;
import com.demo.persistence.ChangePersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TreeViewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeViewDemoApplication.class, args);
    }

    @Bean
    public GraphController graphController() {
        ChangePersistence persistence = new ChangePersistence();
        ChangeService changeService = new ChangeService();
        GraphBuilder graphBuilder = new GraphBuilder();
        Api.GraphViewer viewer = new GraphViewer();
        return new GraphController(persistence, changeService, graphBuilder, viewer);
    }
}
