package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PrimSearcher {

    private final Graph graph;
    private final long[] key;
    private final int[] pred;

    public PrimSearcher(Graph graph) {
        this.graph = graph;
        this.key = new long[graph.getVertices().size()];
        this.pred = new int[graph.getVertices().size()];
    }

    public static PrimSearcher of(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new PrimSearcher(graph);
    }

    public Predecessors computeMinimumSpanningTree() {
        for (var i = 0; i < graph.getVertices().size(); i++) {
            key[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        key[0] = 0;

        final var comparator = Comparator.<Vertex, Long>comparing(vertex -> key[graph.indexOf(vertex)]);
        var PQ = new PriorityQueue<>(comparator);
        PQ.addAll(graph.getVertices());

        while(!PQ.isEmpty()) {
            final var u = PQ.poll();

            final var neighborEdges = graph.getForwardEdges(u);

            for (final var edge : neighborEdges) {
                final var neighborVertex = edge.getTo();
                final var neighborIndex = graph.indexOf(neighborVertex);

                if (PQ.contains(neighborVertex)) {
                    final var w = edge.getDistance();

                    if (w < key[neighborIndex]) {
                        pred[neighborIndex] = graph.indexOf(u);
                        key[neighborIndex] = w;

                        final var currentPQ = PQ;
                        PQ = new PriorityQueue<>(comparator);

                        for (Vertex v : currentPQ) {
                            PQ.offer(v);
                        }
                    }
                }
            }
        }

        final var predecessors = Predecessors.newInstance();

        for (var i =0; i < pred.length; i++) {
            final var vertex = graph.getVertices().get(i);
            Vertex predecessor = null;

            if (pred[i] >= 0) {
                predecessor = graph.getVertices().get(pred[i]);
            }

            predecessors.put(vertex, predecessor, (int) key[i]);
        }

        return predecessors;
    }

    public static void main(String[] args) {
        var v0 = Vertex.of(0);
        var v1 = Vertex.of(1);
        var v2 = Vertex.of(2);
        var v3 = Vertex.of(3);
        var v4 = Vertex.of(4);

        var vertices = List.of(v0, v1, v2, v3, v4);

        var graph = Graph.of(vertices);

        graph.joinUndirected(v0, v1, 2)
                .joinUndirected(v1, v2, 3)
                .joinUndirected(v2, v3, 5)
                .joinUndirected(v3, v0, 8)
                .joinUndirected(v0, v4, 4)
                .joinUndirected(v2, v4, 1)
                .joinUndirected(v4, v3, 7);

        final var primSearcher = PrimSearcher.of(graph);
        final var predecessors = primSearcher.computeMinimumSpanningTree();

        System.out.println(predecessors);
    }

}
