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
        
        System.out.println("\nSubentropies:");
        for(Map.Entry subentropy : dataset.getSubentropies().entrySet()) {
            System.out.println(subentropy.getKey() + ": " + subentropy.getValue());
        }
        
        System.out.println("\nGains:");
        for(Map.Entry gain : dataset.getGains().entrySet()) {
            System.out.println(gain.getKey() + ": " + gain.getValue());
        }
        
        System.out.println("\nGreatest gain:");
        Map.Entry gg = dataset.getGreatestGain();
        System.out.println(gg.getKey() + ": " + gg.getValue());
    }
}
