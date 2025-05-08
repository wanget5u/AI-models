package ONE_LAYERED_NEURAL_NETWORK;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileParser extends SimpleFileVisitor<Path>
{
    private String lastDirectoryName = "";
    private final List<Language> languageTrainList;

    private FileParser(List<Language> languageTrainList)
    {this.languageTrainList = languageTrainList;}

    public static void processDir(String directoryName, List<Language> languageTrainList)
    {
        Path path = Paths.get(directoryName);
        FileParser fileParser = new FileParser(languageTrainList);

        try
        {Files.walkFileTree(path, fileParser);}
        catch (IOException exception)
        {System.err.println("processDir error: " + exception.getMessage());}
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
    {
        lastDirectoryName = dir.getFileName().toString();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
    {
        if (!file.toString().endsWith(".txt"))
        {return FileVisitResult.CONTINUE;}

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file))))
        {
            String line;
            Language language = new Language(lastDirectoryName);

            while ((line = fileReader.readLine()) != null)
            {
                line = line.toUpperCase();
                line.chars().forEach(c ->
                {
                    if ('A' <= c && c <= 'Z')
                    {
                        language.raiseAttributeAt(c - 'A');
                    }
                });
            }

            languageTrainList.add(language);
        }
        catch (IOException exception)
        {System.err.println("visitFile error: " + file.getFileName());}

        return FileVisitResult.CONTINUE;
    }
}
