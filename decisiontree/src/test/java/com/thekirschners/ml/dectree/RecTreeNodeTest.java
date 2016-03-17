package com.thekirschners.ml.dectree;

import com.thekirschners.ml.data.TestDataSet;
import com.thekirschners.ml.data.Tuple;
import com.thekirschners.ml.dectree.builder.RecDecisionTreeNode;
import com.thekirschners.ml.dectree.builder.TreeElement;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by emilkirschner on 14/03/16.
 */
@RunWith(JUnit4.class)
public class RecTreeNodeTest {
    @Test
    public void testDecisionTree() {
        List<Tuple> dataSet = TestDataSet.getDataSet();
        RecDecisionTreeNode recDecisionTreeNode = new RecDecisionTreeNode(new InfoGainSplitAttrSelector(), dataSet, new Integer[]{1, 2, 3, 4}, 5);
        TreeElement build = TreeElement.Builder.build(new InfoGainSplitAttrSelector(), dataSet, new Integer[]{1, 2, 3, 4}, 5);

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        TreeElement.Printer.print(build, "", new PrintStream(out1));
        byte[] bytes1 = out1.toByteArray();

        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        TreeElement.Printer.print(recDecisionTreeNode, "", new PrintStream(out2));
        byte[] bytes2 = out2.toByteArray();

        TestCase.assertEquals(bytes1.length, bytes2.length);
        boolean tmp = true;
        for (int i = 0; i < bytes1.length; i ++)
            tmp = tmp && bytes1[i] == bytes2[i];
        TestCase.assertTrue(tmp);
    }
}
