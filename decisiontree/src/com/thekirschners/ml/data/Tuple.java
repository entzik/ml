package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 26/02/16.
 */
public class Tuple {
    final private String[] attributes;
    final private String classAttribute;

    public Tuple(String[] attributes, String classAttribute) {
        this.attributes = attributes;
        this.classAttribute = classAttribute;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public String getClassAttribute() {
        return classAttribute;
    }
}
