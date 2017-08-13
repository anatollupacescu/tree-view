package com.tree.steps;

import com.demo.GraphTester;
import com.demo.WebGraphTester;
import com.tree.TesterFactory;
import com.tree.domain.GraphUpdate;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TreeNodeSteps {

    private String graphName;
    private GraphTester graphTester = TesterFactory.getTester();

    @Given("^I have a tree called (.*)$")
    public void i_have_a_tree(String graphName) throws Throwable {
        this.graphName = graphName;
        graphTester.create(graphName);
        assertThat(graphTester.getNames(), is(notNullValue()));
        assertThat(graphTester.getNames().contains(graphName), is(equalTo(true)));
    }

    @When("^I add the following nodes$")
    public void i_add_following_nodes(List<GraphUpdate> list) throws Throwable {
        for (GraphUpdate graphUpdate : list) {
            List<String> location = graphTester.toList(graphUpdate.location);
            graphTester.add(graphName, location, graphUpdate.name);
        }
    }

    @Then("^the root has (\\d+) (?:nodes?|node)$")
    public void root_has_nodes(int nodeCount) throws Throwable {
        List<String> children = graphTester.list(graphName, Collections.emptyList());
        Integer size = children.size();
        assertThat(size, is(equalTo(nodeCount)));
    }

    @And("^the node at (.*) contains (.*)$")
    public void parent_contains_child(String parentLocation, String childName) {
        List<String> location = graphTester.toList(parentLocation);
        List<String> children = graphTester.list(graphName, location);
        assertThat(children.contains(childName), is(equalTo(true)));
    }

    @And("^the node at (.*) has no children$")
    public void node_has_no_children(String parentLocation) {
        List<String> location = graphTester.toList(parentLocation);
        List<String> children = graphTester.list(graphName, location);
        assertThat(children.isEmpty(), is(equalTo(true)));
    }

    @And("^I remove node (.*) at location in root")
    public void i_remove_node_in_root(String nodeName) {
        graphTester.remove(graphName, Collections.emptyList(), nodeName);
    }
}
