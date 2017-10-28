package com.tree.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "html:target/cucumber"},
        glue = "com.tree.steps",
        features = {
//                "classpath:cucumber/tree.feature",
                "classpath:cucumber/tree-nodes.feature"
        }
)
public class TreeViewTest {
}