package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class DijkstraSearcher {

    private final Graph graph;

    public static DijkstraSearcher of(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new DijkstraSearcher(graph);
    }

    public Predecessors search(Vertex source) {
        final var predecessors = Predecessors.newInstance();

        for (Vertex vertex : graph.getVertices()) {
            if (vertex.equals(source)) {
                predecessors.put(vertex, null, 0);
            } else {
                predecessors.put(vertex, source, Integer.MAX_VALUE);
            }
        }

        final var priorityQueue = predecessors.toPriorityQueue();

        while (!priorityQueue.isEmpty()) {
            final var predecessor = priorityQueue.poll();

            for (var neighborEdge : graph.getForwardEdges(predecessor.getVertex())) {
                final var neighborVertex = neighborEdge.getTo();
                final var neighborPredecessor = predecessors.getPredecessorOf(neighborVertex);
                final var currentDistance = neighborPredecessor.getDistance();
                final var newDistance = predecessor.getDistance() + neighborEdge.getDistance();

                if (newDistance < currentDistance) {
                    predecessors.put(neighborVertex, predecessor.getVertex(), newDistance);

                    priorityQueue.remove(neighborPredecessor);
                    priorityQueue.offer(predecessors.getPredecessorOf(neighborVertex));
                }
            }
        }

        return predecessors;
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
        final var predecessors = DijkstraSearcher.of(graph).search(v0);
        final var duration = System.currentTimeMillis() - start;

        System.out.println("DijkstraSearcher duration: " + duration);
        System.out.println(predecessors.toList());
    }
}
