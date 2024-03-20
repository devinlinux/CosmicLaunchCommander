package com.michaelb.clc.sci.units.graph;

//  imports
import java.util.Arrays;
import java.util.List;

public abstract class WeightedDiGraph<T> {

    private final List<Node<T>> nodes;
    private int numNodes;

    public WeightedDiGraph(T... labels) {
        this.nodes = Arrays.stream(labels)
            .map(label -> new Node<T>(label, List.of()))
            .toList();
    }

    public static final record Node<T>(T label, List<WeightedDiEdge<T>> edges) {}
    public static final record WeightedDiEdge<T>(Node<T> to, double weight) {}

    public abstract List<WeightedDiEdge<T>> computePathBetween(T from, T to);
    public abstract List<Double> computeWeightsBetween(T from, T to);
}
