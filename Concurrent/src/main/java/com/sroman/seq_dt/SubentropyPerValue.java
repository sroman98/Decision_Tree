package com.sroman.seq_dt;

public class SubentropyPerValue implements Runnable {
    private final int column;
    private final int instances;
    private final int numAttributes;
    private final String[][] data;
    private final Value value;
    
    public SubentropyPerValue(int column, int instances, int numAttributes, String[][] data, Value value) {
        this.column = column;
        this.instances = instances;
        this.numAttributes = numAttributes;
        this.data = data;
        this.value = value;
    }
    
    @Override
    public void run() {
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
    }
}
