package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.TestDataSet;
import com.thekirschners.ml.data.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

/**
 * Created by emilkirschner on 14/03/16.
 */
@RunWith(JUnit4.class)
public class RecParentNodeTest {
    @Test
    public void testDecisionTree() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        TreeNode iterative = TreeNode.Tools.Builder.selector(new InfoGainSplitAttrSelector()).build(dataSet, new Integer[]{1, 2, 3, 4}, 5);
    }
}
