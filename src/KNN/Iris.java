package KNN;

import java.util.Arrays;

class Iris
{
    private final double[] attributes;
    private final String className;
    private double distance;

    public Iris(double[] attributes, String className)
    {
        this.attributes = attributes;
        this.className = className;
    }

    public Iris(Iris iris, double distance)
    {
        this.attributes = Arrays.copyOf(iris.attributes, iris.attributes.length);
        this.className = iris.getName();
        this.distance = distance;
    }

    public double getVector(int index)
    {return attributes[index];}

    public String getName()
    {return className;}

    public int getLength()
    {return attributes.length;}

    public double getDistance()
    {return distance;}

    @Override
    public String toString()
    {return Arrays.toString(attributes) + " " + className;}
}