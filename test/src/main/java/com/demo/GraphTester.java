package com.demo;

import com.demo.api.Api;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class GraphTester implements Api.GraphController {

    public List<String> toList(String location) {
        String[] split = location.split("/");
        return Arrays.stream(split).filter(isNotRootOrEmpty()).collect(Collectors.toList());
    }

    private Predicate<String> isNotRootOrEmpty() {
        return s -> !("/".equals(s) || "".equals(s));
    }
}
