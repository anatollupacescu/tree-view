package com.ocado.demo;

import com.ocado.demo.oak.OakGraphController;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OakGraphControllerTest {

    private OakGraphController manager;
    private String graphName = "test";

    @Before
    public void setUp() {
        SimpleCredentials admin = new SimpleCredentials("admin", "admin".toCharArray());
        manager = new OakGraphController(admin);
    }

    @Test
    public void test1_inNewRepoResourceFolderIsMissing() throws RepositoryException {
        assertThat(manager.getNames().isEmpty(), is(equalTo(true)));
    }

    @Test(expected = RuntimeException.class)
    public void test2_rootNodeIsNotPresent() throws RepositoryException {
        manager.list(graphName, Collections.emptyList());
    }

    @Test
    public void test3_canAddRemoveNode() throws Exception {
        createRepo();
        repoIsEmpty();
        String child1 = "child1";
        addChild(child1);
        assertChildrenCount(1);
        removeChild(child1);
        assertChildrenCount(0);
    }

    @Test
    public void canNavigate() {
        createRepo();
        String products1 = "products";
        List<String> products = manager.list(graphName, Collections.emptyList());
        assertThat(products.isEmpty(), is(equalTo(true)));
        manager.add(graphName, Collections.emptyList(), products1);
        String butter = "butter";
        manager.add(graphName, Collections.singletonList(products1), butter);
        products = manager.list(graphName, Collections.singletonList(products1));
        assertThat(products.isEmpty(), is(equalTo(false)));
    }

    private void removeChild(String child1) {
        manager.remove(graphName, Collections.emptyList(), child1);
    }

    private void assertChildrenCount(int i) {
        List<String> list = manager.list(graphName, Collections.emptyList());
        assertThat(list.size(), is(equalTo(i)));
    }

    private void addChild(String child1) {
        manager.add(graphName, Collections.emptyList(), child1);
    }

    private void createRepo() {
        manager.create(graphName);
    }

    public void repoIsEmpty() throws RepositoryException {
        List<String> list = manager.list(graphName, Collections.emptyList());
        assertThat(list, is(notNullValue()));
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    @org.junit.After
    public void tearDown() {
        manager.close();
    }
}