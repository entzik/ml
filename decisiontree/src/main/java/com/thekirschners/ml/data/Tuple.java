package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class Tuple {
    final private String[] attributes;
    final private String[] classAttributes;

    public Tuple(String[] attributes, String[] classAttributes) {
        this.attributes = attributes;
        this.classAttributes = classAttributes;
    }

    public int attributeCount() {
        return attributes.length;
    }

    public int classAttributeCount() {
        return attributes.length;
    }

    public String attribute(int i) {
        return attributes[i];
    }

    public String classAttribute(int i) {
        return classAttributes[i];
    }

}
