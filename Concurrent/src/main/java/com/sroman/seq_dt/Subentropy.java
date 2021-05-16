package com.sroman.seq_dt;

import java.util.Map;

public class Subentropy implements Runnable {
    private final Attribute a;
    private final int column;
    private final int instances;
    private final String[] headers;
    private final String[][] data;
    private final Double entropy;

    public Subentropy(Attribute a, int column, int instances, String[] headers, String[][] data, Double entropy) {
        this.a = a;
        this.column = column;
        this.instances = instances;
        this.headers = headers;
        this.data = data;
        this.entropy = entropy;
    }
    
    @Override
    public void run() {
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
    }
    
}
