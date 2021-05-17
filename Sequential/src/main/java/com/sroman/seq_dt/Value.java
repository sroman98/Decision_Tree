package com.sroman.seq_dt;

public class Value {
    public String name;
    public int count;
    public double relativeEntropy = 0;
    public Dataset subdataset = null;
    
    public Value(String name, int occurences) {
        this.name = name;
        this.count = occurences;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Value)obj).name.equals(name);
    }
    
}
