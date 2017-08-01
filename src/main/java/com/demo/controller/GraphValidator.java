package com.demo.controller;

import com.demo.changelog.GraphBuilder;
import com.demo.changelog.GraphChange;

import java.util.ArrayList;
import java.util.List;

public class GraphValidator {

    public void validate(List<GraphChange> changeList, GraphChange change) {
        List<GraphChange> changeListToValidate = safeCreateCopy(changeList);
        changeListToValidate.add(change);
        new GraphBuilder().build("validator", changeListToValidate);
    }

    private List<GraphChange> safeCreateCopy(List<GraphChange> changeList) {
        if (changeList == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(changeList);
        }
    }
}
