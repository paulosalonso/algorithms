package com.github.paulosalonso.algorithms.graph;

import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Path {

    private final List<Vertex> vertices;
    private final int distance;

    private Path(List<Vertex> vertices, int distance) {
        this.vertices = vertices;
        this.distance = distance;
    }

    public static Path of(List<Vertex> vertices, int distance) {
        return new Path(Collections.unmodifiableList(vertices), distance);
    }

    public String toString() {
        final var verticesChain = vertices.stream()
                .map(Vertex::getValue)
                .map(Object::toString)
                .collect(Collectors.joining(" → "));

        if (distance >= 0) {
            return verticesChain + " | distance: " + distance;
        }

        return verticesChain;
    }
}
