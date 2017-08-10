package com.demo.changelog;

import lombok.Value;

@Value
public class GraphChange {
    private ChangeType type;
    private ChangeData data;
}
