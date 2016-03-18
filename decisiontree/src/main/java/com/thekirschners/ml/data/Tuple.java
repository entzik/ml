package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class Tuple {
    final private Object[] attributes;

    public Tuple(Object[] attributes) {
        this.attributes = attributes;
    }

    public int attributeCount() {
        return attributes.length;
    }

    public Object attribute(int i) {
        return attributes[i];
    }
}
