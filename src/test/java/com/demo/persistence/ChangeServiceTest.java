package com.demo.persistence;

import com.demo.controller.ChangeService;
import com.demo.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class ChangeServiceTest {

    public static final String TEST = "test";

    private ChangeService changeService;

    @Before
    public void setUp() {
        ChangePersistence persistence = new ChangePersistence();
        changeService = new ChangeService(persistence);
    }

    @Test
    public void canAddNode() {
        changeService.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeService.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        Graph updatedGraph = changeService.build(TEST);
        assertThat(updatedGraph, is(notNullValue()));
        assertThat(updatedGraph.getChildren().isEmpty(), is(equalTo(false)));
        assertThat(updatedGraph.navigate(Collections.singletonList("unu")).getChildren().isEmpty(), is(equalTo(false)));
    }

    @Test
    public void canRemoveNode() {
        changeService.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeService.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        changeService.removeNodeAtLocation(TEST, Collections.emptyList(), "unu");
        Graph updatedGraph = changeService.build(TEST);
        assertThat(updatedGraph, is(notNullValue()));
        assertThat(updatedGraph.getChildren().size(), is(equalTo(0)));
    }
}