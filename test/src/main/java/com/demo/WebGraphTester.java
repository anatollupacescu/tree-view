package com.demo;

import lombok.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.*;

public class WebGraphTester extends GraphTester {

    private final RestTemplate client = new RestTemplate();
    private final String baseUrl = "http://localhost:8080/graph";
    private final String graphName;

    public WebGraphTester(String graphName) {
        this.graphName = graphName;
    }

    @Override
    public int getChildrenCount() {
        return list(graphName, Collections.emptyList()).size();
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<>(Arrays.asList(graphName));
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        ResponseEntity<List<String>> response = doPostForList("/list/", GraphRequest.of(null, location));
        return response.getBody();
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        ResponseEntity<String> response = doPostForString("/create/", GraphRequest.of(nodeName, location));
        checkStatus(response);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        ResponseEntity<String> response = doPostForString("/delete/", GraphRequest.of(nodeName, location));
        checkStatus(response);
    }

    private void checkStatus(ResponseEntity<String> response) {
        if (response.getStatusCodeValue() > 300) {
            throw new IllegalStateException("Could not delete");
        }
    }

    private ResponseEntity<String> doPostForString(String path, GraphRequest graphRequest) {
        HttpEntity<GraphRequest> request = new HttpEntity<>(graphRequest);
        return client.exchange(baseUrl + path + graphName, HttpMethod.POST, request, String.class);
    }

    private ResponseEntity<List<String>> doPostForList(String path, GraphRequest graphRequest) {
        HttpEntity<GraphRequest> request = new HttpEntity<>(graphRequest);
        ParameterizedTypeReference<List<String>> listTypeReference = new ParameterizedTypeReference<List<String>>() {
            @Override
            public Type getType() {
                return super.getType();
            }
        };
        return client.exchange(baseUrl + path + graphName, HttpMethod.POST, request, listTypeReference);
    }

    @Value(staticConstructor = "of")
    public static class GraphRequest {
        private String childName;
        private List<String> location;
    }
}
