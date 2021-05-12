/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sroman.seq_dt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sroman
 */
public class Helpers {
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
}
