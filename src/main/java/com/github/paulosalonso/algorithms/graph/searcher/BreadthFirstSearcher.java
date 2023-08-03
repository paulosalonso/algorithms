package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import lombok.RequiredArgsConstructor;

import java.util.ArrayDeque;

import static com.github.paulosalonso.algorithms.graph.searcher.VertexStatus.*;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class BreadthFirstSearcher {

    private final Graph graph;

    public static BreadthFirstSearcher of(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new BreadthFirstSearcher(graph);
    }

    public Predecessors search(Vertex source) {
        var predecessors = Predecessors.newInstance();
        var statuses = graph.getVertices().stream()
                .collect(toMap(vertex -> vertex, vertex -> UNVISITED));

        final var queue = new ArrayDeque<Vertex>();
        queue.offer(source);

        statuses.put(source, VISITED);

        while (!queue.isEmpty()) {
            var currentVertex = queue.poll();

            for (var neighbor : graph.getNeighbors(currentVertex)) {
                if (statuses.get(neighbor).equals(UNVISITED)) {
                    predecessors.put(neighbor, currentVertex);
                    statuses.put(neighbor, VISITED);
                    queue.offer(neighbor);
                }
            }

            statuses.put(currentVertex, DONE);
        }

        return predecessors;
    }
}
