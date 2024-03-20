package com.michaelb.clc.sci.units.graph;

//  imports
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.Path;
import java.nio.file.Files;

public abstract class WeightedDiGraph<T> {

    private final List<Node<T>> nodes;
    private int numNodes;

    private WeightedDiGraph() {
        this.nodes = new ArrayList<>();
        this.numNodes = 0;
    }

    public WeightedDiGraph(T... labels) {
        this.nodes = Arrays.stream(labels)
            .map(label -> new Node<T>(label, List.of()))
            .toList();
        this.numNodes = labels.length;
    }

    public static final record Node<T>(T label, List<WeightedDiEdge<T>> edges) {
        public static <T> Node<T> fromComponents(String[] components) {
            Arrays.stream(components)
                .skip(1)
                .skip(components.length - 1)
                .forEach()
        }
    }
    
    public static final record WeightedDiEdge<T>(Node<T> to, double weight) {}

    public abstract List<WeightedDiEdge<T>> computePathBetween(T from, T to);
    public abstract List<Double> computeWeightsBetween(T from, T to);

    public abstract void write(String path);
    
    public static <T> List<T> read(String path) {
        List<Node<T>> labels = Files.lines(Path.of(path))
        .map(line -> line.split("::"))
        .forEach(components -> 
            Node.fromComponents(components)
        )
        .toList();
    }
}
