package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 29/02/16.
 */
public class InfoGainRatioSplitAttrSelector extends InfoGainSplitAttrSelector {

    public static final double LOG2_2 = Math.log(2.0d);

    @Override
    public Pair<Integer, Map<String, List<Tuple>>> selectSplitAttribute(final Collection<Tuple> tuples, final Integer[] splitAttributeCandidates, final int classAttr) {
        final double entropy = entropy(tuples, classAttr);
        Triplet<Integer, Map<String, List<Tuple>>, Double> triplet = Arrays.asList(splitAttributeCandidates).parallelStream()
                .map(i -> new Pair<>(i, partitionOnAttribute(tuples, i)))
                .map(p -> new Triplet<>(p, gainRatio(tuples, classAttr, entropy, p.getB().values())))
                .max(Comparator.comparingDouble(Triplet::getC))
                .get();
        return new Pair<>(triplet.getA(), triplet.getB());
    }

    double gainRatio(Collection<Tuple> tuples, int classAttr, double entropy, Collection<List<Tuple>> partitions) {
        return informationGain(entropy, tuples, partitions, classAttr) / splitInfo(tuples, partitions, classAttr);
    }

    double splitInfo(final Collection<Tuple> tuples, Collection<List<Tuple>> partitions, final int classAttr) {
        final double initialSize = tuples.size();
        // return the weighted entropy
        return - partitions.parallelStream()
                .map(p -> ((double) p.size()) / initialSize)
                .map(c -> c * Math.log(c) / LOG2_2)
                .collect(Collectors.summingDouble(d -> d));
     }
}
