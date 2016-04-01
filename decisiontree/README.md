#Decision Trees

Easy to use API for building decision tree using information gain and and information gain ratio based attribute selection

```java
Integer[] attributes = {1, 2, 3, 4};
int classAttribute = 5;
TreeNode root = TreeNode.Tools.Builder.selector(new InfoGainSplitAttrSelector()).build(dataSet, attributes, classAttribute);

Object predict = TreeNode.Tools.Oracle.predict(root, dataSet.get(0));
```
