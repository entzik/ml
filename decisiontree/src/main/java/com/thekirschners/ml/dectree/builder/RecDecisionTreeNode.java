package com.thekirschners.ml.dectree.builder;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Tuple;
import com.thekirschners.ml.dectree.SplitAttributeSelector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class RecDecisionTreeNode implements TreeElement{
    private final int splitAttribute;
    private final Map<String, RecDecisionTreeNode> children;
    private final String classValue;

    public RecDecisionTreeNode(SplitAttributeSelector splitAttributeSelector, List<Tuple> tuples, Integer[] attributes, int classAttribute) {
        if (attributes.length == 0) { // this is a leaf
            // count classes by name
            Map<String, Long> classCountByName = tuples.parallelStream()
                    .map(t -> t.attribute(classAttribute))
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            // apply majority voting
            this.classValue = classCountByName.entrySet().parallelStream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();
            this.children = null;
            this.splitAttribute = -1;
            // TODO calculate confidence as |majority_class| / |tuples|
        } else { // not a leaf
            this.classValue = null;
            this.children = new HashMap<>();
            // retrieve the best attribute to split on and the tuples partitioned by values of the attribute
            if (attributes.length == 1) {
                this.splitAttribute = attributes[0];
                Map<String, List<Tuple>> partitionsByAttribute = tuples.parallelStream().collect(Collectors.groupingBy(t -> t.attribute(splitAttribute)));
                partitionsByAttribute.entrySet().forEach(entry -> children.put(entry.getKey(), new RecDecisionTreeNode(splitAttributeSelector, entry.getValue(), new Integer[0], classAttribute)));
            } else {
                Pair<Integer, Map<String, List<Tuple>>> attributeSelectionResult = splitAttributeSelector.selectSplitAttribute(tuples, attributes, classAttribute);
                this.splitAttribute = attributeSelectionResult.getA();
                Map<String, List<Tuple>> partitionsByAttribute = attributeSelectionResult.getB();

                // substract the selected attribute
                Integer[] remainingAttr = new Integer[attributes.length - 1];
                int runner = 0;
                for (Integer ndx : attributes) {
                    if (!ndx.equals(splitAttribute)) {
                        remainingAttr[runner] = ndx;
                        runner ++;
                    }
                }

                // create children by further splitting the partitions
                partitionsByAttribute.entrySet().forEach(entry -> children.put(entry.getKey(), new RecDecisionTreeNode(splitAttributeSelector, entry.getValue(), remainingAttr, classAttribute)));
            }
        }
    }

    @Override
    public boolean isLeaf() {
        return classValue != null;
    }

    public String predict(Tuple t) {
        if (classValue != null)
            return classValue;
        else
            return children.get(t.attribute(splitAttribute)).predict(t);
    }

    @Override
    public Collection<TreeElement> children() {
        return children == null ? new ArrayList<>() : new ArrayList<>(children.values());
    }

    @Override
    public String toString() {
        if (isLeaf())
            return classValue;
        else
            return "-" + splitAttribute;
    }
}
