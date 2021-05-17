package com.sroman.seq_dt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class SubentropyRecursiveAction extends RecursiveAction {

    private final int column;
    private final Dataset dataset;
    private final Attribute a;
    private final int start;
    private final int finish;

    public SubentropyRecursiveAction(Attribute a, int column, Dataset dataset) {
        this.a = a;
        this.column = column;
        this.dataset = dataset;
        this.start = 0;
        this.finish = a.getValues().size();
    }

    public SubentropyRecursiveAction(SubentropyRecursiveAction s, int start, int finish) {
        this.a = s.a;
        this.column = s.column;
        this.dataset = s.dataset;
        this.start = start;
        this.finish = finish;
    }

    @Override
    protected void compute() {
        if (finish - start > 1) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(a.getValues().get(start));
        }
    }

    private List<SubentropyRecursiveAction> createSubtasks() {
        List<SubentropyRecursiveAction> subtasks = new ArrayList<>();

        int mid = (start + finish) / 2;

        subtasks.add(new SubentropyRecursiveAction(this, start, mid));
        subtasks.add(new SubentropyRecursiveAction(this, mid, finish));

        return subtasks;
    }

    private void processing(Value value) {
        String name = value.name;
        int count = value.count;

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
        value.subdataset = subdataset;
        value.relativeEntropy = relativeEntropy;
        a.augmentEntropy(relativeEntropy);
        a.reduceGain(dataset.getEntropy(), relativeEntropy);
    }
}
