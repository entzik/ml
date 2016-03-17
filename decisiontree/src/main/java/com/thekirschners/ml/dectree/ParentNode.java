package com.thekirschners.ml.dectree;

import com.thekirschners.ml.data.Tuple;

import java.util.*;

/**
 * Created by emilkirschner on 16/03/16.
 */
public class ParentNode implements TreeNode {
    Map<Object, TreeNode> children;
    int splitAttr;

    public ParentNode() {
        children = Collections.emptyMap();
    }

    public void setChildren(int splitAttr, Map<Object, TreeNode> children) {
        this.splitAttr = splitAttr;
        this.children = Collections.unmodifiableMap(children);
    }

    @Override
    public Object predict(Tuple t) {
        return children.get(t.attribute(splitAttr)).predict(t);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Collection<TreeNode> children() {
        return children.values();
    }

    @Override
    public String toString() {
        return "-" + splitAttr;
    }
}
