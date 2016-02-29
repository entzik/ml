package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Tuple;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 29/02/16.
 */
public class Calculations {
    static class Pair {
        private final String name;
        private final double value;

        public Pair(String name, double value) {
            this.value = value;
            this.name = name;
        }

        public String name() {
            return name;
        }

        public double value() {
            return value();
        }
    }

    public static double entropy(Collection<Tuple> tuples, int classAttr) {
        final double tupleCount = (double) tuples.size();
        Map<String, Double> classProbabilities = tuples.parallelStream()
                .map(t -> new Pair(t.attribute(classAttr), 1 / tupleCount))
                .collect(Collectors.toMap(Pair::name, Pair::value, (p1, p2) -> p1 + p2));
        return classProbabilities.entrySet().stream().
                map(Map.Entry::getValue).map(p -> p * Math.log(p))
                .reduce((a, b) -> a + b)
                .get();
    }
}
