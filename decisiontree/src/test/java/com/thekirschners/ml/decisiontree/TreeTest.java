package com.thekirschners.ml.decisiontree;

import com.thekirschners.ml.data.TestDataSet;
import com.thekirschners.ml.data.Tuple;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.model.TestClass;

import java.util.List;

/**
 * Created by emilkirschner on 14/03/16.
 */
@RunWith(JUnit4.class)
public class TreeTest {
    @Test
    public void testDecisionTree() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        TreeNode root = TreeNode.Tools.Builder.selector(new InfoGainSplitAttrSelector()).build(dataSet, new Integer[]{1, 2, 3, 4}, 5);
        Object predict = TreeNode.Tools.Oracle.predict(root, dataSet.get(0));
        TestCase.assertEquals(dataSet.get(0).attribute(5), predict);
    }
}
