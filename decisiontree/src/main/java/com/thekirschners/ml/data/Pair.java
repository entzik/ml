package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 05/03/16.
 */
public class Pair<A,B> {
    final A a;
    final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
