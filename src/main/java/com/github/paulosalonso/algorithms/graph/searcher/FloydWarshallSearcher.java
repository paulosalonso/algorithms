package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Edge;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import com.github.paulosalonso.algorithms.graph.Path;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshallSearcher {

    private final Graph graph;
    private final long[][] dist;
    private final int[][] pred;

    private FloydWarshallSearcher(final Graph graph) {
        this.graph = graph;
        final var n = graph.getVertices().size();
        this.dist = new long[n][n];
        this.pred = new int[n][n];
        calculateAllPairsShortestPath();
    }

    public static FloydWarshallSearcher of(final Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new FloydWarshallSearcher(graph);
    }

    public Path getShortestPath(final Vertex from, final Vertex to) {
        final var fromIndex = graph.indexOf(from);
        final var toIndex = graph.indexOf(to);

        final var vertices = new ArrayList<Vertex>();
        vertices.add(to);

        Vertex predecessor = to;
        int predecessorIndex = toIndex;

        while (predecessor != from) {
            predecessorIndex = pred[fromIndex][predecessorIndex];
            predecessor = graph.getVertices().get(predecessorIndex);
            vertices.add(0, predecessor);
        }

        return Path.of(vertices, (int) dist[fromIndex][toIndex]);
    }

    private void calculateAllPairsShortestPath() {
        final var vertices = graph.getVertices();
        final var n = vertices.size();

        for (var u = 0; u < n; u++) {
            for (var v = 0; v < n; v++) {
                dist[u][v] = Integer.MAX_VALUE;
                pred[u][v] = -1;
            }

            dist[u][u] = 0;

            final var neighborEdges = graph.getForwardEdges(vertices.get(u));

            for (final Edge neighborEdge : neighborEdges) {
                final var neighborVertex = neighborEdge.getTo();
                final var v = vertices.indexOf(neighborVertex);

                dist[u][v] = neighborEdge.getDistance();
                pred[u][v] = u;
            }
        }

        for (var k = 0; k < n; k++) {
            for (var u = 0; u < n; u++) {
                for (var v = 0; v < n; v++) {
                    final var newDistance = dist[u][k] + dist[k][v];

                    if (newDistance < dist[u][v]) {
                        dist[u][v] = newDistance;
                        pred[u][v] = pred[k][v];
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        var v0 = Vertex.of(0);
        var v1 = Vertex.of(1);
        var v2 = Vertex.of(2);
        var v3 = Vertex.of(3);
        var v4 = Vertex.of(4);

        var vertices = List.of(v0, v1, v2, v3, v4);

        var graph = Graph.of(vertices);

        graph.joinDirected(v0, v1, 2)
                .joinDirected(v1, v2, 3)
                .joinDirected(v2, v3, 5)
                .joinDirected(v3, v0, 8)
                .joinDirected(v0, v4, 4)
                .joinDirected(v2, v4, 1)
                .joinDirected(v4, v3, 7);

        final var floydWarshallSearcher = FloydWarshallSearcher.of(graph);

        System.out.println(floydWarshallSearcher.getShortestPath(v1, v0));
    }
}
