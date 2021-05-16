package com.sroman.seq_dt;

public class Main {

    public static void main(String args[]) {
        final String[][] data = Helpers.getMatrixFromCSV("../weather.csv");
        Dataset dataset = new Dataset(data);
        Node tree = createTree(dataset);
    }

    static Node createTree(Dataset dataset) {
        return recursiveEntropy(dataset, 0, null);
    }

    static Node recursiveEntropy(Dataset dataset, int iter, Node parent) {
        if (dataset.getEntropy() == 0) {
            Node node = new Node(dataset.getYValue());
            if (parent == null)
                return node;
            else {
                parent.addChild(node);
                return parent;
            }
        } else if (iter == 1) {
            dataset.getPartialYValues().entrySet().forEach(value -> {
                parent.addChild(new Node(value));
            });
            return parent;
        } else {
            Attribute a = dataset.getGreatestGainAttribute();
            Node node = new Node(a.getName());
            a.getSubdatasets().entrySet().forEach(e -> {
                Node child = new Node(e.getKey());
                node.addChild(recursiveEntropy(e.getValue(), iter + 1, child));
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
