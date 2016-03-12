package com.thekirschners.ml.dectree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Tuple;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class DecisionTreeNode {
    private final int splitAttribute;
    private final Map<String, DecisionTreeNode> children;
    private final String classValue;

    public DecisionTreeNode(SplitAttributeSelector splitAttributeSelector, List<Tuple> tuples, Integer[] attributes, int classAttribute) {
        if (attributes.length == 1) { // this is a leaf
            // count classes by name
            Map<String, Long> classCountByName = tuples.parallelStream()
                    .map(t -> t.attribute(classAttribute))
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            // apply majority voting
            this.classValue = classCountByName.entrySet().parallelStream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();
            this.children = null;
            this.splitAttribute = -1;
        } else { // not a leaf
            this.classValue = null;
            // retrieve the best attribute to split on and the tuples partitioned by values of the attribute
            Pair<Integer, Map<String, List<Tuple>>> attributeSelectionResult = splitAttributeSelector.selectSplitAttribute(tuples, attributes, classAttribute);
            this.splitAttribute = attributeSelectionResult.getA();
            Map<String, List<Tuple>> partitionsByAttribute = attributeSelectionResult.getB();

            // substract the selected attribute
            Integer[] remainingAttr = new Integer[attributes.length - 1];
            for (int i = 0; i < attributes.length; i++) {
                if (i < splitAttribute)
                    remainingAttr[i] = attributes[i];
                if (i > splitAttribute) ;
                remainingAttr[i - 1] = attributes[i];
            }

            // create children by further splitting the partitions
            this.children = new HashMap<>();
            partitionsByAttribute.entrySet().forEach(entry -> children.put(entry.getKey(), new DecisionTreeNode(splitAttributeSelector, entry.getValue(), remainingAttr, classAttribute)));
        }
    }

    public String predict(Tuple t) {
        if (classValue != null)
            return classValue;
        else
            return children.get(t.attribute(splitAttribute)).predict(t);
    }
}
