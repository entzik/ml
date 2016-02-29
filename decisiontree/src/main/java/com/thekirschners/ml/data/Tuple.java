package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class Tuple {
    final private String[] attributes;

    public Tuple(String[] attributes) {
        this.attributes = attributes;
    }

    public int attributeCount() {
        return attributes.length;
    }

    public String attribute(int i) {
        return attributes[i];
    }
}
