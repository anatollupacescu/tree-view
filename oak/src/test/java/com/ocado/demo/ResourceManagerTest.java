package com.ocado.demo;

import com.ocado.demo.api.Api;
import com.ocado.demo.api.ResourceManager;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.JVM)
public class ResourceManagerTest {

    final String tmpRepo = "/tmp/repo";
    final String resourceNode = "images";

    private ResourceManager manager;

    @Before
    public void setUp() {
        SimpleCredentials admin = new SimpleCredentials("admin", "admin".toCharArray());
        manager = new ResourceManager(resourceNode, tmpRepo, admin);
    }

    @Test
    public void inNewRepoResourceFolderIsMissing() throws RepositoryException {
        repoIsEmpty();
    }

    @Test
    public void canAddRemoveNode() throws Exception {
        repoIsEmpty();
        Api.Resource img = new Api.Resource("image1", new byte[]{}, "image/jpg");
        manager.store(img);
        Set<Api.Resource> list = manager.getAll();
        assertThat(list.size(), is(equalTo(1)));
        manager.remove(img.getIdentifier());
        list = manager.getAll();
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    public void repoIsEmpty() throws RepositoryException {
        Set<Api.Resource> list = manager.getAll();
        assertThat(list, is(notNullValue()));
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

    @org.junit.After
    public void tearDown() {
        manager.close();
    }
}