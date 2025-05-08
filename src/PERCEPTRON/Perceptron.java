package PERCEPTRON;

import java.util.Random;
import java.util.Vector;
import java.util.stream.IntStream;

public class Perceptron
{
    private final Vector<Double> weights;
    private final double alpha;
    private final String positiveOutput;
    private double threshold;

    public Perceptron(int vectorSize, double alpha, String positiveOutput)
    {
        Random random = new Random();

        this.weights = new Vector<>();

        for (int x = 0; x < vectorSize; x++)
        {this.weights.add((random.nextDouble() * 10) - 5);}

        this.threshold = (random.nextDouble() * 10) - 1;
        this.alpha = alpha;

        this.positiveOutput = positiveOutput;
    }

    // Wektor wag W=[w₁,w₂,…,wn]
    // Wartość oczekiwana d
    // Wyjście y
    // Stała uczenia 𝛼
    // Wektor wejściowy X=[x₁,x₂,…,xn]
    // Próg t
    public void learn(Iris iris)
    {
        // net = X ∘ W - t
        // net ≥ 0 ⟹ y = 1
        // net < 0 ⟹ y = 0

        double net = calculateNet(iris);

        int y = (net >= 0) ? 1 : 0;
        int d = isPositiveOutput(iris.getClassName());

        // W’= W +(d-y)𝛼 X
        for (int x = 0; x < weights.size(); x++)
        {weights.set(x, weights.get(x) + (d - y) * alpha * iris.getAttributesAt(x));}

        // t’= t -(d-y)𝛼
        threshold -= (d - y) * alpha;
    }

    public int compute(Iris iris)
    {
        double net = calculateNet(iris);

        if (!iris.getClassName().isEmpty())
        {System.out.print("Net = " + Math.round(net * 1000.0) / 1000.0  + "; Expected: " + iris.getClassName() + "; ");}

        // net = X ∘ W - t
        // net ≥ 0 ⟹ y = 1
        // net < 0 ⟹ y = 0

        return net >= 0 ? 1 : 0;
    }

    public int isPositiveOutput(String className)
    {return className.equals(positiveOutput) ? 1 : 0;}

    public double calculateNet(Iris iris)
    {return dotProduct(weights, iris.getAttributes()) - threshold;}

    public double dotProduct(Vector<Double> v1, Vector<Double> v2)
    {
        return IntStream
                .range(0, v1.size())
                .mapToDouble(index -> v1.get(index) * v2.get(index))
                .sum();
    }

    public String getPositiveOutput()
    {return positiveOutput;}

    @Override
    public String toString()
    {
        return "Running perceptron: " + "\n" +
                "W = " + weights + "\n" +
                "Alpha = " + alpha + "\n" +
                "Threshold " + threshold + "\n\n";
    }
}