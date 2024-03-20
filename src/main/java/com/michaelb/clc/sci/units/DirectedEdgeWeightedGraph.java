package com.michaelb.clc.sci.units;

//  imports
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.michaelb.clc.util.Logger;

import static com.michaelb.clc.math.MathUtil.inv;

public abstract class DirectedEdgeWeightedGraph<T> {

    public record Node<T>(T label, List<WeightedEdge> edges) {}

    public record WeightedEdge(Node<?> to, double weight) {}

    private final List<Node<T>> nodes;
    private int numNodes;

    @SafeVarargs
    public DirectedEdgeWeightedGraph(final T... nodes) {
        this.nodes = Arrays.stream(nodes)
            .map(label -> new Node<>(label, new ArrayList<>()))
            .toList();
        this.numNodes = nodes.length;
    }

    public void connect(T u, T v, double weight) {

    }

    public void connectWithInverse(T u, T v, double weight) {

    }

    public void from(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            //  TODO: implement reading logic here based on standard file format
        } catch (IOException e) {
            Logger.err("An error occured while attempting to read Graph: %s".formatted(e.getMessage()),
                    "DirectedEdgeWeightedGraph::from");
        }
    }

    public List<WeightedEdge> computePathBetween(Node<T> start, Node<T> end) {
        boolean[] visited = new boolean[this.numNodes];
    }
}
