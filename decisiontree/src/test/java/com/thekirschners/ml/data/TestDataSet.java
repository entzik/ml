package com.thekirschners.ml.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by emilkirschner on 05/03/16.
 */
public class TestDataSet {
    public static List<Tuple> getDataSet() {
        return Arrays.asList(
                new Tuple(new String[]{"1", "youth", "high", "no", "fair", "no"}),
                new Tuple(new String[]{"2", "youth", "high", "no", "excellent", "no"}),
                new Tuple(new String[]{"3", "middle", "high", "no", "fair", "yes"}),
                new Tuple(new String[]{"4", "senior", "medium", "no", "fair", "yes"}),
                new Tuple(new String[]{"5", "senior", "low", "yes", "fair", "yes"}),
                new Tuple(new String[]{"6", "senior", "low", "yes", "excellent", "no"}),
                new Tuple(new String[]{"7", "middle", "low", "yes", "excellent", "yes"}),
                new Tuple(new String[]{"8", "youth", "medium", "no", "fair", "no"}),
                new Tuple(new String[]{"9", "youth", "low", "yes", "fair", "yes"}),
                new Tuple(new String[]{"10", "senior", "medium", "yes", "fair", "yes"}),
                new Tuple(new String[]{"11", "youth", "medium", "yes", "excellent", "yes"}),
                new Tuple(new String[]{"12", "middle", "medium", "no", "excellent", "yes"}),
                new Tuple(new String[]{"13", "middle", "high", "yes", "fair", "yes"}),
                new Tuple(new String[]{"14", "senior", "medium", "no", "excellent", "no"})
        );

        // info_D = 0.940
        // info_age_D = 0.694
        // informationGain(age) = 0.246
    }
}
