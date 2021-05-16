package com.sroman.seq_dt;

import java.text.DecimalFormat;
import java.util.Map;

public class Main {

    public static void main(String args[]) {
        final String[][] data = Helpers.getMatrixFromCSV("../titanic.csv");
        Dataset dataset = new Dataset(data);
        recursiveEntropy(dataset, 0);
    }

    static void recursiveEntropy(Dataset dataset, int iter) {
        String tabs = "";
        for (int i = 0; i < iter; i++) {
            tabs += "\t\t";
        }
        if (dataset.getEntropy() == 0) {
            System.out.println(tabs + dataset.getYValue());
        } else if (iter == 4) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            for (Map.Entry<String, Float> value : dataset.getPartialYValues().entrySet()) {
                System.out.println(tabs + value.getKey() + " " + df.format(value.getValue()));
            }
        } else {
            Attribute a = dataset.getGreatestGainAttribute();
            System.out.println(tabs + a.getName() + "");
            for (Map.Entry<String, Dataset> e : a.getSubdatasets().entrySet()) {
                System.out.println(tabs + "\t" + e.getKey() + ":");
                recursiveEntropy(e.getValue(), iter + 1);
            }
        }
    }
}
