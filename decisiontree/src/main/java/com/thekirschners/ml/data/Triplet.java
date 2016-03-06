package com.thekirschners.ml.data;

/**
 * Created by emilkirschner on 05/03/16.
 */
public class Triplet<A,B,C> {
    final A a;
    final B b;
    final C c;

    public Triplet(Pair<A,B> p, C c) {
        this(p.getA(), p.getB(), c);
    }

    public Triplet(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
