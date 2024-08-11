package com.ahmet.DockerSpringBootMongoDB.validator;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class JMXValidator {

    public static void main(String[] args) {
        String directoryPath = "src/test/resources/performance";
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".jmx"));

        if (files != null) {
            for (File file : files) {
                validateJMXFile(file);
            }
        }
    }

    private static void validateJMXFile(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            // Validate XML structure
            if (!doc.getDocumentElement().getNodeName().equals("jmeterTestPlan")) {
                System.out.println("Error in file: " + file.getName() + " - Root element is not <jmeterTestPlan>");
                return;
            }

            // Check for common errors
            NodeList testPlanList = doc.getElementsByTagName("TestPlan");
            if (testPlanList.getLength() == 0) {
                System.out.println("Error in file: " + file.getName() + " - No <TestPlan> element found");
            }

            // Additional checks can be added here

            System.out.println("File " + file.getName() + " is valid.");
        } catch (Exception e) {
            System.out.println("Error in file: " + file.getName() + " - " + e.getMessage());
        }
    }
}