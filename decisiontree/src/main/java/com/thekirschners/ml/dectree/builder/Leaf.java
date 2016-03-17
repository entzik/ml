package com.thekirschners.ml.dectree.builder;

import com.thekirschners.ml.data.Tuple;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by emilkirschner on 16/03/16.
 */
public class Leaf implements TreeElement {
    final Object classValue;

    public Leaf(Object classValue) {
        this.classValue = classValue;
    }

    @Override
    public Object predict(Tuple t) {
        return classValue;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Collection<TreeElement> children() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return classValue.toString();
    }
}
