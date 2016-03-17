package com.thekirschners.ml.decisiontree;


import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Triplet;
import com.thekirschners.ml.data.Tuple;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public interface TreeNode {
    public Object predict(Tuple t);
    public boolean isLeaf();
    public Collection<TreeNode> children();

    public static class Printer {
        public static void print(TreeNode element, String s, PrintStream pw) {
            pw.println(s + element);
            element.children().forEach(te -> print(te, s + "\t", pw));
        }
    }

    public static class Builder {
        public static TreeNode build(SplitAttributeSelector splitAttributeSelector, List<Tuple> data, Integer[] attributes, int classAttribute) {
            ParentNode rootNode = new ParentNode();
            Queue<Triplet<ParentNode, List<Tuple>, Integer[]>> workSleeve = new ArrayDeque<>();
            workSleeve.add(new Triplet<>(rootNode, data, attributes));

            while (!workSleeve.isEmpty()) {
                Triplet<ParentNode, List<Tuple>, Integer[]> pop = workSleeve.poll();
                ParentNode node = pop.getA();
                List<Tuple> partition = pop.getB();
                Integer[] crtAttributes = pop.getC();

                switch (crtAttributes.length) {
                    case 1: {
                        node.setChildren(crtAttributes[0], getObjectTreeElementMap(classAttribute, crtAttributes[0], partition));
                    }
                    break;

                    default: {
                        Map<Object, TreeNode> children = new HashMap<>();

                        Pair<Integer, Map<String, List<Tuple>>> splitAttrAndPartitions = splitAttributeSelector.selectSplitAttribute(partition, crtAttributes, classAttribute);
                        int splitAttr = splitAttrAndPartitions.getA();
                        Map<String, List<Tuple>> partitionsByAttr = splitAttrAndPartitions.getB();

                        Integer[] remainingAttr = removeValue(crtAttributes, splitAttr);

                        for (Map.Entry<String, List<Tuple>> entry : partitionsByAttr.entrySet()) {
                            ParentNode child = new ParentNode();
                            children.put(entry.getKey(), child);
                            workSleeve.add(new Triplet<>(child, entry.getValue(), remainingAttr));
                        }

                        node.setChildren(splitAttr, children);
                    }
                }


            }

            return rootNode;
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

        private static Map<Object, TreeNode> getObjectTreeElementMap(int classAttribute, Integer crtAttribute, List<Tuple> partition) {
            Map<Object, TreeNode> children = new HashMap<>();
            Map<String, List<Tuple>> partitions = partition.parallelStream().collect(Collectors.groupingBy(tuple -> tuple.attribute(crtAttribute)));
            for (Map.Entry<String, List<Tuple>> entry : partitions.entrySet()) {
                Map<String, Long> classCountByName = entry.getValue().parallelStream()
                        .map(t -> t.attribute(classAttribute))
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
                // apply majority voting
                String votedClass = classCountByName.entrySet().parallelStream().max(Comparator.comparingLong(Map.Entry::getValue)).get().getKey();

                Leaf child = new Leaf(votedClass);
                children.put(entry.getKey(), child);
            }
            return children;
        }
    }

}