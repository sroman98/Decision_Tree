package com.sroman.seq_dt;

public class SubentropyPerAttribute implements Runnable {
    private final Attribute a;
    private final int column;
    private final int instances;
    private final int numAttributes;
    private final String[][] data;
    private final Double entropy;

    public SubentropyPerAttribute(Attribute a, int column, int instances, int numAttributes, String[][] data, Double entropy) {
        this.a = a;
        this.column = column;
        this.instances = instances;
        this.numAttributes = numAttributes;
        this.data = data;
        this.entropy = entropy;
    }
    
    @Override
    public void run() {
        double sum = 0.0;
        for(Value value : a.getValues()) {
            String name = value.getName();
            int count = value.getCount();
            
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
            value.setSubdataset(subdataset);
            value.setRelativeEntropy(relativeEntropy);
            
            sum += relativeEntropy;
        }
        a.setEntropy(sum);
        a.setGain(entropy - sum);
    }
}
