package KNN;

import java.io.*;
import java.util.*;

public class KNN
{
    static List<Iris> irisTrainList = new ArrayList<>();
    static List<Iris> irisTestList = new ArrayList<>();
    static int k;
    static int fixedVectorSize = 4;

    public static void main(String[] args)
    {
        if (args.length != 3)
        {System.err.println("Arguments: java KNN <k> <train-set file name> <test-set file name>. "); return;}

        try
        {k = Integer.parseInt(args[0]);}
        catch (NumberFormatException exception)
        {System.err.println("K parameter must be an integer. "); return;}

        try
        {
            parseFile(args[1], irisTrainList);
            parseFile(args[2], irisTestList);

            if (k > irisTrainList.size())
            {System.err.println("K parameter exceeds the amount of training samples. "); System.exit(1);}

            runKNN();

            userVectorInputHandler();
        }
        catch (IOException exception)
        {System.err.println("Error reading file: " + exception.getMessage());}
    }
    private static void userVectorInputHandler()
    {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("exit"))
        {
            System.out.println("Type in a vector (example 5.1;3.5;1.4;0.2) or type 'exit' to abort: ");
            input = scanner.nextLine();

            String[] inputData = input.split(";");
            double[] attributes = new double[fixedVectorSize];
            try
            {
                if (inputData.length != fixedVectorSize)
                {throw new ArrayIndexOutOfBoundsException();}

                for (int x = 0; x < fixedVectorSize; x++)
                {attributes[x] = Double.parseDouble(inputData[x]);}

                Iris irisTest = new Iris(attributes, "");
                String predictedClassName = getDominantClassName(getSortedNeighbors(irisTest));

                System.out.println("Predicted: " + predictedClassName);
            }
            catch (ArrayIndexOutOfBoundsException exception)
            {
                if (!input.equals("exit"))
                {System.out.println("Vector must consists of specifically " + fixedVectorSize + " arguments. ");}
            }
            catch (NumberFormatException exception)
            {System.err.println("Invalid input data. Try again. ");}
        }
    }
    public static void runKNN()
    {
        int correctGuesses = 0;
        int totalGuesses = 0;

        for (Iris irisTest : irisTestList)
        {
            String dominantClassName = getDominantClassName(getSortedNeighbors(irisTest));

            System.out.println("Expected: " + irisTest.getName() + "; Predicted: " + dominantClassName);

            if (irisTest.getName().equals(dominantClassName))
            {correctGuesses++;}

            totalGuesses++;
        }

        System.out.println("\n" +
                "Correct guesses: " + correctGuesses + "\n" +
                "Total guesses: " + totalGuesses + "\n" +
                "Accuracy: " + Math.round((float) correctGuesses / totalGuesses * 100) + "% \n");
    }
    private static List<Iris> getSortedNeighbors(Iris irisTest)
    {
        List<Iris> neighbors = new ArrayList<>();

        for (Iris irisTrain : irisTrainList)
        {neighbors.add(new Iris(irisTrain, euclideanDistance(irisTest, irisTrain)));}

        neighbors.sort(Comparator.comparingDouble(Iris::getDistance));

        return neighbors;
    }
    private static String getDominantClassName(List<Iris> neighbors)
    {
        Map<String, Integer> classCounts = new HashMap<>();

        for (int x = 0; x < k; x++)
        {classCounts.put(neighbors.get(x).getName(), classCounts.getOrDefault(neighbors.get(x).getName(), 0) + 1);}

        return Collections.max(classCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
    public static void parseFile(String fileName, List<Iris> irisList) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName)))
        {
            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] data = line.split(";");
                double[] attributesVector = new double[fixedVectorSize];

                try
                {
                    if (data.length != fixedVectorSize + 1)
                    {throw new ArrayIndexOutOfBoundsException();}

                    for (int x = 0; x < fixedVectorSize; x++)
                    {attributesVector[x] = Double.parseDouble(data[x]);}

                    irisList.add(new Iris(attributesVector, data[data.length - 1]));
                }
                catch (NumberFormatException | ArrayIndexOutOfBoundsException exception)
                {System.err.println("Skipping invalid data: " + line);}
            }
        }
    }
    public static double euclideanDistance(Iris iris1, Iris iris2)
    {
        if (iris1.getLength() != iris2.getLength())
        {return -1;}

        double length = 0;

        for (int x = 0; x < iris1.getLength(); x++)
        {length += Math.pow((iris1.getVector(x) - iris2.getVector(x)), 2);}

        return Math.sqrt(length);
    }
}