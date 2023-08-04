package com.github.paulosalonso.algorithms.graph;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@Getter
public class Graph {

    private final List<Vertex> vertices;
    private final Map<Vertex, List<Edge>> forwardEdges;

    private Graph(List<Vertex> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("Vertices must be non null");
        }

        this.vertices = unmodifiableList(vertices);
        this.forwardEdges = vertices.stream().collect(toMap(vertex -> vertex, vertex -> new ArrayList<>()));
    }

    public static Graph of(List<Vertex> vertices) {
        return new Graph(vertices);
    }

    public Graph joinDirected(Vertex from, Vertex to) {
        validateVertices(from, to);
        final var edge = Edge.of(from, to);
        forwardEdges.get(from).add(edge);
        return this;
    }

    public Graph joinDirected(Vertex from, Vertex to, int distance) {
        validateVertices(from, to);
        final var edge = Edge.of(from, to, distance);
        forwardEdges.get(from).add(edge);
        return this;
    }

    public Graph joinUndirected(Vertex a, Vertex b) {
        validateVertices(a, b);
        final var edgeA = Edge.of(a, b);
        forwardEdges.get(a).add(edgeA);
        final var edgeB = Edge.of(b, a);
        forwardEdges.get(b).add(edgeB);
        return this;
    }

    public Graph joinUndirected(Vertex a, Vertex b, int distance) {
        validateVertices(a, b);
        final var edgeA = Edge.of(a, b, distance);
        forwardEdges.get(a).add(edgeA);
        final var edgeB = Edge.of(b, a, distance);
        forwardEdges.get(b).add(edgeB);
        return this;
    }

    public Optional<Integer> getDistance(Vertex from, Vertex to) {
        final var edges = forwardEdges.get(from);
        return edges.stream()
                .filter(edge -> edge.to.equals(to))
                .findFirst()
                .map(Edge::getDistance);
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        return forwardEdges.get(vertex).stream().map(Edge::getTo).toList();
    }

    public List<Edge> getForwardEdges(Vertex vertex) {
        return forwardEdges.get(vertex);
    }

    public int indexOf(Vertex vertex) {
        return vertices.indexOf(vertex);
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
