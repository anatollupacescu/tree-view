package com.ocado.demo;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.api.CommitFailedException;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.memory.MemoryNodeStore;
import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.commit.EmptyHook;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.junit.Test;

import javax.jcr.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JCRTest {

    private SimpleCredentials credentials = new SimpleCredentials("admin", "admin".toCharArray());

    @Test
    public void testCommit() throws CommitFailedException {
        final NodeStore ns = new MemoryNodeStore();
        final String imagesFolder = "images";
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        NodeBuilder rootBuilder = ns.getRoot().builder();
        rootBuilder.child(imagesFolder);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        ns.merge(rootBuilder, EmptyHook.INSTANCE, CommitInfo.EMPTY);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(true)));
    }

    @Test
    public void testRemove() throws CommitFailedException {
        final NodeStore ns = new MemoryNodeStore();
        final String imagesFolder = "images";
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        NodeBuilder rootBuilder = ns.getRoot().builder();
        rootBuilder.child(imagesFolder);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        ns.merge(rootBuilder, EmptyHook.INSTANCE, CommitInfo.EMPTY);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(true)));
        rootBuilder.child(imagesFolder).remove();
        ns.merge(rootBuilder, EmptyHook.INSTANCE, CommitInfo.EMPTY);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
    }

    @Test
    public void testCreate() throws CommitFailedException, RepositoryException {
        final NodeStore ns = new MemoryNodeStore();
        final String imagesFolder = "images";
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        NodeBuilder rootBuilder = ns.getRoot().builder();
        NodeBuilder imagesFolderBuilder = rootBuilder.child(imagesFolder);
        imagesFolderBuilder.setProperty(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_FOLDER, Type.NAME);
        assertThat(ns.getRoot().getChildNode(imagesFolder).exists(), is(equalTo(false)));
        ns.merge(rootBuilder, EmptyHook.INSTANCE, CommitInfo.EMPTY);
        Repository repo = new Jcr(new Oak(ns)).createRepository();
        Session session = repo.login(credentials);
        Node images = session.getRootNode().getNode("images");
        session.save();
        assertThat(images.getPrimaryNodeType().getName(), is(equalTo(JcrConstants.NT_FOLDER)));
        session.logout();
        session = repo.login(credentials);
        Node images1 = session.getRootNode().getNode("images");
        assertThat(images1.isNode(), is(equalTo(true)));
        assertThat(images1.getPrimaryNodeType().getName(), is(equalTo(JcrConstants.NT_FOLDER)));
    }
}
