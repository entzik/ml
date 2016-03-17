package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 29/02/16.
 */
public class InfoGainSplitAttrSelector implements SplitAttributeSelector {

    public static final double LOG2_2 = Math.log(2.0d);

    @Override
    public Pair<Integer, Map<String, List<Tuple>>> selectSplitAttribute(final Collection<Tuple> tuples, final Integer[] splitAttributeCandidates, final int classAttr) {
        final double entropy = entropy(tuples, classAttr);

        Triplet<Integer, Double, Map<String, List<Tuple>>> triplet = Arrays.asList(splitAttributeCandidates).parallelStream()
                .map(i -> {
                    Map<String, List<Tuple>> partitions = partitionOnAttribute(tuples, i);
                    return new Triplet<>(i, informationGain(entropy, tuples, partitions.values(), classAttr), partitions);
                })
                .max(Comparator.comparingDouble((java.util.function.ToDoubleFunction<Triplet<Integer, Double, Map<String, List<Tuple>>>>) (integerDoubleMapTriplet) -> {
                    Double b = integerDoubleMapTriplet.getB();
                    return b;
                }))
                .get();
        Map<String, List<Tuple>> partitions = triplet.getC();
        return new Pair<>(triplet.getA(), partitions);
    }

    double entropy(Collection<Tuple> tuples, int classAttr) {
        final double tupleCount = (double) tuples.size();
        Collection<Long> classCardinalities = tuples.parallelStream()
                .map(t -> t.attribute(classAttr))
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .values();
        return - classCardinalities.stream()
                .map(count -> ((double) count) / tupleCount) // probability for a tuple to belong to this class
                .collect(Collectors.summingDouble(probability -> probability * Math.log(probability) / LOG2_2));
    }

     double splitEntropy(final Collection<Tuple> tuples, final Collection<List<Tuple>> partitions, final int classAttr) {
        final double initialSize = tuples.size();
        // return the weighted entropy
        return partitions.parallelStream().collect(Collectors.summingDouble(p -> entropy(p, classAttr) * ((double) p.size()) / initialSize));
    }

    Map<String,List<Tuple>> partitionOnAttribute(Collection<Tuple> tuples, int splitAtribute) {
        return tuples.parallelStream().collect(Collectors.groupingBy(t -> t.attribute(splitAtribute)));
    }

    double informationGain(double entropy, final Collection<Tuple> tuples, final Collection<List<Tuple>> partitions, final int classAttr) {
        return  entropy - splitEntropy(tuples, partitions, classAttr);
    }


}
