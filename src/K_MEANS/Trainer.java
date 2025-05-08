package K_MEANS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Trainer
{
    public static final int FIXED_VECTOR_SIZE = 4;
    public static final List<Iris> irisData = new ArrayList<>();
    public static int k;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {System.err.println("usage: java Trainer <k> <sample.csv>"); return;}

        k = Integer.parseInt(args[0]);

        parseFile(args[1], irisData);

        compute(irisData);

        for (int x = 1; x < k + 1; x++)
        {
            int groupNumber = x;
            irisData
                    .stream()
                    .filter(iris -> iris.getGroupNumber() == groupNumber)
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
        }
    }

    public static void compute(List<Iris> irisData)
    {
        boolean changed = true;

        while (changed)
        {
            changed = false;

            List<List<Double>> centroids = new ArrayList<>();

            for (int x = 1; x < k + 1; x++)
            {
                int finalX = x;
                List<Iris> group = irisData
                        .stream()
                        .filter(iris -> iris.getGroupNumber() == finalX)
                        .collect(Collectors.toList());

                List<Double> centroid = new ArrayList<>();

                for (int y = 0; y < FIXED_VECTOR_SIZE; y++)
                {
                    int finalY = y;
                    double average = group
                            .stream()
                            .mapToDouble(iris -> iris.getAttributesAt(finalY))
                            .average()
                            .orElse(0);

                    centroid.add(average);
                }

                centroids.add(centroid);
            }

            for (Iris iris : irisData)
            {
                int currentGroup = iris.getGroupNumber();
                int closestGroup = -1;
                double minDistance = Double.MAX_VALUE;

                for (int x = 0; x < centroids.size(); x++)
                {
                    List<Double> centroid = centroids.get(x);
                    double distance = 0d;

                    for (int y = 0; y < FIXED_VECTOR_SIZE; y++)
                    {distance += Math.pow(iris.getAttributesAt(y) - centroid.get(y), 2);}

                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        closestGroup = x + 1;
                    }
                }

                if (currentGroup != closestGroup)
                {
                    iris.setGroupNumber(closestGroup);
                    changed = true;
                }
            }
        }
    }

    public static void parseFile(String fileName, List<Iris> irisList)
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

                    irisList.add(new Iris(attributes, data[FIXED_VECTOR_SIZE], ThreadLocalRandom.current().nextInt(1, k + 1)));
                }
                catch (NumberFormatException formatException)
                {System.out.println("Skipping invalid data: '" + line + "' ");}
                catch (ArrayIndexOutOfBoundsException indexOutOfBoundsException)
                {System.out.println("Skipping invalid amount of arguments: '" + line + "' ");}
            }
        }
        catch (IOException exception)
        {System.err.println("parseFile() exception: " + exception.getMessage());}
    }
}