package com.demo.treeview;

import com.demo.controller.GraphService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/graph")
public class HierarchyController {

    private @Autowired
    GraphService graphService;

    @GetMapping
    @ResponseBody
    public Set<String> get() {
        return graphService.listGraphNames();
    }

    @GetMapping(value = "/render/{name}", produces = "application/json")
    @ResponseBody
    public List<Content> renderGraph(@PathVariable @NotNull String name) {
        return toContentList(name, Collections.emptyList());
    }

    private List<Content> toContentList(String name, List<String> location) {
        List<String> titles = graphService.listNodesAtLocation(name, location);
        return titles.stream()
                .map(st -> {
                    List<String> subLocation = new ArrayList<>(location);
                    subLocation.add(st);
                    List<Content> nodes = toContentList(name, subLocation);
                    return Content.of(st, null, null, nodes);
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/create/{graphName}", consumes = "application/json")
    public ResponseEntity<String> createChildAtLocation(@Valid @RequestBody GraphRequest req,
                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        String title = req.childName;
        graphService.createNodeAtLocation(graphName, location, title);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/list/{graphName}", produces = "application/json")
    @ResponseBody
    public List<String> getChildrenAtLocation(@Valid @RequestBody GraphRequest req,
                                              @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        return graphService.listNodesAtLocation(graphName, location);
    }

    @PostMapping(value = "/delete/{graphName}")
    public ResponseEntity<String> removeChildAtLocation(@Valid @RequestBody GraphRequest req,
                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        String nodeName = req.childName;
        graphService.removeNodeAtLocation(graphName, location, nodeName);
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
