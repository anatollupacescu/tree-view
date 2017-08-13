package com.demo.persistence;

import com.demo.changelog.GraphChange;
import com.demo.changelog.InMemoryGraphChangeBuilder;
import com.demo.graph.api.GraphChangeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ChangeServiceTest {

    private GraphChangeBuilder<GraphChange> changeService;

    @Before
    public void setUp() {
        changeService = new InMemoryGraphChangeBuilder();
    }

    @Test
    public void canCreateChanges() {
        List<GraphChange> changes = new ArrayList<>();
        changes.add(changeService.add(Collections.emptyList(), "unu"));
        changes.add(changeService.remove(Collections.singletonList("unu"), "doi"));
        assertThat(changes.size(), is(equalTo(2)));
    }
}