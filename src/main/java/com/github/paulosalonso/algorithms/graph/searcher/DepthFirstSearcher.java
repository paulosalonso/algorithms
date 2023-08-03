package com.github.paulosalonso.algorithms.graph.searcher;

import com.github.paulosalonso.algorithms.graph.Graph;
import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.github.paulosalonso.algorithms.graph.searcher.VertexStatus.*;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class DepthFirstSearcher {

    private final Graph graph;

    public static DepthFirstSearcher of(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must be not null");
        }

        return new DepthFirstSearcher(graph);
    }

    public Predecessors search(Vertex source) {
        var predecessors = Predecessors.newInstance();
        var verticesStatus = graph.getVertices().stream()
                .collect(toMap(vertex -> vertex, vertex -> UNVISITED));

        visit(graph, source, predecessors, verticesStatus);

        return predecessors;
    }

    private void visit(Graph graph, Vertex source, Predecessors predecessors, Map<Vertex, VertexStatus> verticesStatus) {
        verticesStatus.put(source, VISITED);

        for (var neighbor : graph.getNeighbors(source)) {
            if (verticesStatus.get(neighbor).equals(UNVISITED)) {
                predecessors.put(neighbor, source);
                visit(graph, neighbor, predecessors, verticesStatus);
            }
        }

        verticesStatus.put(source, DONE);
    }
}
