package com.sroman.concurrenttrees;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
    final private String name;
    final private List<Value> values;
    private Double entropy;
    private Double gain;
    
    public Attribute(String[][] data, int col) {
        name = data[0][col];
        values = new ArrayList<>();
        for(int i = 1; i < data.length; i++) {
            String value = data[i][col];
            Value newValue = new Value(value, 1);
            if(!values.contains(newValue))
                values.add(newValue);
            else {
                int index = values.indexOf(newValue);
                values.get(index).setCount(values.get(index).getCount() + 1);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Double getEntropy() {
        return entropy;
    }
    
    public List<Value> getValues() {
        return values;
    }

    public Double getGain() {
        return gain;
    }
    
    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }
    
    public void setGain(double gain) {
        this.gain = gain;
    }

}
