package com.sroman.concurrenttrees;

import hu.webarticum.treeprinter.BorderTreeNodeDecorator;
import hu.webarticum.treeprinter.SimpleTreeNode;
import hu.webarticum.treeprinter.TraditionalTreePrinter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static long time = 0;

    public static Approach approach;

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter dataset's name:");
        String datasetName = scanner.nextLine();
        System.out.println("\nEnter tree's max depth:");
        int maxDepth = scanner.nextInt();

        System.out.println("\nMENU");
        System.out.println("1. Thread per attribute");
        System.out.println("2. Thread per value");
        System.out.println("3. ForkJoin - RecursiveAction");
        System.out.println("4. ForkJoin - RecursiveTask");
        System.out.println("Enter the concurrent approach to take:");
        int approachIndex = scanner.nextInt();

        switch (approachIndex) {
            case 1:
                approach = Approach.PER_ATTRIBUTE;
                break;
            case 2:
                approach = Approach.PER_VALUE;
                break;
            case 4:
                approach = Approach.RECURSIVE_TASK;
                break;
            default:
                approach = Approach.RECURSIVE_ACTION;
        }
        
        final String[][] data = Helpers.getMatrixFromCSV("src/main/java/com/sroman/concurrenttrees/ds/" + datasetName + ".csv");
        Dataset dataset = new Dataset(data);
        
        SimpleTreeNode tree = createTree(dataset, maxDepth);
        new TraditionalTreePrinter().print(new BorderTreeNodeDecorator(tree));
        
        System.out.println("Time spent calculating subentropies:\n" + time + " ms");
    }

    

    static SimpleTreeNode createTree(Dataset dataset, int maxLevel) {
        if (maxLevel > dataset.getNumOfAttributes()) {
            maxLevel = dataset.getNumOfAttributes();
        }
        return recursiveEntropy(dataset, 0, maxLevel, null);
    }

    static SimpleTreeNode recursiveEntropy(Dataset dataset, int iter, int maxLevel, SimpleTreeNode parent) {
        if (dataset.getEntropy() == 0) {
            SimpleTreeNode node = new SimpleTreeNode(dataset.getYValue());
            if (parent == null) {
                return node;
            } else {
                parent.addChild(node);
                return parent;
            }
        } else if (iter == maxLevel) {
            dataset.getPartialYValues().entrySet().forEach(value -> {
                parent.addChild(new SimpleTreeNode(value.getKey() + " => " + value.getValue()));
            });
            return parent;
        } else {
            Attribute a;
            try {
                a = dataset.getGreatestGainAttribute();
                time += dataset.getTime();

                SimpleTreeNode node = new SimpleTreeNode(a.getName());
                a.getValues().forEach(value -> {
                    SimpleTreeNode child = new SimpleTreeNode(value.getName());
                    node.addChild(recursiveEntropy(value.getSubdataset(), iter + 1, maxLevel, child));
                });
                if (parent == null) {
                    return node;
                } else {
                    parent.addChild(node);
                    return parent;
                }
            } catch (InterruptedException ex) {
                System.out.println("INTERRUPTED");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
}
