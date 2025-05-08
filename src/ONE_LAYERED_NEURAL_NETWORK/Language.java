package ONE_LAYERED_NEURAL_NETWORK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Language
{
    private final List<Integer> attributes;
    private final String className;

    public Language(String className)
    {
        this.attributes = new ArrayList<>(Collections.nCopies(26, 0));
        this.className = className;
    }

    public List<Double> getAttributes()
    {
        int total = attributes.stream().mapToInt(i -> i).sum();
        if (total == 0) return Collections.nCopies(26, 0d);

        List<Double> normalized = new ArrayList<>();

        for (int val : attributes)
        {normalized.add((double) val / total);}

        return normalized;
    }

    public Integer getAttributesAt(int index)
    {return attributes.get(index);}

    public void raiseAttributeAt(int index)
    {attributes.set(index, attributes.get(index) + 1);}

    public String getClassName()
    {return className;}

    @Override
    public String toString()
    {
        List<Character> alphabet = new ArrayList<>();

        for (char c = 'A'; c < 'Z'; c++)
        {alphabet.add(c);}

        return "Language: " + className + "\n" + alphabet + "\n" + attributes;
    }
}
