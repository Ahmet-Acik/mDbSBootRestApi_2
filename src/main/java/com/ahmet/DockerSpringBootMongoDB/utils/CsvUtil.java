package com.ahmet.DockerSpringBootMongoDB.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvUtil {

    public static void updateCsvFile(String filePath, List<String[]> data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}