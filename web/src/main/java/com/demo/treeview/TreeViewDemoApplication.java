package com.demo.treeview;

import com.demo.api.Api;
import com.demo.api.UserPass;
import com.demo.changelog.InMemoryGraphBuilder;
import com.demo.changelog.InMemoryGraphChangeBuilder;
import com.demo.controller.InMemoryGraphController;
import com.demo.controller.NodeGraphViewer;
import com.demo.graph.api.GraphChangeBuilder;
import com.demo.graph.api.GraphViewer;
import com.demo.persistence.InMemoryChangePersistence;
import com.ocado.demo.oak.OakGraphController;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.jcr.Repository;
import java.util.function.Function;

@SpringBootApplication
public class TreeViewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeViewDemoApplication.class, args);
    }

    @Bean
    public UserPass defaultUserPass() {
        return new UserPass("admin", "admin");
    }

    @Configuration
    @Profile("oak")
    class OakConfiguration {

        @Bean
        public NodeStore nodeStore() {
            return new MemoryNodeStore();
        }

        @Bean
        public Repository repository() {
            Jcr jcr = new Jcr(nodeStore());
            return jcr.createRepository();
        }

        @Bean
        public Function<UserPass, Api.GraphController> controllerFactory() {
            return userPass -> {
                OakGraphController controller = new OakGraphController(repository());
                controller.login(defaultUserPass());
                return controller;
            };
        }
    }

    @Configuration
    @Profile("default")
    class InMemoryConfiguration {

        @Bean
        public Function<UserPass, Api.GraphController> controllerFactory() {
            return userPass -> graphControllerInMemory();
        }

        @Bean
        public Api.GraphController graphControllerInMemory() {
            InMemoryChangePersistence persistence = new InMemoryChangePersistence();
            InMemoryGraphBuilder inMemoryGraphBuilder = new InMemoryGraphBuilder();
            GraphViewer viewer = new NodeGraphViewer();
            GraphChangeBuilder changeService = new InMemoryGraphChangeBuilder();
            return new InMemoryGraphController(persistence, changeService, inMemoryGraphBuilder, viewer);
        }
    }
}
