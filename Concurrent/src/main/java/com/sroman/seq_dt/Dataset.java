package com.sroman.seq_dt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    
    public Attribute getGreatestGainAttribute() throws InterruptedException {
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
        y.getValues().entrySet().forEach(value -> {
            map.put(value.getKey(), (float)value.getValue()/instances);
        });
        return map;
    }
    
    public int getNumberOfAttributes() {
        return x.length;
    }
    
    private HashMap<String,Double> getSubentropies() throws InterruptedException {
        if(subentropies == null) {
            getEntropy();
            subentropies = calculateSubentropies();
        }
        return subentropies;
    }
    
    private HashMap<String,Double> getGains() throws InterruptedException {
        if(gains == null) {
            getSubentropies();
            gains = new HashMap<>();
            for(Attribute a : x)
                gains.put(a.getName(), a.getGain());
        }
        return gains;
    }
    
    private Map.Entry<String,Double> getGreatestGain() throws InterruptedException {
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
    
    private HashMap<String, Double> calculateSubentropies() throws InterruptedException {
        HashMap<String,Double> entropies = new HashMap<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i = 0; i < x.length; i++) {
            Attribute a = x[i];
            if(a.getEntropy() == null)
                pool.execute(new Subentropy(a, i, instances, headers.clone(), data.clone(), entropy));
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        for (Attribute a : x) {
            entropies.put(a.getName(), a.getEntropy());
        }
        return entropies;
    }
    
}
