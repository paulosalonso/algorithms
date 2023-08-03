package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class BellmanFordSearcher {

    private final Graph graph;

    public static BellmanFordSearcher of(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new BellmanFordSearcher(graph);
    }

    public Predecessors search(Vertex source) {
        final var distances = new HashMap<Vertex, Integer>();
        final var predecessors = new HashMap<Vertex, Vertex>();


        for (final var vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
        }

        distances.put(source, 0);

        for (var vertex : graph.getVertices()) {
            var leaveEarly = true;
            final var neighborsEdges = graph.getForwardEdges(vertex);

            for (var neighborEdge : neighborsEdges) {
                final var neighborVertex = neighborEdge.getTo();
                final var newDistance = distances.get(vertex) + neighborEdge.getDistance();

                if (newDistance < distances.get(neighborVertex)) {
                    final var vertices = graph.getVertices();
                    final var lastVertex = vertices.get(vertices.size() - 1);

                    if (vertex.equals(lastVertex)) {
                        throw new IllegalStateException("Graph has a negative cycle");
                    }

                    distances.put(neighborVertex, newDistance);
                    predecessors.put(neighborVertex, vertex);
                    leaveEarly = false;
                }
            }

            if (leaveEarly) {
                break;
            }
        }

        return Predecessors.of(predecessors, distances);
    }

    public static void main(String[] args) {
        var v0 = Vertex.of(0);
        var v1 = Vertex.of(1);
        var v2 = Vertex.of(2);
        var v3 = Vertex.of(3);
        var v4 = Vertex.of(4);
        var v5 = Vertex.of(5);

        var vertices = List.of(v0, v1, v2, v3, v4, v5);

        var graph = Graph.of(vertices);

        graph.joinDirected(v0, v1, 6);
        graph.joinDirected(v0, v2, 8);
        graph.joinDirected(v0, v3, 18);
        graph.joinDirected(v1, v4, 11);
        graph.joinDirected(v2, v3, 9);
        graph.joinDirected(v4, v5, 3);
        graph.joinDirected(v5, v2, 7);
        graph.joinDirected(v5, v3, 4);

        final var start = System.currentTimeMillis();
        final var predecessors = BellmanFordSearcher.of(graph).search(v0);
        final var duration = System.currentTimeMillis() - start;

        System.out.println("BellmanFordSearcher duration: " + duration);
        System.out.println(predecessors.toList());
    }
}
