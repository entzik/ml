package com.thekirschners.ml.dectree.builder;

import com.thekirschners.ml.data.Tuple;

import java.util.*;

/**
 * Created by emilkirschner on 16/03/16.
 */
public class TreeNode implements TreeElement {
    Map<Object, TreeElement> children;
    int splitAttr;

    public TreeNode() {
        children = Collections.emptyMap();
    }

    public void setChildren(int splitAttr, Map<Object, TreeElement> children) {
        this.splitAttr = splitAttr;
        this.children = Collections.unmodifiableMap(children);
    }

    @Override
    public Object predict(Tuple t) {
        return children.get(t.attribute(splitAttr));
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Collection<TreeElement> children() {
        return children.values();
    }

    @Override
    public String toString() {
        return "-" + splitAttr;
    }
}
