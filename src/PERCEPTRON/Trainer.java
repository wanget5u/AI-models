package PERCEPTRON;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Trainer
{
    static List<Iris> irisTrainList = new ArrayList<>();
    static List<Iris> irisTestList = new ArrayList<>();

    static int FIXED_VECTOR_SIZE = 4;
    static double alpha;

    static UI window;

    public static void main(String[] args)
    {
        if (args.length != 3)
        {System.err.println("Ustawienie argumentów: alpha train-set test-set"); return;}

        try
        {alpha = Double.parseDouble(args[0]);}
        catch (NumberFormatException exception)
        {System.err.println("alpha parameter must be an integer. "); return;}

        try
        {
            parseFile(args[1], irisTrainList);
            parseFile(args[2], irisTestList);
        }
        catch (IOException exception)
        {System.err.println("Error reading file: " + exception.getMessage());}

        Perceptron perceptron = new Perceptron(FIXED_VECTOR_SIZE, alpha, "Iris-setosa");

        SwingUtilities.invokeLater(() -> window = new UI(perceptron, irisTrainList, irisTestList, args));
    }
    public static String runTestSamples(Perceptron perceptron, List<Iris> irisTestList)
    {
        int correctGuesses = 0;
        int totalGuesses = 0;

        for (Iris irisTest : irisTestList)
        {
            int y = perceptron.compute(irisTest);

            if (y == 1)
            {
                System.out.println("Predicted: " + perceptron.getPositiveOutput());

                if (irisTest.getClassName().equals(perceptron.getPositiveOutput()))
                {correctGuesses++;}
            }
            else
            {
                System.out.println("Predicted: Not " + perceptron.getPositiveOutput());

                if (!irisTest.getClassName().equals(perceptron.getPositiveOutput()))
                {correctGuesses++;}
            }

            totalGuesses++;
        }

        return "Correct guesses: " + correctGuesses + "\n" +
               "Total guesses: " + totalGuesses + "\n" +
               "Accuracy: " + Math.round((float) correctGuesses / totalGuesses * 100) + "%\n\n";
    }
    public static void runTraining(Perceptron perceptron, List<Iris> irisTrainList)
    {
        for (Iris irisTrain : irisTrainList)
        {perceptron.learn(irisTrain);}
    }
    public static void parseFile(String fileName, List<Iris> irisList) throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] data = line.split(";");

                try
                {
                    if (data.length != FIXED_VECTOR_SIZE + 1)
                    {throw new ArrayIndexOutOfBoundsException();}

                    List<Double> attributes = IntStream
                            .range(0, FIXED_VECTOR_SIZE)
                            .mapToObj(index -> Double.parseDouble(data[index]))
                            .collect(Collectors.toList());

                    irisList.add(new Iris(attributes, data[FIXED_VECTOR_SIZE]));
                }
                catch (NumberFormatException formatException)
                {System.out.println("Skipping invalid data: '" + line + "' ");}
                catch (ArrayIndexOutOfBoundsException indexOutOfBoundsException)
                {System.out.println("Skipping invalid amount of arguments: '" + line + "' ");}
            }
        }
    }
}