package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * selects the best attribute to split a set of tuples on based in information gain metrics
 */
public class InfoGainSplitAttrSelector implements SplitAttributeSelector {


    /**
     * calculates infromation gain for each individual attribute and maximizes it
     *
     * @param tuples the tuples we need to split
     * @param splitAttributeCandidates split attribute candidates
     * @param classAttr the class attribute
     * @return a pair containing the selected split attribute in the first slot and the set of subpartitions indexed by attribute value in the second slot
     */
    @Override
    public Pair<Integer, Map<Object, List<Tuple>>> selectSplitAttribute(final Collection<Tuple> tuples, final Integer[] splitAttributeCandidates, final int classAttr) {
        final double entropy = entropy(tuples, classAttr);
        final double size = tuples.size();

        Triplet<Integer, Double, Map<Object, List<Tuple>>> triplet = Arrays.asList(splitAttributeCandidates).parallelStream()
                .map(i -> {
                    Map<Object, List<Tuple>> partitions = partitionOnAttribute(tuples, i);
                    return new Triplet<>(i, informationGain(entropy, size, partitions.values(), classAttr), partitions);
                })
                .max(Comparator.comparingDouble((java.util.function.ToDoubleFunction<Triplet<Integer, Double, Map<Object, List<Tuple>>>>) (integerDoubleMapTriplet) -> {
                    Double b = integerDoubleMapTriplet.getB();
                    return b;
                }))
                .get();
        Map<Object, List<Tuple>> partitions = triplet.getC();
        return new Pair<>(triplet.getA(), partitions);
    }

    /**
     * <p>the entropy of a data set with respect to a class attribute C is calculated as -sum{i -> 1..n}(p[i] * log2[p_i], where
     * n is the number of distinct values of the class attribute C and p[i] is the probability of a tuple to belong to class C[i].
     * if count[i] is the number of tuples tha belong to class C[i] and count_total is the total number of tuples in
     * the training set, then p[i] = count[i[ / total_count</p>
     *
     * <p>this value is generally known in literature as info_D - where D is the set of tuples in the training data set.
     * It represents the amount of information required to classify a tuple in the data set, measured in bits</p>
     *
     * @param tuples - the training data set
     * @param classAttr - the class attribute
     * @return the value of the entropy, measured in bits
     */
    double entropy(Collection<Tuple> tuples, int classAttr) {
        final double tupleCount = (double) tuples.size();
        // calculate the cardinalities of each class value, that is, the number of tuples that belongs to each class
        Collection<Long> classCardinalities = tuples.parallelStream()
                .map(t -> t.attribute(classAttr))
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .values();
        return - classCardinalities.stream()
                .map(count -> ((double) count) / tupleCount) // probability for a tuple to belong to this class
                .collect(Collectors.summingDouble(probability -> probability * MathTools.log2(probability)));
    }

    /**
     * <p>split entropy represents the amount of information still needed to classify a tuple after partitioning the training
     * data set D on a specified attribute in N partitions D[1[...D[N]. It is calculated as sum{j -> i..N}w[i]*entropy(D[i]), where n is the number of
     * distinct values of the splitting attribute and w[i] is the weight of partition D[i] calculated as count_D[i] / count_D
     *</p>
     *
     * <p>this value is generally known in literature as info_A_D where A is the attribute on which we partition.</p>
     *
     * @param initialSize the number of tuples we are looking to partition
     * @param partitions - tuples paritioned on attribute D
     * @param classAttr the class attribute
     * @return the split value entropy in buts
     */
    double splitEntropy(final double initialSize, final Collection<List<Tuple>> partitions, final int classAttr) {
        // return the weighted entropy
        return partitions.parallelStream().collect(Collectors.summingDouble(p -> entropy(p, classAttr) * ((double) p.size()) / initialSize));
    }

    /**
     * partition tuples based on a attribute
     * @param tuples tuples to be partitioned
     * @param splitAtribute the partitioning attribute
     * @return partitioned tuples
     */
    Map<Object,List<Tuple>> partitionOnAttribute(Collection<Tuple> tuples, int splitAtribute) {
        return tuples.parallelStream().collect(Collectors.groupingBy(t -> t.attribute(splitAtribute)));
    }

    /**
     * calculates the information gain obtained by partitioning a set of tuples on a specified attribute. calculations of
     * entropy and the partitions obtained from partitioning on the selected attribute are performed upstream.
     *
     * @param entropy the entropy if the tuple set
     * @param initialSize the number of tuples we are looking to partition
     * @param partitions tuples partitioned on attribute
     * @param classAttr the class attribute
     * @return the information gain
     */
    double informationGain(double entropy, final double initialSize, final Collection<List<Tuple>> partitions, final int classAttr) {
        return  entropy - splitEntropy(initialSize, partitions, classAttr);
    }


}
