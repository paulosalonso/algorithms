package com.github.paulosalonso.algorithms.graph;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static lombok.AccessLevel.PRIVATE;

@Getter
public class Graph {

    private final List<Vertex> vertices;
    private final List<Edge> edges;

    private Graph(List<Vertex> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Vertices must be non null");
        }

        this.vertices = unmodifiableList(vertices);
        this.edges = new ArrayList<>();
    }

    public static Graph of(List<Vertex> vertices) {
        return new Graph(vertices);
    }

    public Graph joinDirected(Vertex from, Vertex to) {
        validateVertices(from, to);

        edges.add(Edge.of(from, to));

        return this;
    }

    public Graph joinDirected(Vertex from, Vertex to, int distance) {
        validateVertices(from, to);

        edges.add(Edge.of(from, to, distance));

        return this;
    }

    public Graph joinUndirected(Vertex a, Vertex b) {
        validateVertices(a, b);

        edges.add(Edge.of(a, b));
        edges.add(Edge.of(b, a));

        return this;
    }

    public Graph joinUndirected(Vertex a, Vertex b, int distance) {
        validateVertices(a, b);

        edges.add(Edge.of(a, b, distance));
        edges.add(Edge.of(b, a, distance));

        return this;
    }

    public Optional<Integer> getWeight(Vertex from, Vertex to) {
        return edges.stream()
                .filter(edge -> edge.from.equals(from))
                .filter(edge -> edge.to.equals(to))
                .findFirst()
                .map(Edge::getDistance);
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        return edges.stream()
                .filter(v -> v.getFrom().equals(vertex))
                .map(Edge::getTo)
                .toList();
    }

    public List<Edge> getForwardEdges(Vertex vertex) {
        return edges.stream()
                .filter(edge -> edge.from.equals(vertex))
                .toList();
    }

    private void validateVertices(Vertex a, Vertex b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Vertices must be non null");
        }

        if (!vertices.contains(a) || !vertices.contains(b)) {
            throw new IllegalArgumentException("Vertices must belongs to graph");
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Vertex {

        private final int value;

        public static Vertex of(int value) {
            return new Vertex(value);
        }
    }

    @RequiredArgsConstructor(access = PRIVATE)
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Edge {

        @NonNull
        private final Vertex from;

        @NonNull
        private final Vertex to;

        private final int distance;

        private static Edge of(Vertex from, Vertex to) {
            return new Edge(from, to, -1);
        }

        private static Edge of(Vertex from, Vertex to, int weight) {
            if (weight < 0) {
                throw new IllegalArgumentException("Weight must be greater than zero");
            }

            return new Edge(from, to, weight);
        }
    }
}
