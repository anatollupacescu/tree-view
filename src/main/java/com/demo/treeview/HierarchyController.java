package com.demo.treeview;

import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/app")
public class HierarchyController {

    private final Content isle1 = new Content("text", "href", Collections.emptyList(), Collections.emptyList());
    private final Content isle0 = new Content("parent", "href", Collections.emptyList(), Arrays.asList(isle1));
    private final Content isle2 = new Content("multi", "href", Collections.emptyList(), Arrays.asList(isle0, isle1));

    @GetMapping(value="/data", produces = "application/json")
    @ResponseBody
    public List<Content> getContent() {
        return Arrays.asList(isle0, isle1, isle2, isle0);
    }

@Value(staticConstructor = "of")
    public static class Content {
        private String text; //: 'Parent 1',
        private String href; //: '#parent1',
        private List<String> tags; //: ['4'],
        private List<Content> nodes; //: [
    }
}
