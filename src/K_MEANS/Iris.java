package K_MEANS;

import java.util.List;
import java.util.Vector;

public class Iris
{
    private final Vector<Double> attributes;
    private final String className;
    private int groupNumber;

    public Iris(List<Double> attributes, String className, int groupName)
    {
        this.attributes = new Vector<>(attributes);
        this.className = className;
        this.groupNumber = groupName;
    }

    public int getGroupNumber()
    {return groupNumber;}

    public void setGroupNumber(int groupNumber)
    {this.groupNumber = groupNumber;}

    public Vector<Double> getAttributes()
    {return attributes;}

    public double getAttributesAt(int x)
    {return attributes.get(x);}

    public String getClassName()
    {return className;}

    @Override
    public String toString()
    {return attributes + " | " + groupNumber + " | " + className;}
}