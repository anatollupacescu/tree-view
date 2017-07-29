package com.demo.treeview;

import com.demo.controller.GraphController;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/graph")
public class HierarchyController {

    private final GraphController controller = new GraphController();

    @GetMapping
    @ResponseBody
    public Set<String> get() {
        return controller.listGraphNames();
    }

    @GetMapping(value = "/render/{name}", produces = "application/json")
    @ResponseBody
    public List<Content> renderGraph(@PathVariable String name) {
        return toContentList(name, Collections.emptyList());
    }

    private List<Content> toContentList(String name, List<String> location) {
        List<String> titles = controller.listNodesAtLocation(name, location);
        return titles.stream()
                .map(st -> {
                    List<String> subLocation = new ArrayList<>(location);
                    subLocation.add(st);
                    List<Content> nodes = toContentList(name, subLocation);
                    return Content.of(st, null, null, nodes);
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/create")
    public String createGraph(@RequestBody String name) {
        controller.createGraph(name);
        return "redirect:/graph";
    }

    @PostMapping(value = "/create/{graphName}", consumes = "application/json")
    public String createChildAtLocation(@Valid @RequestBody GraphRequest req,
                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        String title = req.childName;
        controller.createNodeAtLocation(graphName, location, title);
        return "redirect:/graph/list/" + graphName;
    }

    @PostMapping(value = "/list/{graphName}", produces = "application/json")
    @ResponseBody
    public List<String> getChildrenAtLocation(@Valid @RequestBody GraphRequest req,
                                              @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        List<String> list = controller.listNodesAtLocation(graphName, location);
        return list;
    }

    @DeleteMapping(value = "/{graphName}")
    public String remove(@PathVariable String graphName) {
        controller.removeGraph(graphName);
        return "redirect:/graph";
    }

    @PostMapping(value = "/delete/{graphName}")
    public String removeChildAtLocation(@Valid @RequestBody GraphRequest req,
                                        @PathVariable String graphName) {
        List<String> location = Arrays.asList(req.location);
        controller.removeNodeAtLocation(graphName, location);
        return "redirect:/graph/list/" + graphName;
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
