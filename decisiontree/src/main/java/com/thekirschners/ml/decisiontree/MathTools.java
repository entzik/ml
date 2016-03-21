package com.thekirschners.ml.decisiontree;

/**
 * Created by emilkirschner on 21/03/16.
 */
public class MathTools {
    public static final double LOG2_2 = Math.log(2.0d);

    public static double log2(double n) {
        return Math.log(n) / LOG2_2;
    }
}
