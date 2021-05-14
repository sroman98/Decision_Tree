package com.sroman.seq_dt;

import java.util.Map;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final String[][] data = Helpers.getMatrixFromCSV("weather.csv");
        Dataset dataset = new Dataset(data);

//        System.out.println("System entropy:");
//        System.out.println(dataset.getEntropy());
//        
//        System.out.println("\nSubentropies:");
//        for(Map.Entry subentropy : dataset.getSubentropies().entrySet()) {
//            System.out.println(subentropy.getKey() + ": " + subentropy.getValue());
//        }
//        
//        System.out.println("\nGains:");
//        for(Map.Entry gain : dataset.getGains().entrySet()) {
//            System.out.println(gain.getKey() + ": " + gain.getValue());
//        }
//        
//        System.out.println("\nGreatest gain:");
//        Map.Entry gg = dataset.getGreatestGain();
//        System.out.println(gg.getKey() + ": " + gg.getValue());
        recursiveEntropy(dataset, 0);
    }

    static void recursiveEntropy(Dataset dataset, int iter) {
        if (iter == 3) {
            System.out.println("DONE");
        } else if (dataset.getEntropy() == 0) {
            System.out.println(dataset.getYValue());
        } else {
            System.out.println("\nGreatest gain:");
            Map.Entry gg = dataset.getGreatestGain();
            System.out.println(gg.getKey() + ": " + gg.getValue());
            Attribute a = dataset.getGreatestGainAttribute();
            for (Map.Entry<String, Dataset> e : a.getSubdatasets().entrySet()) {
                System.out.println("\n" + e.getKey() + ":");
                recursiveEntropy(e.getValue(), iter + 1);
            }
        }
    }
}
