package com.demo.persistence;

import com.demo.changelog.GraphChange;
import com.demo.controller.ChangeService;
import com.demo.controller.GraphValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ChangeServiceTest {

    public static final String TEST = "test";

    private ChangeService changeService;

    @Before
    public void setUp() {
        ChangePersistence persistence = new ChangePersistence();
        GraphValidator validator = new GraphValidator();
        changeService = new ChangeService(persistence, validator);
    }

    @Test
    public void canAddNode() {
        changeService.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeService.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        List<GraphChange> updatedGraph = changeService.getChangesOrFail(TEST);
        assertThat(updatedGraph, is(notNullValue()));
        assertThat(updatedGraph.size(), is(equalTo(2)));
    }

    @Test
    public void canRemoveNode() {
        changeService.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeService.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        changeService.removeNodeAtLocation(TEST, Collections.emptyList(), "unu");
        List<GraphChange> updatedGraph = changeService.getChangesOrFail(TEST);
        assertThat(updatedGraph, is(notNullValue()));
        assertThat(updatedGraph.size(), is(equalTo(3)));
    }
}