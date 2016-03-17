package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.Pair;
import com.thekirschners.ml.data.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by emilkirschner on 02/03/16.
 */
public interface SplitAttributeSelector {
    Pair<Integer, Map<String, List<Tuple>>> selectSplitAttribute(Collection<Tuple> tuples, Integer[] splitAttributeCandidates, int classAttr);
}
