package com.demo.treeview;

import com.demo.api.Api;
import com.ocado.demo.api.GraphController;
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
    public Api.GraphController graphController(SimpleCredentials admin) {
        return new GraphController(admin);
    }
}
