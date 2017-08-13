package com.demo.treeview;

import com.demo.api.Api;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/graph")
public class GraphRestController {

    @Autowired
    private Api.GraphController graphController;

    @GetMapping
    @ResponseBody
    public Set<String> getNames() {
        return graphController.getNames();
    }

    @GetMapping(value = "/render/{name}", produces = "application/json")
    @ResponseBody
    public List<Content> renderGraph(@PathVariable @NotNull String name) {
        return toContentList(name, Collections.emptyList());
    }

    private List<Content> toContentList(String name, List<String> location) {
        List<String> titles = graphController.list(name, location);
        final Function<String, Content> nodeToContent = node -> toContentObject(name, location, node);
        return titles.stream().map(nodeToContent).collect(Collectors.toList());
    }

    private Content toContentObject(String name, List<String> location, String node) {
        List<String> subLocation = new ArrayList<>(location);
        subLocation.add(node);
        List<Content> nodes = toContentList(name, subLocation);
        return Content.of(node, null, null, nodes);
    }

    @DeleteMapping(value = "/delete/{graphName}")
    public ResponseEntity<String> remove(@PathVariable String graphName) {
        Objects.requireNonNull(graphName);
        graphController.remove(graphName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/create/{graphName}")
    public ResponseEntity<String> create(@PathVariable String graphName) {
        Objects.requireNonNull(graphName);
        if(!StringUtils.isEmpty(graphName)) {
            createGraph(graphName);
            return created();
        }
        return new ResponseEntity<>("Please provide a name", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/create/{graphName}/{nodeName}", consumes = "application/json")
    public ResponseEntity<String> add(@Valid @RequestBody String[] location,
                                      @PathVariable String graphName,
                                      @PathVariable String nodeName) {
        Objects.requireNonNull(graphName);
        Objects.requireNonNull(location);
        Objects.requireNonNull(nodeName);
        createNode(graphName, location, nodeName);
        return created();
    }

    private void createNode(String graphName, String[] location, String nodeName) {
        graphController.add(graphName, Arrays.asList(location), nodeName);
    }

    private void createGraph(String graphName) {
        graphController.create(graphName);
    }

    private ResponseEntity<String> created() {
        return new ResponseEntity<>("created with success", HttpStatus.CREATED);
    }

    @PostMapping(value = "/list/{graphName}", produces = "application/json")
    @ResponseBody
    public List<String> list(@Valid @RequestBody String[] location,
                             @PathVariable String graphName) {
        Objects.requireNonNull(graphName);
        List<String> locationList = toList(location);
        return graphController.list(graphName, locationList);
    }

    private List<String> toList(String[] location) {
        if(location == null)
            return Collections.emptyList();
        else
            return Arrays.asList(location);
    }

    @PostMapping(value = "/delete/{graphName}/{nodeName}")
    public ResponseEntity<String> remove(@Valid @RequestBody String [] location,
                                         @PathVariable String graphName, @PathVariable String nodeName) {
        Objects.requireNonNull(graphName);
        List<String> locationList = toList(location);
        graphController.remove(graphName, locationList, nodeName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        private String text; //: 'Parent 1',
        private String href; //: '#parent1',
        private List<String> tags; //: ['4'],
        private List<Content> nodes; //: [
    }
}
