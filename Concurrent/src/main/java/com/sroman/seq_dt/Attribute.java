package com.sroman.seq_dt;

import java.util.HashMap;

public class Attribute {
    final private String name;
    final private HashMap<String,Integer> values;
    private Double entropy;
    private Double gain;
    private final HashMap<String,Dataset> subdatasets;
    
    public Attribute(String[][] data, int index) {
        name = data[0][index];
        subdatasets = new HashMap<>();
        values = new HashMap<>();
        for(int i = 1; i < data.length; i++) {
            String value = data[i][index];
            Integer count = values.get(value);
            if(count == null)
                values.put(value, 1);
            else
                values.replace(value, ++count);
        }
    }

    public String getName() {
        return name;
    }

    public Double getEntropy() {
        return entropy;
    }
    
    public HashMap<String, Integer> getValues() {
        return values;
    }

    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }
    
    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }
    
    public void putSubdataset(String key, Dataset subdataset) {
        subdatasets.put(key, subdataset);
    }

    public HashMap<String, Dataset> getSubdatasets() {
        return subdatasets;
    }

}
