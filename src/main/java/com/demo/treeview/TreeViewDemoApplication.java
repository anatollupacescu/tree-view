package com.demo.treeview;

import com.demo.changelog.GraphBuilder;
import com.demo.controller.ChangeService;
import com.demo.controller.GraphService;
import com.demo.controller.GraphValidator;
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
    public GraphService graphController() {
        ChangePersistence persistence = new ChangePersistence();
        GraphValidator validator = new GraphValidator();
        ChangeService changeService = new ChangeService(persistence, validator);
        GraphBuilder graphBuilder = new GraphBuilder();
        return new GraphService(graphBuilder, changeService);
    }
}
