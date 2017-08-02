package com.demo.changelog;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphChange that = (GraphChange) o;
        return type == that.type &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }

    @Override
    public String toString() {
        return "GraphChange{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
