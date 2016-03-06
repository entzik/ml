package com.thekirschners.ml.decisiontree;

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
public class InformationGainRatioSplitAttributeSelectorTest {
    @Test
    public void testsplitInfo() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InformationGainRatioSplitAttributeSelector selector = new InformationGainRatioSplitAttributeSelector();
        Collection<List<Tuple>> partitions = selector.partitionOnAttribute(dataSet, 2);
        double splitInfoA = selector.splitInfo(dataSet, partitions, 5);
        TestCase.assertEquals("splitInfo", 1.557, splitInfoA, 0.001);
    }

    @Test
    public void testGainRatio() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InformationGainRatioSplitAttributeSelector selector = new InformationGainRatioSplitAttributeSelector();
        double entropy = selector.entropy(dataSet, 5);
        double gainRatio = selector.gainRatio(dataSet, 5, entropy, selector.partitionOnAttribute(dataSet, 2));
        TestCase.assertEquals("gain ratio", 0.019, gainRatio, 0.001);
    }

    @Test
    public void testSelectSplitAttribute() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        int splitAttribute = new InformationGainRatioSplitAttributeSelector().selectSplitAttribute(dataSet, new Integer[]{1, 2, 3, 4}, 5);
        TestCase.assertEquals("splitAttribute", 1, splitAttribute);
    }
}
