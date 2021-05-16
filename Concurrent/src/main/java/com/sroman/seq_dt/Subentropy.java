package com.sroman.seq_dt;

import java.util.Map;

public class Subentropy implements Runnable {
    private final Attribute a;
    private final int column;
    private final int instances;
    private final int numAttributes;
    private final String[][] data;
    private final Double entropy;
    private final Value value;
    
    public Subentropy(Attribute a, int column, int instances, int numAttributes, String[][] data, Double entropy, Value value) {
        this.a = a;
        this.column = column;
        this.instances = instances;
        this.numAttributes = numAttributes;
        this.data = data;
        this.entropy = entropy;
        this.value = value;
    }
    
    @Override
    public void run() {
            String name = value.name;
            int count = value.count;
            
            String[][] subdata = new String[count + 1][numAttributes];
            String[][] dataWithoutCol = Helpers.removeCol(data, column);
            subdata[0] = dataWithoutCol[0];
            
            int indx = 1;
            for(int i = 1; i < data.length; i++) {
                if(data[i][column].equals(name)) {
                    subdata[indx] = dataWithoutCol[i];
                    indx++;
                }
            }
            
            Dataset subdataset = new Dataset(subdata);
            double relativeEntropy = ((double)count/instances) * subdataset.getEntropy();
            value.subdataset = subdataset;
            value.relativeEntropy = relativeEntropy;
    }
    
}
