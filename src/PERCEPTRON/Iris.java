package PERCEPTRON;

import java.util.List;
import java.util.Vector;

public class Iris
{
    private final Vector<Double> attributes;
    private final String className;

    public Iris(List<Double> attributes, String className)
    {
        this.attributes = new Vector<>(attributes);
        this.className = className;
    }

    public Vector<Double> getAttributes()
    {return attributes;}

    public double getAttributesAt(int x)
    {return attributes.get(x);}

    public String getClassName()
    {return className;}

    @Override
    public String toString()
    {return attributes + " " + className;}
}