package com.sroman.seq_dt;

import hu.webarticum.treeprinter.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.TraditionalTreePrinter;

public class Main {

    public static void main(String args[]) {
        final String[][] data = Helpers.getMatrixFromCSV("../weather.csv");
        Dataset dataset = new Dataset(data);
        SimpleTreeNode tree = createTree(dataset, 4);
        new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(tree));
    }

    static SimpleTreeNode createTree(Dataset dataset, int maxLevel) {
        if(maxLevel > dataset.getNumberOfAttributes())
            maxLevel = dataset.getNumberOfAttributes();
        return recursiveEntropy(dataset, 0, maxLevel, null);
    }

    static SimpleTreeNode recursiveEntropy(Dataset dataset, int iter, int maxLevel, SimpleTreeNode parent) {
        if (dataset.getEntropy() == 0) {
            SimpleTreeNode node = new SimpleTreeNode(dataset.getYValue());
            if (parent == null)
                return node;
            else {
                parent.addChild(node);
                return parent;
            }
        } else if (iter == maxLevel) {
            dataset.getPartialYValues().entrySet().forEach(value -> {
                parent.addChild(new SimpleTreeNode(value.getKey() + " => " + value.getValue()));
            });
            return parent;
        } else {
            Attribute a = dataset.getGreatestGainAttribute();
            SimpleTreeNode node = new SimpleTreeNode(a.getName());
            a.getSubdatasets().entrySet().forEach(e -> {
                SimpleTreeNode child = new SimpleTreeNode(e.getKey());
                node.addChild(recursiveEntropy(e.getValue(), iter + 1, maxLevel, child));
            });
            if (parent == null) {
                return node;
            } else {
                parent.addChild(node);
                return parent;
            }
        }
    }
}
