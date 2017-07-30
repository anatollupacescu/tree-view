package com.demo.controller;

import com.demo.graph.Node;
import com.demo.persistence.ChangePersistence;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class GraphServiceTest {

    private static final String GRAPH_NAME = "graph1";
    private static final String NODE_NAME1 = "node1";
    private static final String NODE_NAME2 = "node2";

    public GraphService createDefault(){
        ChangePersistence persistence = new ChangePersistence();
        ChangeService changeService = new ChangeService(persistence);
        return new GraphService(changeService);
    }

    @Test
    public void createDefaultCreatesInstance() {
        GraphService service = createDefault();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void isInitializedEmpty() {
        GraphService service = createDefault();
        assertThat(service.listGraphNames().isEmpty(), is(equalTo(true)));
    }

    @Test(expected = ChangeService.GraphNotFoundException.class)
    public void canNotReadNonExistentGraph() {
        GraphService service = createDefault();
        service.listNodesAtLocation("noGo", Collections.emptyList());
    }

    @Test
    public void listNodesAtLocation() {
        GraphService service = createDefault();
        service.createNodeAtLocation(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        List<String> nodes = service.listNodesAtLocation(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
    }

    @Test
    public void createNodeAtLocation() {
        GraphService service = createDefault();
        service.createNodeAtLocation(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        service.createNodeAtLocation(GRAPH_NAME, Collections.singletonList(NODE_NAME1), NODE_NAME2);
        List<String> nodes = service.listNodesAtLocation(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
        nodes = service.listNodesAtLocation(GRAPH_NAME, Collections.singletonList(NODE_NAME1));
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(equalTo(1)));
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void removeNodeAtBadLocationFails() {
        List<String> location = Collections.singletonList(NODE_NAME1);
        GraphService service = createDefault();
        service.removeNodeAtLocation(GRAPH_NAME, location, NODE_NAME2);
    }

    @Test
    public void removeNodeAtLocationRemovesNode() {
        GraphService service = createDefault();
        service.createNodeAtLocation(GRAPH_NAME, Collections.emptyList(), NODE_NAME1);
        List<String> location = Collections.singletonList(NODE_NAME1);
        service.createNodeAtLocation(GRAPH_NAME, location, NODE_NAME2);
        service.removeNodeAtLocation(GRAPH_NAME, location, NODE_NAME2);
        List<String> nodes = service.listNodesAtLocation(GRAPH_NAME, location);
        assertThat(nodes.size(), is(equalTo(0)));
        nodes = service.listNodesAtLocation(GRAPH_NAME, Collections.emptyList());
        assertThat(nodes.size(), is(equalTo(1)));
    }
}