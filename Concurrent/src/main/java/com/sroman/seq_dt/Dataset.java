package com.sroman.seq_dt;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Dataset {

    private final String[][] data;
    private final String[] headers;
    private final Attribute[] x;
    private final Attribute y;
    private final int instances;

    private Double entropy;

    public Dataset(String[][] data) {
        this.data = data;
        headers = data[0];
        y = new Attribute(data, headers.length - 1);
        instances = data.length - 1;

        x = new Attribute[headers.length - 1];
        for (int i = 0; i < headers.length - 1; i++) {
            x[i] = new Attribute(data, i);
        }
    }

    public double getEntropy() {
        calculateSystemEntropy();
        return entropy;
    }

    public Attribute getGreatestGainAttribute() throws InterruptedException {
        calculateSubentropies();
        Attribute greatest = x[0];
        for (Attribute a : x) {
            if (a.getGain() > greatest.getGain()) {
                greatest = a;
            }
        }
        return greatest;
    }

    // Gives the unique y value (when entropy is 0)
    public String getYValue() {
        return y.getValues().get(0).name;
    }

    // Used when we don't have a unique y value and we've reached max depth
    public HashMap<String, Float> getPartialYValues() {
        HashMap<String, Float> map = new HashMap<>();
        y.getValues().forEach(value -> {
            map.put(value.name, (float) value.count / instances);
        });
        return map;
    }

    // Used for getting max depth in main
    public int getNumOfAttributes() {
        return x.length;
    }
    
    public int getNumOfInstances() {
        return instances;
    }
    
    public String[][] getData() {
        return data;
    }

    private void calculateSystemEntropy() {
        if (entropy != null) {
            return;
        }
        double res = 0.0;
        for (Value value : y.getValues()) {
            double fraction = (double) value.count / instances;
            res += fraction * Helpers.log2(fraction);
        }
        entropy = res * -1;
    }

    private void calculateSubentropies() throws InterruptedException {
        calculateSystemEntropy();
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        for (int i = 0; i < x.length; i++) {
            Attribute a = x[i];
            if (a.getEntropy() == null) {
                commonPool.invoke(new SubentropyRecursiveAction(a,i,this));
            }
        }
        commonPool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
