package com.thekirschners.ml.decisiontree;


public class Leaf implements TreeNode {
    final Object classValue;

    public Leaf(Object classValue) {
        this.classValue = classValue;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Object getClassification() {
        return classValue;
    }
}
