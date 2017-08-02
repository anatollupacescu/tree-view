package com.demo.controller;

import com.demo.api.Api;
import com.demo.changelog.ChangeData;
import com.demo.changelog.ChangeDataParam;
import com.demo.changelog.ChangeType;
import com.demo.changelog.GraphChange;

import java.util.*;

public class ChangeService implements Api.GraphChangeService{

    private GraphChange createNodeAtLocation(List<String> location, String title) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(title);
        ChangeData data = buildNameParentData(title, location);
        return new GraphChange(ChangeType.ADD_CHILD, data);
    }

    private GraphChange removeNodeAtLocation(List<String> location, String name) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(name);
        ChangeData data = buildNameParentData(name, location);
        return new GraphChange(ChangeType.REMOVE_CHILD, data);
    }

    private ChangeData buildNameParentData(String title, List<String> location) {
        ChangeData data = new ChangeData();
        data.put(ChangeDataParam.NAME, title);
        data.put(ChangeDataParam.PARENT, location);
        return data;
    }

    @Override
    public GraphChange remove(List<String> location, String name) {
        return removeNodeAtLocation(location, name);
    }

    @Override
    public GraphChange add(List<String> location, String name) {
        return createNodeAtLocation(location, name);
    }
}
