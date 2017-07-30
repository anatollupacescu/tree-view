package com.demo.persistence;

import com.demo.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class ChangeManagerTest {

    public static final String TEST = "test";

    final GraphManager manager = new GraphManager();
    final ChangeManager changeManager = new ChangeManager(manager);

    @Before
    public void setUp() {
        manager.createGraph(TEST);
    }

    @Test
    public void canAddNode() {
        changeManager.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeManager.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        Graph graph = changeManager.build(TEST);
        assertThat(graph, is(notNullValue()));
        assertThat(graph.getChildren().isEmpty(), is(equalTo(false)));
        assertThat(graph.navigate(Collections.singletonList("unu")).getChildren().isEmpty(), is(equalTo(false)));
    }

    @Test
    public void canRemoveNode() {
        changeManager.createNodeAtLocation(TEST, Collections.emptyList(), "unu");
        changeManager.createNodeAtLocation(TEST, Collections.singletonList("unu"), "doi");
        changeManager.removeNodeAtLocation(TEST, Collections.emptyList(), "unu");
        Graph graph = changeManager.build(TEST);
        assertThat(graph, is(notNullValue()));
    }
}