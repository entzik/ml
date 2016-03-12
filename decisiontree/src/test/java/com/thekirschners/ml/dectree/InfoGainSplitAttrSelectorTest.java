package com.thekirschners.ml.dectree;

import com.thekirschners.ml.data.TestDataSet;
import com.thekirschners.ml.data.Tuple;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.List;

/**
 * Created by emilkirschner on 05/03/16.
 */
@RunWith(JUnit4.class)
public class InfoGainSplitAttrSelectorTest {
    @Test
    public void testEntropy() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        double entropy = new InfoGainSplitAttrSelector().entropy(dataSet, 5);
        TestCase.assertEquals("entropy value", 0.940d, entropy, 0.001);
    }

    @Test
    public void testSplitEntropy() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InfoGainSplitAttrSelector informationGainSplitAttributeSelector = new InfoGainSplitAttrSelector();
        Collection<List<Tuple>> partitions = informationGainSplitAttributeSelector.partitionOnAttribute(dataSet, 1).values();
        double splitEntropy = informationGainSplitAttributeSelector.splitEntropy(dataSet, partitions, 5);
        TestCase.assertEquals("entropy value", 0.694d, splitEntropy, 0.001);
    }

    @Test
    public void testInformationGain() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InfoGainSplitAttrSelector informationGainSplitAttributeSelector = new InfoGainSplitAttrSelector();
        double entropy = informationGainSplitAttributeSelector.entropy(dataSet, 5);
        Collection<List<Tuple>> partitions = informationGainSplitAttributeSelector.partitionOnAttribute(dataSet, 1).values();
        double informationGain = informationGainSplitAttributeSelector.informationGain(entropy, dataSet, partitions, 5);
        TestCase.assertEquals("information gain", 0.246d, informationGain, 0.001);
    }

    @Test
    public void testSelectSplitAttribute() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        int splitAttribute = new InfoGainSplitAttrSelector().selectSplitAttribute(dataSet, new Integer[]{1, 2, 3, 4}, 5).getA();
        TestCase.assertEquals("splitAttribute", 1, splitAttribute);
    }
}
