package ONE_LAYERED_NEURAL_NETWORK;

import javax.swing.*;
import java.util.*;

public class Trainer
{
    static List<Language> languageTrainList = new ArrayList<>();
    static List<Perceptron> perceptronList = new ArrayList<>();
    static Set<String> languageNames = new HashSet<>();
    static String availableLanguages = "";
    static double alpha;
    static UI window;

    public static void main(String[] args)
    {
        if (args.length != 2)
        {System.err.println("Ustawienie argumentów: alpha train-set-catalog"); return;}

        try
        {alpha = Double.parseDouble(args[0]);}
        catch (NumberFormatException exception)
        {System.err.println("alpha parameter must be an integer. "); return;}

        FileParser.processDir(args[1], languageTrainList);
        languageTrainList.forEach(language -> languageNames.add(language.getClassName()));

        languageNames.forEach(name ->
        {
            perceptronList.add(new Perceptron(alpha, name));
            availableLanguages += name + " ";
        });
        Collections.shuffle(languageTrainList);

        SwingUtilities.invokeLater(() -> window = new UI(perceptronList, languageTrainList, args, availableLanguages));
    }
    public static void runTraining(List<Perceptron> perceptronList, List<Language> languageTrainList)
    {
        for (Perceptron perceptron : perceptronList)
        {
            for (Language language : languageTrainList)
            {perceptron.learn(language);}
        }
    }
}