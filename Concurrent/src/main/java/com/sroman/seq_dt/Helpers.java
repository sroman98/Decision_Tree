package com.sroman.seq_dt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Helpers {
    private static ForkJoinPool commonPool;
    
    public static ForkJoinPool getCommonPool() {
        if(commonPool == null)
            commonPool = ForkJoinPool.commonPool();
        return commonPool;
    }
    
    public static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }
    
    static String[][] getMatrixFromCSV(String filePath) {
        List<String[]> rowList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        String[][] matrix = new String[rowList.size()][];
        for (int i = 0; i < rowList.size(); i++) {
            String[] row = rowList.get(i);
            matrix[i] = row;
        }
        return matrix;
    }
    
    static String[][] removeCol(String[][] data, int col) {
        int rows = data.length;
        int cols = data[0].length;

        String[][] newData = new String[rows][cols - 1];

        for (int i = 0; i < rows; i++) {
            int curr = 0;
            for (int j = 0; j < cols; j++) {
                if (j != col) {
                    newData[i][curr++] = data[i][j];
                }
            }
        }

        return newData;
    }
}
