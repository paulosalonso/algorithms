package com.github.paulosalonso.algorithms.graph;

import com.github.paulosalonso.algorithms.graph.Graph.Vertex;
import com.github.paulosalonso.algorithms.graph.searcher.*;

import java.util.List;

public class Sample {
    public static void main(String[] args) {
        var v0 = Vertex.of(0);
        var v1 = Vertex.of(1);
        var v2 = Vertex.of(2);
        var v3 = Vertex.of(3);
        var v4 = Vertex.of(4);
        var v5 = Vertex.of(5);
        var v6 = Vertex.of(6);
        var v7 = Vertex.of(7);
        var v8 = Vertex.of(8);
        var v9 = Vertex.of(9);
        var v10 = Vertex.of(10);
        var v11 = Vertex.of(11);
        var v12 = Vertex.of(12);
        var v13 = Vertex.of(13);
        var v14 = Vertex.of(14);
        var v15 = Vertex.of(15);

        var vertices = List.of(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15);

        var graph = Graph.of(vertices);

        graph.joinUndirected(v0, v1, 1)
                .joinUndirected(v1, v2, 7)
                .joinUndirected(v2, v10, 8)
                .joinUndirected(v2, v11, 15)
                .joinUndirected(v1, v3, 21)
                .joinUndirected(v3, v12, 9)
                .joinUndirected(v3, v4, 3)
                .joinUndirected(v4, v13, 7)
                .joinUndirected(v4, v5, 30)
                .joinUndirected(v5, v6, 16)
                .joinUndirected(v5, v9, 4)
                .joinUndirected(v0, v6, 9)
                .joinUndirected(v6, v7, 5)
                .joinUndirected(v7, v9, 40)
                .joinUndirected(v9, v15, 2)
                .joinUndirected(v0, v8, 23)
                .joinUndirected(v8, v7, 17)
                .joinUndirected(v8, v14, 11);

        var start = System.currentTimeMillis();
        var depthFirstSearcher = DepthFirstSearcher.of(graph);
        var depthFirstResult = depthFirstSearcher.search(v0);
        var duration = System.currentTimeMillis() - start;
        System.out.println("DepthFirstSearcher:         " + depthFirstResult.findPathTo(v15) + " | duration: " + duration + "ms");

        start = System.currentTimeMillis();
        var breadthFirstSearcher = BreadthFirstSearcher.of(graph);
        var breadthFirstResult = breadthFirstSearcher.search(v0);
        duration = System.currentTimeMillis() - start;
        System.out.println("BreadthFirstSearcher:       " + breadthFirstResult.findPathTo(v11) + " | duration: " + duration + "ms");

        start = System.currentTimeMillis();
        var dijkstraSearcher = DijkstraSearcher.of(graph);
        var dijkstraResult = dijkstraSearcher.search(v0);
        duration = System.currentTimeMillis() - start;
        System.out.println("DijkstraSearcher:           " + dijkstraResult.findPathTo(v5) + " | duration: " + duration + "ms");

        start = System.currentTimeMillis();
        var denseGraphDijkstraSearcher = DenseGraphDijkstraSearcher.of(graph);
        var denseGraphDijkstraResult = denseGraphDijkstraSearcher.search(v0);
        duration = System.currentTimeMillis() - start;
        System.out.println("DenseGraphDijkstraSearcher: " + denseGraphDijkstraResult.findPathTo(v13) + " | duration: " + duration + "ms");

        start = System.currentTimeMillis();
        var bellmanFordSearcher = BellmanFordSearcher.of(graph);
        var bellmanFordResult = bellmanFordSearcher.search(v0);
        duration = System.currentTimeMillis() - start;
        System.out.println("BellmanFordSearcher:        " + bellmanFordResult.findPathTo(v10) + " | duration: " + duration + "ms");

        start = System.currentTimeMillis();
        var floydWarshallSearcher = FloydWarshallSearcher.of(graph);
        duration = System.currentTimeMillis() - start;
        System.out.println("FloydWarshallSearcher:      " + floydWarshallSearcher.getShortestPath(v0, v15) + " | duration: " + duration + "ms");
    }
}
