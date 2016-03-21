package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * information gain ratio is based in standard information gain with the property of adjusting against biasing towards selecting attributes with large numbers of distinct values
 */
public class InfoGainRatioSplitAttrSelector extends InfoGainSplitAttrSelector {
    /**
     * selects the best attribute to split on by selecting the one with the highest gain ratio
     * @param tuples the tuples we need to split
     * @param splitAttributeCandidates split attribute candidates
     * @param classAttr the class attribute
     * @return a pair containing the selected split attribute in the first slot and the set of subpartitions indexed by attribute value in the second slot
     */
    @Override
    public Pair<Integer, Map<Object, List<Tuple>>> selectSplitAttribute(final Collection<Tuple> tuples, final Integer[] splitAttributeCandidates, final int classAttr) {
        final double entropy = entropy(tuples, classAttr);
        final double initialSize = tuples.size();
        Triplet<Integer, Map<Object, List<Tuple>>, Double> triplet = Arrays.asList(splitAttributeCandidates).parallelStream()
                .map(i -> new Pair<>(i, partitionOnAttribute(tuples, i)))
                .map(p -> new Triplet<>(p, gainRatio(initialSize, classAttr, entropy, p.getB().values())))
                .max(Comparator.comparingDouble(Triplet::getC))
                .get();
        return new Pair<>(triplet.getA(), triplet.getB());
    }

    /**
     * calculate gain ratio for splitting on an attribute by dividing information gain by the splitInfo measure
     * @param initialSize number of tuples to be partitioned
     * @param classAttr  the class attribute
     * @param entropy pre-calculated entropy of the tuples
     * @param partitions partitions obtained after splitting on a specific attribute
     * @return
     */
    double gainRatio(final double initialSize, int classAttr, double entropy, Collection<List<Tuple>> partitions) {
        return informationGain(entropy, initialSize, partitions, classAttr) / splitInfo(initialSize, partitions);
    }

    /**
     * split information is calculated as sum{i -> 1..N}w[i]*log2(p[i]), N is the number of partitions andwhere w[i]
     * is the weight of a partition calculated as: w[i] = partition_size[i] / size_tuples.
     *
     * @param initialSize number of tuples to be partitioned
     * @param partitions partitioned tuples
     * @return
     */
    double splitInfo(final double initialSize, Collection<List<Tuple>> partitions) {
        // return the weighted entropy
        return - partitions.parallelStream()
                .map(p -> ((double) p.size()) / initialSize)
                .map(c -> c * MathTools.log2(c))
                .collect(Collectors.summingDouble(d -> d));
     }
}
