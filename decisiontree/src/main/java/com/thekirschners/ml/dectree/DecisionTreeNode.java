package com.thekirschners.ml.dectree;

import com.thekirschners.ml.data.Tuple;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class DecisionTreeNode {
    final int attributeNdx;
    final String classAttribute;
    final Map<String, DecisionTreeNode> children;

    public DecisionTreeNode(int attributeNdx, String classAttribute, HashMap<String, DecisionTreeNode> children) {
        this.attributeNdx = attributeNdx;
        this.classAttribute = classAttribute;
        this.children = Collections.unmodifiableMap(children);
    }

    public String predict(Tuple t) {
        if (classAttribute != null)
            return classAttribute;
        else
            return children.get(t.attribute(attributeNdx)).predict(t);
    }
}
