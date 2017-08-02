package com.demo.controller;

import com.demo.api.Api;
import com.demo.changelog.GraphBuilder;
import com.demo.graph.Node;
import com.demo.persistence.ChangePersistence;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class GraphControllerTest {

    private static final String GRAPH_NAME = "graph1";
    private static final String NODE_NAME1 = "node1";
    private static final String NODE_NAME2 = "node2";

    public GraphController createDefault() {
        ChangePersistence persistence = new ChangePersistence();
        ChangeService changeService = new ChangeService();
        GraphBuilder graphBuilder = new GraphBuilder();
        Api.GraphViewer viewer = new GraphViewer();
        return new GraphController(persistence, changeService, graphBuilder, viewer);
    }

    @Test
    public void createDefaultCreatesInstance() {
        GraphController service = createDefault();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void isInitializedEmpty() {
        GraphController service = createDefault();
        assertThat(service.getNames().isEmpty(), is(equalTo(true)));
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void canNotReadNonExistentGraph() {
        GraphController service = createDefault();
        service.list(GRAPH_NAME, Collections.singletonList(NODE_NAME1));
    }

    @Test
    public void canNotListRootNodesOfNonExistentGraph() {
        GraphController service = createDefault();
        List<String> list = service.list("noGo", Collections.emptyList());
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void listNodesAtLocation() {
        GraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        List<String> nodes = service.list(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
    }

    @Test
    public void createNodeAtLocation() {
        GraphController service = createDefault();
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
        GraphController service = createDefault();
        service.remove(GRAPH_NAME, location, NODE_NAME2);
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void removeNodeAtBadLocationFails() {
        List<String> location = Collections.singletonList(NODE_NAME1);
        GraphController service = createDefault();
        service.add(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        service.remove(GRAPH_NAME, location, NODE_NAME2);
    }

    @Test
    public void removeNodeAtLocationRemovesNode() {
        GraphController service = createDefault();
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