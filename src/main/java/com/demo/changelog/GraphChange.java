package com.demo.changelog;

public class GraphChange {

    private final ChangeType type;
    private final ChangeData data;

    public GraphChange(ChangeType type, ChangeData data) {
        this.type = type;
        this.data = data;
    }

    public ChangeData getData() {
        return data;
    }

    public ChangeType getType() {
        return type;
    }
}

