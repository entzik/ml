package com.thekirschners.ml.decisiontree;


import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * defines the basic interface of a generic decision tree node.
 */
public interface TreeNode {
    /**
     * returns true if this node is a leaf node (holding the class to which the tree thinks the tuple belongs)
     * @return true of this node is a leaf
     */
    boolean isLeaf();

    /**
     * gets the child towards which the specified tuple will be routed during the decision process - only supported for non-leaf nodes
     * @param t the tuple to be routed
     * @return the next node in the decision path
     */
    default TreeNode getChild(Tuple t) {
        throw new IllegalStateException(this.getClass().getName() + " instances do not have children");
    }

    /**
     * returns this node's classification label - only supported for leaf nodes
     * @return the classification object
     */
    default Object getClassification() {
        throw new IllegalStateException(this.getClass().getName() + " instances do not store the class, you must get to a leaf first");
    }

    /**
     * DSL namespace interface
     */
    interface Tools {
        /**
         * iterative algorithm used to predict the class label of a tuple
         */
        class Oracle {
            public static Object predict(TreeNode root, Tuple t) {
                TreeNode crt = root;
                while (!crt.isLeaf())
                    crt = crt.getChild(t);
                return crt.getClassification();
            }
        }

        /**
         * implements an iterative algorithm to build a decision tree using information gain or information gain ratio attribute selection criteria
         */
        class Builder {
            final SplitAttributeSelector selector;
            final NodeBuilder nodeBuilder;

            private Builder(SplitAttributeSelector selector) {
                this.selector = selector;
                this.nodeBuilder = new IterativeBuilder(selector);
            }

            public static Builder selector(SplitAttributeSelector selector) {
                return new Builder(selector);
            }

            public TreeNode build(List<Tuple> tuples, Integer[] attributes, int classAttribute) {
                return nodeBuilder.build(tuples, attributes, classAttribute);
            }

            interface NodeBuilder {
                TreeNode build(List<Tuple> tuples, Integer[] attributes, int classAttribute);
            }

            static class IterativeBuilder implements NodeBuilder {
                private final SplitAttributeSelector selector;

                public IterativeBuilder(SplitAttributeSelector selector) {
                    this.selector = selector;
                }

                public TreeNode build(List<Tuple> data, Integer[] attributes, int classAttribute) {
                    final ParentNode rootNode = new ParentNode();
                    final Queue<Triplet<ParentNode, List<Tuple>, Integer[]>> workSleeve = new ArrayDeque<>();
                    workSleeve.add(new Triplet<>(rootNode, data, attributes));

                    while (!workSleeve.isEmpty()) {
                        final Triplet<ParentNode, List<Tuple>, Integer[]> pop = workSleeve.poll();
                        final ParentNode node = pop.getA();
                        final List<Tuple> partition = pop.getB();
                        final Integer[] crtAttributes = pop.getC();

                        Pair<Integer, Map<Object, TreeNode>> children =
                                crtAttributes.length == 1 ?
                                        buildLeafs(partition, classAttribute, crtAttributes[0]) :
                                        buildParentNodes(partition, classAttribute, workSleeve, crtAttributes, selector);
                        node.setChildren(children.getA(), children.getB());
                    }

                    return rootNode;
                }

                private static Pair<Integer, Map<Object, TreeNode>> buildParentNodes(List<Tuple> partition, int classAttribute, Queue<Triplet<ParentNode, List<Tuple>, Integer[]>> workSleeve, Integer[] crtAttributes, SplitAttributeSelector splitAttributeSelector) {
                    final Map<Object, TreeNode> children = new HashMap<>();

                    final Pair<Integer, Map<Object, List<Tuple>>> splitAttrAndPartitions = splitAttributeSelector.selectSplitAttribute(partition, crtAttributes, classAttribute);
                    final int splitAttr = splitAttrAndPartitions.getA();
                    final Map<Object, List<Tuple>> partitionsByAttr = splitAttrAndPartitions.getB();

                    final Integer[] remainingAttr = removeValue(crtAttributes, splitAttr);

                    for (Map.Entry<Object, List<Tuple>> entry : partitionsByAttr.entrySet()) {
                        final ParentNode child = new ParentNode();
                        children.put(entry.getKey(), child);
                        workSleeve.add(new Triplet<>(child, entry.getValue(), remainingAttr));
                    }

                    return new Pair<>(splitAttr, children);
                }

                private static Integer[] removeValue(Integer[] values, int toRemove) {
                    Integer[] remainingAttr = new Integer[values.length - 1];
                    int runner = 0;
                    for (Integer ndx : values) {
                        if (!ndx.equals(toRemove)) {
                            remainingAttr[runner] = ndx;
                            runner++;
                        }
                    }
                    return remainingAttr;
                }

                private static Pair<Integer, Map<Object, TreeNode>> buildLeafs(List<Tuple> partition, int classAttribute, Integer splitAtribute) {
                    Map<Object, TreeNode> children = new HashMap<>();
                    Map<Object, List<Tuple>> partitions = partition.parallelStream().collect(Collectors.groupingBy(tuple -> tuple.attribute(splitAtribute)));
                    for (Map.Entry<Object, List<Tuple>> entry : partitions.entrySet()) {
                        Map<Object, Long> classCountByName = entry.getValue().parallelStream()
                                .map(t -> t.attribute(classAttribute))
                                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
                        // apply majority voting
                        Object votedClass = classCountByName.entrySet().parallelStream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();

                        Leaf child = new Leaf(votedClass);
                        children.put(entry.getKey(), child);
                    }
                    return new Pair<>(splitAtribute, children);
                }
            }

        }
    }
}
