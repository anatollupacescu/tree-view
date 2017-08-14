package com.demo.changelog;

import com.demo.graph.Graph;
import com.demo.graph.Node;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InMemoryGraphBuilderTest {

    private static final String GRAPH_NAME = "graph";
    private static final InMemoryGraphBuilder builder = new InMemoryGraphBuilder();

    @Test(expected = Node.NodeNotFoundException.class)
    public void canNotRemoveNonExistentChild() {
        ChangeData data = ChangeData.of("lolo", Collections.emptyList());
        GraphChange graphChange = new GraphChange(ChangeType.REMOVE_CHILD, data);
        List<GraphChange> changes = Collections.singletonList(graphChange);
        builder.build(GRAPH_NAME, changes);
    }

    @Test
    public void canAddChild() {
        ChangeData data = ChangeData.of("lolo", Collections.emptyList());
        GraphChange graphChange = new GraphChange(ChangeType.ADD_CHILD, data);
        List<GraphChange> changes = Collections.singletonList(graphChange);
        Graph graph = builder.build(GRAPH_NAME, changes);
        Node lolo = graph.getChild("lolo");
        assertThat(lolo, is(notNullValue()));
    }

    @Test
    public void canAddChildToLocation() {
        ChangeData data = ChangeData.of("lolo", Collections.emptyList());
        GraphChange graphChange1 = new GraphChange(ChangeType.ADD_CHILD, data);
        data = ChangeData.of("sub", Collections.singletonList("lolo"));
        GraphChange graphChange2 = new GraphChange(ChangeType.ADD_CHILD, data);
        List<GraphChange> changes = Arrays.asList(graphChange1, graphChange2);
        Graph graph = builder.build(GRAPH_NAME, changes);
        Node lolo = graph.getChild("lolo");
        assertThat(lolo, is(notNullValue()));
        Node sub = lolo.getChild("sub");
        assertThat(sub, is(notNullValue()));
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void canNotAddChildToBadLocation() {
        ChangeData data = ChangeData.of("sub", Collections.singletonList("lolo"));
        GraphChange graphChange2 = new GraphChange(ChangeType.ADD_CHILD, data);
        List<GraphChange> changes = Collections.singletonList(graphChange2);
        builder.build(GRAPH_NAME, changes);
    }

    @Test(expected = Node.NodeNotFoundException.class)
    public void canNotAddExistentChild() {
        ChangeData data = ChangeData.of("lolo", Collections.emptyList());
        GraphChange graphChange1 = new GraphChange(ChangeType.ADD_CHILD, data);
        GraphChange graphChange2 = new GraphChange(ChangeType.ADD_CHILD, data);
        List<GraphChange> changes = Arrays.asList(graphChange1, graphChange2);
        builder.build(GRAPH_NAME, changes);
    }
}