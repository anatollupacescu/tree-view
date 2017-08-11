package com.ocado.demo.api;

import com.google.common.io.ByteStreams;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.api.Blob;
import org.apache.jackrabbit.oak.api.CommitFailedException;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.plugins.value.BinaryBasedBlob;
import org.apache.jackrabbit.oak.segment.SegmentNodeStore;
import org.apache.jackrabbit.oak.segment.SegmentNodeStoreBuilders;
import org.apache.jackrabbit.oak.segment.file.FileStore;
import org.apache.jackrabbit.oak.segment.file.FileStoreBuilder;
import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.commit.EmptyHook;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.jackrabbit.oak.spi.state.NodeState;
import org.apache.jackrabbit.value.ValueFactoryImpl;

import javax.jcr.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static org.apache.jackrabbit.JcrConstants.*;

public class ResourceManager implements Api.ResourceManager<Api.Resource>, AutoCloseable {

    public static final String JCR_MIME_TYPE = "jcr:mimeType";

    private final FileStore fs;
    private final Session session;
    private final String resourceNode;
    private final SegmentNodeStore ns;

    public ResourceManager(String resourceNode, String repositoryPath, SimpleCredentials admin) {
        this.resourceNode = resourceNode;
        try {
            File directory = new File(repositoryPath);
            this.fs = FileStoreBuilder.fileStoreBuilder(directory).build();
            this.ns = SegmentNodeStoreBuilders.builder(fs).build();
            Repository repo = new Jcr(ns).createRepository();
            this.session = repo.login(admin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Api.Resource get(String identifier) {
        Node folder = getImagesFolderNode();
        Node file = getNodeChild(folder, identifier);
        return mapToResource(file);
    }

    private Node getNodeChild(Node folder, String identifier) {
        try {
            return folder.getNode(identifier);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void store(Api.Resource image) {
        NodeBuilder rootBuilder = getRootBuilder();
        NodeBuilder builder = rootBuilder.child(resourceNode);
        createImage(builder, image);
        emptyCommit(rootBuilder);
    }

    @Override
    public void remove(String nodeName) {
        NodeBuilder rootBuilder = getRootBuilder();
        NodeBuilder resourceNodeBuilder = rootBuilder.child(resourceNode);
        NodeBuilder childNodeBuilder = resourceNodeBuilder.child(nodeName);
        childNodeBuilder.remove();
        emptyCommit(rootBuilder);
    }

    private void createImage(NodeBuilder parent, Api.Resource image) {
        String nodeName = image.getIdentifier();
        NodeBuilder child = parent.child(nodeName);
        setNodePrimaryType(child, NT_FILE);
        createImageContentNode(child, image);
    }

    private void setNodePrimaryType(NodeBuilder child, String type) {
        child.setProperty(JCR_PRIMARYTYPE, type, Type.NAME);
    }

    private void createImageContentNode(NodeBuilder imageNode, Api.Resource resource) {
        Blob binary = getBinaryData(resource.getContent());
        String mimeType = resource.getMimeType();
        NodeBuilder image = imageNode.child(JCR_CONTENT);
        setNodePrimaryType(image, NT_RESOURCE);
        image.setProperty(JCR_DATA, binary, Type.BINARY);
        image.setProperty(JCR_MIME_TYPE, mimeType);
        image.setProperty(JCR_LASTMODIFIED, now(), Type.DATE);
    }

    private String now() {
        Value date = ValueFactoryImpl.getInstance().createValue(Calendar.getInstance());
        try {
            return date.getString();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Blob getBinaryData(byte[] image) {
        InputStream stream = new ByteArrayInputStream(image);
        try {
            Binary binary = session.getValueFactory().createBinary(stream);
            return new BinaryBasedBlob(binary);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean resourceNodePresent() {
        NodeState imagesFolder = getRootChild(resourceNode);
        return imagesFolder.exists();
    }

    private NodeState getRootChild(String name) {
        NodeState root = ns.getRoot();
        return getChild(root, name);
    }

    private NodeState getChild(NodeState parent, String name) {
        return parent.getChildNode(name);
    }

    private void createResourceFolder() {
        if(!resourceNodePresent()) {
            NodeBuilder builder = getRootBuilder();
            builder.child(resourceNode);
            emptyCommit(builder);
            setResourceFolderPrimaryType();
        }
    }

    private void setResourceFolderPrimaryType() {
        try {
            Node rootNode = session.getRootNode();
            Node node = rootNode.getNode(resourceNode);
            node.setPrimaryType(JcrConstants.NT_FOLDER);
            session.save();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private NodeBuilder getRootBuilder() {
        NodeState root = ns.getRoot();
        return root.builder();
    }

    @Override
    public Set<Api.Resource> getAll() {
        final Set<Api.Resource> nodes = new HashSet<>();
        try {
            Node root = getImagesFolderNode();
            root.getNodes().forEachRemaining(node -> {
                Node imageNode = (Node) node;
                Api.Resource res = mapToResource(imageNode);
                nodes.add(res);
            });
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        return nodes;
    }

    private Node getImagesFolderNode() {
        try {
            Node root = session.getRootNode();
            if (root.hasNode(resourceNode)) {
                return root.getNode(resourceNode);
            }
            createResourceFolder();
            return getImagesFolderNode();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private void emptyCommit(NodeBuilder builder) {
        try {
            ns.merge(builder, EmptyHook.INSTANCE, CommitInfo.EMPTY);
            session.save();
        } catch (CommitFailedException | RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private Api.Resource mapToResource(Node imageRootNode) {
        try {
            Node contentNode = imageRootNode.getNode(JCR_CONTENT);
            String identifier = imageRootNode.getName();
            Property mimeTypeProperty = contentNode.getProperty(JCR_MIME_TYPE);
            Property binaryProperty = contentNode.getProperty(JCR_DATA);
            String mimeType = mimeTypeProperty.getString();
            byte[] contents = getContents(binaryProperty);
            return new Api.Resource(identifier, contents, mimeType);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getContents(Property binaryProperty) {
        try {
            Binary bin = binaryProperty.getBinary();
            return ByteStreams.toByteArray(bin.getStream());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        try {
            session.save();
            fs.flush();
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        save();
        session.logout();
        fs.close();
    }
}
