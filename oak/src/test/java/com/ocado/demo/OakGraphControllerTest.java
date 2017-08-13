package com.ocado.demo;

import com.demo.api.UserPass;
import com.ocado.demo.oak.NodeNotFoundException;
import com.ocado.demo.oak.OakGraphController;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OakGraphControllerTest {

    private final String TEST = "test";

    private OakGraphController manager;

    @Before
    public void setUp() {
        NodeStore ns = new MemoryNodeStore();
        Repository repository = new Jcr(ns).createRepository();
        manager = new OakGraphController(repository);
        manager.login(new UserPass("admin", "admin"));
    }

    @Test
    public void test1_inNewRepoResourceFolderIsMissing() throws RepositoryException {
        assertThat(manager.getNames().isEmpty(), is(equalTo(true)));
    }

    @Test(expected = RuntimeException.class)
    public void test2_rootNodeIsNotPresent() throws RepositoryException {
        manager.list(TEST, Collections.emptyList());
    }

    @Test
    public void test3_canAddRemoveNode() throws Exception {
        createRepo(TEST);
        repoIsEmpty();
        String child1 = "child1";
        addChild(child1);
        assertChildrenCount(1);
        removeChild(child1);
        assertChildrenCount(0);
    }

    @Test
    public void canNavigate() {
        createRepo(TEST);
        String products1 = "products";
        Set<String> products = manager.list(TEST, Collections.emptyList());
        assertThat(products.isEmpty(), is(equalTo(true)));
        manager.add(TEST, Collections.emptyList(), products1);
        String butter = "butter";
        manager.add(TEST, Collections.singletonList(products1), butter);
        products = manager.list(TEST, Collections.singletonList(products1));
        assertThat(products.isEmpty(), is(equalTo(false)));
    }

    @Test(expected = NodeNotFoundException.class)
    public void canNotNavigateToBadLocation() {
        createRepo(TEST);
        manager.list(TEST, Collections.singletonList("non-existent"));
    }

    private void removeChild(String child1) {
        manager.remove(TEST, Collections.emptyList(), child1);
    }

    private void assertChildrenCount(int i) {
        Set<String> list = manager.list(TEST, Collections.emptyList());
        assertThat(list.size(), is(equalTo(i)));
    }

    private void addChild(String child1) {
        manager.add(TEST, Collections.emptyList(), child1);
    }

    private void createRepo(String graphName) {
        manager.create(graphName);
    }

    private void repoIsEmpty() {
        Set<String> list = manager.list(TEST, Collections.emptyList());
        assertThat(list, is(notNullValue()));
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    @org.junit.After
    public void tearDown() {
        manager.close();
    }
}