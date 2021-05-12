package com.sroman.seq_dt;

import static java.lang.Double.doubleToLongBits;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
    
    public HashMap<String,Double> getSubentropies() {
        HashMap<String,Double> subentropies = new HashMap<>();
        for(int i = 0; i < x.length; i++) {
            Attribute a = x[i];
            if(a.getEntropy() == null)
                calculateSubentropy(a, i);
            subentropies.put(a.getName(), a.getEntropy());
        }
        return subentropies;
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
        HashMap<String, Integer>  values = a.getValues();
        double sum = 0.0;
        for(Map.Entry entry : a.getValues().entrySet()) {
            String value = (String)entry.getKey();
            int count = (int)entry.getValue();
            
            String[][] subdata = new String[count + 1][2];
            subdata[0][0] = headers[column];
            subdata[0][1] = headers[headers.length - 1];
            int indx = 1;
            for(int i = 1; i < data.length; i++) {
                if(data[i][column].equals(value)) {
                    subdata[indx][0] = data[i][column];
                    subdata[indx][1] = data[i][headers.length - 1];
                    indx++;
                }
            }
            Dataset subdataset = new Dataset(subdata);
            double relativeEntropy = ((double)count/instances) * subdataset.getEntropy();
            sum += relativeEntropy;
        };
        a.setEntropy(sum);
        return a.getEntropy();
    }
}
