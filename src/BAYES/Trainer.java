package BAYES;

import java.io.*;
import java.util.*;

public class Trainer
{
    static int ATTRIBUTES_LENGTH = 4;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {System.err.println("usage: java Trainer.java <training-set.csv> <train-set.csv>"); return;}

        List<List<String>> trainData = parseFile(args[0], true);
        List<List<String>> testData = parseFile(args[1], false);

        if (trainData.isEmpty() || testData.isEmpty())
        {System.err.println("Nie można przetworzyć danych: dane treningowe lub testowe są puste."); return;}

        testData.forEach(rowData -> compute(trainData, rowData));

        testData.forEach((rowData) -> compute(trainData, rowData));
    }

    public static void compute(List<List<String>> trainData, List<String> rowData)
    {
        if (rowData.size() < ATTRIBUTES_LENGTH)
        {System.err.println("Pominięto wiersz testowy z niepełnymi danymi: " + rowData); return;}

        System.out.println(
                "====================================================================================================" + "\n" +
                        "Dla wartości: " + rowData);

        Set<String> uniqueDecisionValues = new HashSet<>();

        for (List<String> decisionAttribute : trainData)
        {uniqueDecisionValues.add(decisionAttribute.get(decisionAttribute.size() - 1));}

        for (String decisionAttributeValue : uniqueDecisionValues)
        {
            double[] probabilities = new double[ATTRIBUTES_LENGTH + 1];

            for (int x = 0; x < ATTRIBUTES_LENGTH; x++)
            {
                String attribute = rowData.get(x);

                int attributesOnIndexCount = 0;
                int playOnIndexCount = 0;

                Set<String> uniqueOnIndexValues = new HashSet<>();

                for (int y = 0; y < trainData.size(); y++)
                {
                    String attributeValue = trainData.get(y).get(x);

                    if (attributeValue.equals(attribute))
                    {attributesOnIndexCount++;}

                    List<String> decisionAttribue = trainData.get(y);

                    if (decisionAttribue.get(decisionAttribue.size() - 1).equals(decisionAttributeValue))
                    {playOnIndexCount++;}

                    uniqueOnIndexValues.add(attributeValue);
                }

                if (attributesOnIndexCount == 0)
                {
                    attributesOnIndexCount = 1;
                    playOnIndexCount += uniqueOnIndexValues.size();
                }

                probabilities[x] = (double) attributesOnIndexCount / (double) playOnIndexCount;
            }

            int decisionAttributeValueCount = 0;
            int totalDecisionAttributeValues = 0;

            for (List<String> decisionAttribute : trainData)
            {
                for (String attributeValue : decisionAttribute)
                {
                    if (attributeValue.equals(decisionAttributeValue))
                    {decisionAttributeValueCount++;}

                    totalDecisionAttributeValues++;
                }
            }

            probabilities[probabilities.length - 1] = (double) decisionAttributeValueCount / (double) totalDecisionAttributeValues;
            double probability = Arrays.stream(probabilities).reduce(1.0, (a, b) -> a * b);

            System.out.println(
                    "Kolejne prawdopodobieństwa: " + Arrays.toString(probabilities) + "\n" +
                            decisionAttributeValue + " = " + probability);
        }
    }

    public static List<List<String>> parseFile(String fileName, boolean isTrainingFile)
    {
        List<List<String>> dataArrayList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null)
            {
                if (line.trim().isEmpty())
                {lineNumber++; continue;}

                List<String> values = Arrays.asList(line.trim().split(","));

                if (isTrainingFile && values.size() < ATTRIBUTES_LENGTH + 1)
                {System.err.println("Pominięto niepełny wiersz treningowy w linii " + lineNumber + " " + values);}
                else if (!isTrainingFile && values.size() < ATTRIBUTES_LENGTH)
                {System.err.println("Pominięto niepełny wiersz testowy w linii " + lineNumber + " " + values);}
                else
                {dataArrayList.add(values);}

                lineNumber++;
            }
        }
        catch (IOException exception)
        {System.err.println("parseFile exception: " + exception.getMessage());}

        return dataArrayList;
    }
}
