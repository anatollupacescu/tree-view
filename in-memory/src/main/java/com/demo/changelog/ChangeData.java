package com.demo.changelog;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
class ChangeData {
    private String name;
    private List<String> location;
}
