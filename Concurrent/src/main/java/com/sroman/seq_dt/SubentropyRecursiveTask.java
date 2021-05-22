package com.sroman.seq_dt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class SubentropyRecursiveTask extends RecursiveTask<Double> {

    private final int THRESHOLD = 3;

    private final int column;
    private final Dataset dataset;
    private final Attribute a;
    private final List<Value> values;

    public SubentropyRecursiveTask(Attribute a, int column, Dataset dataset) {
        this.a = a;
        this.column = column;
        this.dataset = dataset;
        this.values = a.getValues();
    }

    public SubentropyRecursiveTask(SubentropyRecursiveTask s, List<Value> values) {
        this.a = s.a;
        this.column = s.column;
        this.dataset = s.dataset;
        this.values = values;
    }

    @Override
    protected Double compute() {
        if (values.size() > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks()).stream().mapToDouble(ForkJoinTask::join).sum();
        } else {
            return processing();
        }
    }

    private Collection<SubentropyRecursiveTask> createSubtasks() {
        List<SubentropyRecursiveTask> subtasks = new ArrayList<>();

        int mid = values.size() / 2;

        subtasks.add(new SubentropyRecursiveTask(this, values.subList(0, mid)));
        subtasks.add(new SubentropyRecursiveTask(this, values.subList(mid, values.size())));

        return subtasks;
    }

    private Double processing() {
        double sum = 0.0;
        
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
            sum += relativeEntropy;
        }

        return sum;
    }
}
