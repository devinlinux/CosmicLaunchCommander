package com.michaelb.clc.sci.units.graph;

//  imports
import java.util.List;

public abstract class WeightedDiGraph<T> {

    private List<Node<T>> nodes;

    public record Node<T>(T label, List<WeightedDiEdge<T>> edges) {}
    public record WeightedDiEdge<T>(Node<T> to, double weight) {}
}
