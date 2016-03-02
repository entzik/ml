package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 29/02/16.
 */
public class InformationGainSplitAttributeSelector implements SplitAttributeSelector {
    @Override
    public int selectSplitAttribute(final Collection<Tuple> tuples, final int[] splitAttributeCandidates, final int classAttr) {
        Double max = Double.MIN_VALUE;
        int selectedSplitCandidate = -1;
        for (int splitAttributeCandidate : splitAttributeCandidates) {
            double gain = gain(tuples, splitAttributeCandidate, classAttr);
            if (gain > max) {
                max = gain;
                selectedSplitCandidate = splitAttributeCandidate;
            }
        }

        return selectedSplitCandidate;
    }

    private double entropy(Collection<Tuple> tuples, int classAttr) {
        final double tupleCount = (double) tuples.size();
        Collection<Long> classCardinalities = tuples.parallelStream()
                .map(t -> t.attribute(classAttr))
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .values();
        return - classCardinalities.stream()
                .map(count -> ((double) count) / tupleCount) // probability for a tuple to belong to this class
                .collect(Collectors.summingDouble(probability -> probability * Math.log(probability)));
    }

    private double splitEntropy(final Collection<Tuple> tuples, final int splitAtribute, final int classAttr) {
        final double initialSize = tuples.size();
        // partition the touples on provided split attribute
        final Collection<List<Tuple>> collect = tuples.parallelStream().collect(Collectors.groupingBy(t -> t.attribute(splitAtribute))).values();
        // return the weighted entropy
        return collect.parallelStream().collect(Collectors.summingDouble(p -> entropy(p, classAttr) * ((double) p.size()) / initialSize));
    }

    public double gain(final Collection<Tuple> tuples, final int splitAtribute, final int classAttr) {
        return entropy(tuples, classAttr) - splitEntropy(tuples, splitAtribute, classAttr);
    }
}
