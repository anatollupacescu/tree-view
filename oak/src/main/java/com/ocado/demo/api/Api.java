package com.ocado.demo.api;

import lombok.Value;

import java.util.Set;

public class Api {

    public interface ResourceManager<T> {

        void store(T content);

        void remove(String identifier);

        T get(String identifier);

        Set<T> getAll();
    }

    @Value
    public static class Resource {
        private String identifier;
        private byte[] content;
        private String mimeType;
    }
}
