package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Tuple;

import java.util.Collection;

/**
 * Created by emilkirschner on 02/03/16.
 */
public interface SplitAttributeSelector {
    int selectSplitAttribute(Collection<Tuple> tuples, Integer[] splitAttributeCandidates, int classAttr);
}
