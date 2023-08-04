package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import com.github.paulosalonso.algorithms.graph.Path;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

import static java.util.Comparator.comparing;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class Predecessors {

    private final Map<Vertex, Vertex> predecessors;
    private final Map<Vertex, Integer> distances;

    public static Predecessors newInstance() {
        return new Predecessors(new HashMap<>(), new HashMap<>());
    }

    public static Predecessors of(Map<Vertex, Vertex> predecessors, Map<Vertex, Integer> distances) {
        return new Predecessors(predecessors, distances);
    }

    public void put(Vertex vertex, Vertex predecessor) {
        predecessors.put(vertex, predecessor);
        distances.put(vertex, -1);
    }

    public void put(Vertex vertex, Vertex predecessor, int distance) {
        predecessors.put(vertex, predecessor);
        distances.put(vertex, distance);
    }

    public List<Predecessor> toList() {
        return predecessors.entrySet().stream()
                .map(entry -> Predecessor.of(entry.getKey(), entry.getValue(), distances.get(entry.getKey())))
                .toList();
    }

    public PriorityQueue<Predecessor> toPriorityQueue() {
        final var priorityQueue = new PriorityQueue<>(comparing(Predecessor::getDistance));
        priorityQueue.addAll(toList());
        return priorityQueue;
    }

    public Vertex getPredecessorVertexOf(Vertex vertex) {
        return predecessors.get(vertex);
    }

    public Predecessor getPredecessorOf(Vertex vertex) {
        return Predecessor.of(vertex, predecessors.get(vertex), distances.get(vertex));
    }

    public Path findPathTo(final Vertex vertex) {
        final var vertices = buildPath(List.of(vertex));
        return Path.of(vertices, distances.get(vertices.get(vertices.size() - 1)));
    }

    private List<Vertex> buildPath(List<Vertex> vertices) {
        if (!vertices.isEmpty()) {
            final var firstVertex = vertices.get(0);
            final var previousVertex = getPredecessorVertexOf(firstVertex);

            if (previousVertex != null) {
                final var newVertices = new ArrayList<Vertex>();
                newVertices.add(previousVertex);
                newVertices.addAll(vertices);
                return buildPath(newVertices);
            }
        }

        return vertices;
    }

    @RequiredArgsConstructor(access = PRIVATE)
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Predecessor {

        private final Vertex vertex;
        private final Vertex predecessor;
        private final int distance;

        public static Predecessor of(Vertex vertex, Vertex predecessor) {
            return new Predecessor(vertex, predecessor, -1);
        }

        public static Predecessor of(Vertex vertex, Vertex predecessor, int distance) {
            return new Predecessor(vertex, predecessor, distance);
        }
    }

}
