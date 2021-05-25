package com.sroman.concurrenttrees;

public class Value {
    private final String name;
    private int count;
    private double relativeEntropy = 0;
    private Dataset subdataset = null;
    
    public Value(String name, int occurences) {
        this.name = name;
        this.count = occurences;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Value)obj).name.equals(name);
    }
    
    public double getRelativeEntropy() {
        return relativeEntropy;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRelativeEntropy(double relativeEntropy) {
        this.relativeEntropy = relativeEntropy;
    }

    public void setSubdataset(Dataset subdataset) {
        this.subdataset = subdataset;
    }

    public Dataset getSubdataset() {
        return subdataset;
    }
    
}
