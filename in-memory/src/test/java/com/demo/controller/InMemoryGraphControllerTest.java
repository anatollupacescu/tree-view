package com.demo.controller;

import com.demo.api.Api;
import com.demo.changelog.InMemoryGraphBuilder;
import com.demo.changelog.InMemoryGraphChangeBuilder;
import com.demo.graph.Node;
import com.demo.graph.api.GraphChangeBuilder;
import com.demo.graph.api.GraphViewer;
import com.demo.persistence.ChangePersistence;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class InMemoryGraphControllerTest {

    private static final String GRAPH_NAME = "graph1";
    private static final String NODE_NAME1 = "node1";
    private static final String NODE_NAME2 = "node2";

    public InMemoryGraphController createDefault() {
        ChangePersistence persistence = new ChangePersistence();
        InMemoryGraphBuilder inMemoryGraphBuilder = new InMemoryGraphBuilder();
        GraphViewer viewer = new NodeGraphViewer();
        GraphChangeBuilder changeService = new InMemoryGraphChangeBuilder();
        return new InMemoryGraphController(persistence, changeService, inMemoryGraphBuilder, viewer);
    }

    @Test
    public void createDefaultCreatesInstance() {
        InMemoryGraphController service = createDefault();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void isInitializedEmpty() {
        InMemoryGraphController service = createDefault();
        assertThat(service.getNames().isEmpty(), is(equalTo(true)));
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void canNotReadNonExistentGraph() {
        InMemoryGraphController service = createDefault();
        service.list(GRAPH_NAME, Collections.singletonList(NODE_NAME1));
    }

    @Test
    public void canNotListRootNodesOfNonExistentGraph() {
        InMemoryGraphController service = createDefault();
        List<String> list = service.list("noGo", Collections.emptyList());
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void listNodesAtLocation() {
        InMemoryGraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        List<String> nodes = service.list(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
    }

    @Test
    public void createNodeAtLocation() {
        InMemoryGraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        service.add(GRAPH_NAME, Collections.singletonList(NODE_NAME1), NODE_NAME2);
        List<String> nodes = service.list(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
        nodes = service.list(GRAPH_NAME, Collections.singletonList(NODE_NAME1));
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
    }

    @Test(expected = Api.GraphNotFoundException.class)
    public void removeNodeAsFirstCommandFails() {
        List<String> location = Collections.singletonList(NODE_NAME1);
        InMemoryGraphController service = createDefault();
        service.remove(GRAPH_NAME, location, NODE_NAME2);
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void removeNodeAtBadLocationFails() {
        List<String> location = Collections.singletonList(NODE_NAME1);
        InMemoryGraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        service.remove(GRAPH_NAME, location, NODE_NAME2);
    }

    @Test
    public void removeNodeAtLocationRemovesNode() {
        InMemoryGraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        List<String> location = Collections.singletonList(NODE_NAME1);
        service.add(GRAPH_NAME, location, NODE_NAME2);
        service.remove(GRAPH_NAME, location, NODE_NAME2);
        List<String> nodes = service.list(GRAPH_NAME, location);
        assertThat(nodes.size(), is(equalTo(0)));
        nodes = service.list(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes.size(), is(equalTo(1)));
    }
}