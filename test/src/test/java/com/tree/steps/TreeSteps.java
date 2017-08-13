package com.tree.steps;

import com.demo.GraphTester;
import com.tree.TesterFactory;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TreeSteps {

    private GraphTester graphTester = TesterFactory.getTester();

    @Given("^no trees have been created$")
    public void no_trees_have_been_created() throws Throwable {
        Set<String> treeNames = graphTester.getNames();
        for (String tree : treeNames) {
            graphTester.remove(tree);
        }
    }

    @When("^I create a tree called (.*)$")
    public void i_create_a_tree_called_products(String graphName) throws Throwable {
        graphTester.create(graphName);
    }

    @When("^I remove the tree instance called (.*)$")
    public void i_remove_the_tree_instance_called_alpha(String name) throws Throwable {
        graphTester.remove(name);
    }

    @Given("^a tree instance called (.*) is present$")
    public void a_tree_instance_called_products_is_present(String name) throws Throwable {
        Set<String> treeNames = graphTester.getNames();
        assertThat(treeNames.contains(name), is(equalTo(true)));
    }

    @Then("^the tree names array should have (\\d) (?:elements?|element)$")
    public void a_tree_names_reponse_should_have_size(int size) throws Throwable {
        Set<String> treeNames = graphTester.getNames();
        assertThat(treeNames.size(), is(equalTo(size)));
    }
}