package com.demo.treeview;

import com.demo.controller.GraphController;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/graph")
public class HierarchyController {

    @Autowired private GraphController graphService;

    @GetMapping
    @ResponseBody
    public Set<String> get() {
        return graphService.getNames();
    }

    @GetMapping(value = "/render/{name}", produces = "application/json")
    @ResponseBody
    public List<Content> renderGraph(@PathVariable @NotNull String name) {
        return toContentList(name, Collections.emptyList());
    }

    private List<Content> toContentList(String name, List<String> location) {
        List<String> titles = graphService.list(name, location);
        final Function<String, Content> nodeToContent = node -> toContentObject(name, location, node);
        return titles.stream().map(nodeToContent).collect(Collectors.toList());
    }

    private Content toContentObject(String name, List<String> location, String node) {
        List<String> subLocation = new ArrayList<>(location);
        subLocation.add(node);
        List<Content> nodes = toContentList(name, subLocation);
        return Content.of(node, null, null, nodes);
    }

    @PostMapping(value = "/create/{graphName}", consumes = "application/json")
    public ResponseEntity<String> createChildAtLocation(@Valid @RequestBody GraphRequest req,
                                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        String title = req.childName;
        graphService.add(graphName, location, title);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/list/{graphName}", produces = "application/json")
    @ResponseBody
    public List<String> getChildrenAtLocation(@Valid @RequestBody GraphRequest req,
                                              @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        return graphService.list(graphName, location);
    }

    @PostMapping(value = "/delete/{graphName}")
    public ResponseEntity<String> removeChildAtLocation(@Valid @RequestBody GraphRequest req,
                                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        String nodeName = req.childName;
        graphService.remove(graphName, location, nodeName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @Value
    public static class GraphRequest {
        private String childName;
        private String[] location;
    }

    @Value(staticConstructor = "of")
    public static class Content {
        private String text; //: 'Parent 1',
        private String href; //: '#parent1',
        private List<String> tags; //: ['4'],
        private List<Content> nodes; //: [
    }
}
