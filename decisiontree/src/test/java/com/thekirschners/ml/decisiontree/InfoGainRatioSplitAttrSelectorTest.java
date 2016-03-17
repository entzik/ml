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
public class InfoGainRatioSplitAttrSelectorTest {
    @Test
    public void testsplitInfo() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InfoGainRatioSplitAttrSelector selector = new InfoGainRatioSplitAttrSelector();
        Collection<List<Tuple>> partitions = selector.partitionOnAttribute(dataSet, 2).values();
        double splitInfoA = selector.splitInfo(dataSet, partitions, 5);
        TestCase.assertEquals("splitInfo", 1.557, splitInfoA, 0.001);
    }

    @Test
    public void testGainRatio() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        InfoGainRatioSplitAttrSelector selector = new InfoGainRatioSplitAttrSelector();
        double entropy = selector.entropy(dataSet, 5);
        double gainRatio = selector.gainRatio(dataSet, 5, entropy, selector.partitionOnAttribute(dataSet, 2).values());
        TestCase.assertEquals("gain ratio", 0.019, gainRatio, 0.001);
    }

    @Test
    public void testSelectSplitAttribute() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        int splitAttribute = new InfoGainRatioSplitAttrSelector().selectSplitAttribute(dataSet, new Integer[]{1, 2, 3, 4}, 5).getA();
        TestCase.assertEquals("splitAttribute", 1, splitAttribute);
    }
}
