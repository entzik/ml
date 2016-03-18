package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Tuple;

import java.util.*;

/**
 * Created by emilkirschner on 16/03/16.
 */
public class ParentNode implements TreeNode {
    private Map<Object, TreeNode> children;
    private int splitAttribute;

    public ParentNode() {
        children = Collections.emptyMap();
    }

    public void setChildren(int splitAttr, Map<Object, TreeNode> children) {
        this.splitAttribute = splitAttr;
        this.children = Collections.unmodifiableMap(children);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public TreeNode getChild(Tuple t) {
        Object attribute = t.attribute(splitAttribute);
        return children.get(attribute);
    }

}
