package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Tuple;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 29/02/16.
 */
public class InformationGainSplitAttributeSelector implements SplitAttributeSelector {

    public static final double LOG2_2 = Math.log(2.0d);

    @Override
    public int selectSplitAttribute(final Collection<Tuple> tuples, final Integer[] splitAttributeCandidates, final int classAttr) {
        final double entropy = entropy(tuples, classAttr);
        return Arrays.asList(splitAttributeCandidates).parallelStream()
                .map(i -> new Pair<>(i, informationGain(entropy, tuples, i, classAttr)))
                .reduce((p1, p2) -> p1.getB() > p2.getB() ? p1 : p2)
                .get().getA();

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

     double splitEntropy(final Collection<Tuple> tuples, final int splitAtribute, final int classAttr) {
        final double initialSize = tuples.size();
        // partition the touples on provided split attribute
        final Collection<List<Tuple>> collect = tuples.parallelStream().collect(Collectors.groupingBy(t -> t.attribute(splitAtribute))).values();
        // return the weighted entropy
        return collect.parallelStream().collect(Collectors.summingDouble(p -> entropy(p, classAttr) * ((double) p.size()) / initialSize));
    }

    double informationGain(double entropy, final Collection<Tuple> tuples, final int splitAtribute, final int classAttr) {
        return  entropy - splitEntropy(tuples, splitAtribute, classAttr);
    }
}
