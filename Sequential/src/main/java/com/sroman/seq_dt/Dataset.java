package com.sroman.seq_dt;

import java.util.HashMap;
import java.util.Map;

public class Dataset {
    private final String[][] data;
    private final String[] headers;
    private final Attribute[] x;
    private final Attribute y;
    private final int instances;
    
    private Double entropy;
    private HashMap<String,Double> subentropies;
    private HashMap<String,Double> gains;
    
    public Dataset(String[][] data) {
        this.data = data;
        headers = data[0];
        y = new Attribute(data, headers.length-1);
        instances = data.length - 1;
        
        x = new Attribute[headers.length-1];
        for(int i = 0; i < headers.length-1; i++) {
            x[i] = new Attribute(data, i);
        }
    }

    public double getEntropy() {
        if(entropy == null)
            entropy = calculateSystemEntropy();
        return entropy;
    }
    
    public Attribute getGreatestGainAttribute() {
        String attributeName = getGreatestGain().getKey();
        for(Attribute a : x) {
            if(a.getName().equals(attributeName)) 
                return a;
        }
        return null;
    }
    
    public String getYValue() {
        return y.getValues().keySet().iterator().next();
    }
    
    public HashMap<String, Float> getPartialYValues() {
        HashMap<String, Float> map = new HashMap<>();
        for(Map.Entry<String, Integer> value : y.getValues().entrySet()) {
            map.put(value.getKey(), (float)value.getValue()/instances);
        }
        return map;
    }
    
    private HashMap<String,Double> getSubentropies() {
        if(subentropies == null) {
            getEntropy();
            subentropies = calculateSubentropies();
        }
        return subentropies;
    }
    
    private HashMap<String,Double> getGains() {
        if(gains == null) {
            getSubentropies();
            gains = new HashMap<>();
            for(Attribute a : x)
                gains.put(a.getName(), a.getGain());
        }
        return gains;
    }
    
    private Map.Entry<String,Double> getGreatestGain() {
        getGains();
        Map.Entry<String,Double> greatest = gains.entrySet().iterator().next();
        for(Map.Entry<String,Double> gain: gains.entrySet()) {
            if(gain.getValue() > greatest.getValue())
                greatest = gain;
        }
        return greatest;
    }
    
    private double calculateSystemEntropy() {
        double res = 0.0;
        Integer[] counts = y.getValues().values().stream().toArray(Integer[]::new);
        for (Integer count : counts) {
            double fraction = (double) count / instances;
            res += fraction * Helpers.log2(fraction);
        }
        return res * -1;
    }
    
    private double calculateSubentropy(Attribute a, int column) {
        double sum = 0.0;
        for(Map.Entry entry : a.getValues().entrySet()) {
            String value = (String)entry.getKey();
            int count = (int)entry.getValue();
            
            String[][] subdata = new String[count + 1][headers.length - 1];
            String[][] dataWithoutCol = Helpers.removeCol(data, column);
            subdata[0] = dataWithoutCol[0];
            
            int indx = 1;
            for(int i = 1; i < data.length; i++) {
                if(data[i][column].equals(value)) {
                    subdata[indx] = dataWithoutCol[i];
                    indx++;
                }
            }
            
            Dataset subdataset = new Dataset(subdata);
            a.putSubdataset(value, subdataset);
            double relativeEntropy = ((double)count/instances) * subdataset.getEntropy();
            sum += relativeEntropy;
        }
        a.setEntropy(sum);
        a.setGain(entropy - sum);
        return a.getEntropy();
    }
    
    private HashMap<String, Double> calculateSubentropies() {
        HashMap<String,Double> entropies = new HashMap<>();
        for(int i = 0; i < x.length; i++) {
            Attribute a = x[i];
            if(a.getEntropy() == null)
                calculateSubentropy(a, i);
            entropies.put(a.getName(), a.getEntropy());
        }
        return entropies;
    }
    
}
