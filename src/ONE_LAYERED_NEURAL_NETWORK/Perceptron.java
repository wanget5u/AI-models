package ONE_LAYERED_NEURAL_NETWORK;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.IntStream;

public class Perceptron
{
    private final List<Double> weights;
    private final String positiveOutput;
    private final double alpha;
    private double threshold;

    public Perceptron(double alpha, String positiveOutput)
    {
        Random random = new Random();

        this.weights = new Vector<>();
        for (char c = 'A'; c <= 'Z'; c++)
        {this.weights.add(random.nextDouble());}

        this.threshold = random.nextDouble();
        this.alpha = alpha;
        this.positiveOutput = positiveOutput;
    }

    public double sigmoid(double net)
    {return 1.0 / (1.0 + Math.exp(-net));}

    public double sigmoidDerivative(double net)
    {
        double sig = sigmoid(net);
        return sig * (1 - sig);
    }

    public void learn(Language language)
    {
        // net = X ∘ W - t
        double net = calculateNet(language);

        double y = sigmoid(net);
        int d = isPositiveOutput(language.getClassName());

        for (int x = 0; x < weights.size(); x++)
        {weights.set(x, weights.get(x) + (d - y) * alpha * sigmoidDerivative(net) * language.getAttributesAt(x));}

        threshold -= alpha * (d - y) * sigmoidDerivative(net);
    }

    public double compute(Language language)
    {
        double net = calculateNet(language);

//        if (!language.getClassName().isEmpty())
//        {System.out.print("Net = " + Math.round(net * 1000.0) / 1000.0 + "; Expected: " + language.getClassName() + "; ");}

        return sigmoid(net);
    }

    public int isPositiveOutput(String className)
    {return className.equals(positiveOutput) ? 1 : 0;}

    public double calculateNet(Language language)
    {return dotProduct(language.getAttributes()) - threshold;}

    public double dotProduct(List<Double> languageWeights)
    {
        return IntStream.range(0, weights.size())
                .mapToDouble(index -> weights.get(index) * languageWeights.get(index))
                .sum();
    }

    public String getPositiveOutput()
    {return positiveOutput;}

    @Override
    public String toString() {
        return "Running perceptron: " + "\n" +
                "Positive output = " + positiveOutput + "\n" +
                "W = " + weights + "\n" +
                "Alpha = " + alpha + "\n" +
                "Threshold " + threshold + "\n\n";
    }
}
