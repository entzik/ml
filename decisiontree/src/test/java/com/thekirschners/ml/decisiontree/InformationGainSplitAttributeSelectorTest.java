package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.TestDataSet;
import com.thekirschners.ml.data.Tuple;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

/**
 * Created by emilkirschner on 05/03/16.
 */
@RunWith(JUnit4.class)
public class InformationGainSplitAttributeSelectorTest {
    @Test
    public void testEntropy() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        double entropy = new InformationGainSplitAttributeSelector().entropy(dataSet, 5);
        TestCase.assertEquals("entropy value", 0.940d, entropy, 0.001);
    }

    @Test
    public void testSplitEntropy() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        double splitEntropy = new InformationGainSplitAttributeSelector().splitEntropy(dataSet, 1, 5);
        TestCase.assertEquals("entropy value", 0.694d, splitEntropy, 0.001);
    }

    @Test
    public void testInformationGain() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        double informationGain = new InformationGainSplitAttributeSelector().informationGain(dataSet, 1, 5);
        TestCase.assertEquals("information gain", 0.246d, informationGain, 0.001);
    }
}
