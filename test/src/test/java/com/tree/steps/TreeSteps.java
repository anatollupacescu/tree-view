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

    private final GraphTester graphTester = TesterFactory.getTester();

    @Given("^no trees have been created$")
    public void no_trees_have_been_created() {
        removeAll();
    }

    private void removeAll() {
        Set<String> treeNames = graphTester.getNames();
        for (String tree : treeNames) {
            graphTester.remove(tree);
        }
    }

    @When("^I create a tree called (.*)$")
    public void i_create_a_tree_called_products(String graphName) {
        graphTester.create(graphName);
    }

    @When("^I remove the tree instance called (.*)$")
    public void i_remove_the_tree_instance_called_alpha(String name) {
        graphTester.remove(name);
    }

    @Given("^a tree instance called (.*) is present$")
    public void a_tree_instance_called_products_is_present(String name) {
        Set<String> treeNames = graphTester.getNames();
        assertThat(treeNames.contains(name), is(equalTo(true)));
    }

    @Then("^the tree names array should have (\\d) (?:elements?|element)$")
    public void a_tree_names_reponse_should_have_size(int size) {
        Set<String> treeNames = graphTester.getNames();
        assertThat(treeNames.size(), is(equalTo(size)));
    }

    @Given("^two trees, alpha and beta, are present$")
    public void two_trees_alpha_and_beta_are_present() throws Throwable {
        removeAll();
        graphTester.create("alpha");
        graphTester.create("beta");
    }

    @Then("^the tree names array should contain the name (.*)$")
    public void the_tree_names_array_should_contain_the_name_alpha(String graphName) throws Throwable {
        Set<String> treeNames = graphTester.getNames();
        assertThat(treeNames.contains(graphName), is(equalTo(true)));
    }
}
