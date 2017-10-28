package com.demo.treeview;

import com.demo.api.Api;
import com.demo.api.UserPass;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/graph")
public class GraphRestController {

    @Autowired
    private Function<UserPass, Api.GraphController> controllerFactory;

    @Autowired
    private UserPass defaultUserPass;

    @GetMapping
    @ResponseBody
    public Set<String> getNames() throws Exception {
        try (Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            return graphController.getNames();
        }
    }

    private final AtomicInteger id = new AtomicInteger(0);

    @GetMapping(value = "/render/{name}", produces = "application/json")
    @ResponseBody
    public List<Content> renderGraph(@PathVariable @NotNull String name) {
        Api.GraphController graphController = controllerFactory.apply(defaultUserPass);
        List<Content> contentList = toContentList(graphController, name, Collections.emptyList());
        graphController.logout();
        return contentList;
    }

    private List<Content> toContentList(Api.GraphController graphController, String name, List<String> location) {
        Set<String> titles = graphController.list(name, location);
        final Function<String, Content> nodeToContent = node -> toContentObject(graphController, name, location, node);
        return titles.stream().map(nodeToContent).collect(Collectors.toList());
    }

    private Content toContentObject(Api.GraphController graphController, String name, List<String> location, String node) {
        List<String> subLocation = new ArrayList<>(location);
        subLocation.add(node);
        List<Content> nodes = toContentList(graphController, name, subLocation);
        return Content.of(id.incrementAndGet(), node, nodes);
    }

    @DeleteMapping(value = "/delete/{graphName}")
    public ResponseEntity<String> remove(@PathVariable String graphName) throws Exception {
        Objects.requireNonNull(graphName);
        try(Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            graphController.remove(graphName);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/create/{graphName}")
    public ResponseEntity<String> create(@PathVariable String graphName) throws Exception {
        Objects.requireNonNull(graphName);
        if (!StringUtils.isEmpty(graphName)) {
            createGraph(graphName);
            return created();
        }
        return new ResponseEntity<>("Please provide a name", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/create/{graphName}/{nodeName}", consumes = "application/json")
    public ResponseEntity<String> add(@Valid @RequestBody String[] location,
                                      @PathVariable String graphName,
                                      @PathVariable String nodeName) throws Exception {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        Objects.requireNonNull(nodeName);
        createNode(graphName, location, nodeName);
        return created();
    }

    private void createNode(String graphName, String[] location, String nodeName) throws Exception {
        try(Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            graphController.add(graphName, Arrays.asList(location), nodeName);
        }
    }

    private void createGraph(String graphName) throws Exception {
        try (Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            graphController.create(graphName);
        }
    }

    private ResponseEntity<String> created() {
        return new ResponseEntity<>("created with success", HttpStatus.CREATED);
    }

    @PostMapping(value = "/list/{graphName}", produces = "application/json")
    @ResponseBody
    public Collection<String> list(@Valid @RequestBody String[] location,
                             @PathVariable String graphName) throws Exception {
        Objects.requireNonNull(graphName);
        List<String> locationList = toList(location);
        try(Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            return graphController.list(graphName, locationList);
        }
    }

    private List<String> toList(String[] location) {
        if (location == null)
            return Collections.emptyList();
        else
            return Arrays.asList(location);
    }

    @PostMapping(value = "/delete/{graphName}/{nodeName}")
    public ResponseEntity<String> remove(@Valid @RequestBody String[] location,
                                         @PathVariable String graphName,
                                         @PathVariable String nodeName) throws Exception {
        Objects.requireNonNull(graphName);
        List<String> locationList = toList(location);
        try(Api.GraphController graphController = controllerFactory.apply(defaultUserPass)) {
            graphController.remove(graphName, locationList, nodeName);
            return ResponseEntity.noContent().build();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String message = ex.getMessage();
        if (message == null || message.isEmpty()) {
            message = ex.getClass().getName();
        }
        return ResponseEntity.badRequest().body(message);
    }

    @Value(staticConstructor = "of")
    public static class Content {
        private long id;
        private String text;
        private List<Content> children;
    }
}
