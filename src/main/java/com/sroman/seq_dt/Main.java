package com.sroman.seq_dt;

import java.util.Map;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final String[][] data = Helpers.getMatrixFromCSV("weather.csv");
        Dataset dataset = new Dataset(data);
        System.out.println("System entropy:");
        System.out.println(dataset.getEntropy());
        System.out.println("Subentropies:");
        for(Map.Entry subentropy : dataset.getSubentropies().entrySet()) {
            System.out.println(subentropy.getKey() + ": " + subentropy.getValue());
        }
    }
}
