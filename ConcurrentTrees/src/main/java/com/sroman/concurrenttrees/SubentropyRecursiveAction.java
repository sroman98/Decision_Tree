package com.sroman.concurrenttrees;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class SubentropyRecursiveAction extends RecursiveAction {

    private final int THRESHOLD = 3;
    
    private final int column;
    private final Dataset dataset;
    private final Attribute a;
    private final List<Value> values;

    public SubentropyRecursiveAction(Attribute a, int column, Dataset dataset) {
        this.a = a;
        this.column = column;
        this.dataset = dataset;
        this.values = a.getValues();
    }

    public SubentropyRecursiveAction(SubentropyRecursiveAction s, List<Value> values) {
        this.a = s.a;
        this.column = s.column;
        this.dataset = s.dataset;
        this.values = values;
    }

    @Override
    protected void compute() {
        if (values.size() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing();
        }
    }

    private List<SubentropyRecursiveAction> createSubtasks() {
        List<SubentropyRecursiveAction> subtasks = new ArrayList<>();

        int mid = values.size() / 2;

        subtasks.add(new SubentropyRecursiveAction(this, values.subList(0, mid)));
        subtasks.add(new SubentropyRecursiveAction(this, values.subList(mid, values.size())));

        return subtasks;
    }

    private void processing() {
        for (Value value : values) {
            String name = value.getName();
            int count = value.getCount();

            String[][] subdata = new String[count + 1][dataset.getNumOfAttributes()];
            String[][] dataWithoutCol = Helpers.removeCol(dataset.getData(), column);
            subdata[0] = dataWithoutCol[0];

            int indx = 1;
            for (int i = 1; i < dataset.getData().length; i++) {
                if (dataset.getData()[i][column].equals(name)) {
                    subdata[indx] = dataWithoutCol[i];
                    indx++;
                }
            }

            Dataset subdataset = new Dataset(subdata);
            double relativeEntropy = ((double) count / dataset.getNumOfInstances()) * subdataset.getEntropy();
            value.setSubdataset(subdataset);
            value.setRelativeEntropy(relativeEntropy);
        }
    }
}
