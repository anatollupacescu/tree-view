package com.demo;

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

    private final ParameterizedTypeReference<List<String>> listTypeReference = new ParameterizedTypeReference<List<String>>() {
        public Type getType() {
            return super.getType();
        }
    };

    @Override
    public Set<String> getNames() {
        ResponseEntity<List<String>> response = client.exchange(baseUrl, HttpMethod.GET, null, listTypeReference);
        return new HashSet<>(response.getBody());
    }

    @Override
    public void remove(String graphName) {
        client.exchange(baseUrl + "/delete/" + graphName, HttpMethod.DELETE, null, Void.class);
    }

    @Override
    public void create(String graphName) {
        String url = baseUrl + "/create/" + graphName;
        client.exchange(url, HttpMethod.PUT, null, String.class);
    }

    @Override
    public List<String> list(String graphName, List<String> location) {
        HttpEntity<List<String>> request = new HttpEntity<>(location);
        String url = baseUrl + "/list/" + graphName;
        ResponseEntity<List<String>> response =client.exchange(url, HttpMethod.POST, request, listTypeReference);
        return response.getBody();
    }

    @Override
    public void add(String graphName, List<String> location, String nodeName) {
        HttpEntity<List<String>> request = new HttpEntity<>(location);
        String url = String.format("%s/create/%s/%s", baseUrl, graphName, nodeName);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, request, String.class);
        checkStatus(response);
    }

    @Override
    public void remove(String graphName, List<String> location, String nodeName) {
        HttpEntity<List<String>> request = new HttpEntity<>(location);
        String url = String.format("%s/delete/%s/%s", baseUrl, graphName, nodeName);
        ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, request, String.class);
        checkStatus(response);
    }

    private void checkStatus(ResponseEntity<String> response) {
        if (response.getStatusCodeValue() > 300) {
            throw new IllegalStateException("Could not delete");
        }
    }

}
