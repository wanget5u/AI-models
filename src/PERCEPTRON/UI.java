package PERCEPTRON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class UI extends JFrame
{
    private final JTextArea messagesTextArea;
    private final JTextField userInputField;
    private final JButton runTestSamplesButton;
    private final JButton runEpochTrainButton;
    private final JButton viewPerceptronStatsButton;
    private final JButton shuffleTrainFileButton;

    private static int epochCount = 0;
    private final String[] args;

    private final Perceptron perceptron;

    public UI(Perceptron perceptron, List<Iris> irisTrainList, List<Iris> irisTestList, String[] args)
    {
        this.args = args;
        this.perceptron = perceptron;

        setTitle("Perceptron test " + Arrays.toString(args));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 920);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        messagesTextArea = createTextArea();
        userInputField = createTextField();
        runTestSamplesButton = createButton("Run Test");
        runEpochTrainButton = createButton("Run Epoch");
        viewPerceptronStatsButton = createButton("Show Stats");
        shuffleTrainFileButton = createButton("Shuffle");

        JPanel mainPanel = createMainPanel(messagesTextArea);
        JPanel bottomPanel = createBottomPanel(userInputField);
        JPanel upperPanel = createUpperPanel();

        addActionListeners(perceptron, irisTrainList, irisTestList);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(upperPanel, BorderLayout.NORTH);

        messagesTextArea.setText(perceptron.toString());

        setVisible(true);

        userInputField.requestFocusInWindow();
    }

    private JTextArea createTextArea()
    {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 17));
        textArea.setBackground(new Color(30, 31, 31));
        textArea.setForeground(new Color(222, 223, 226));
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        return textArea;
    }

    private JTextField createTextField()
    {
        JTextField textField = new JTextField();
        textField.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        textField.setBackground(new Color(30, 31, 31));
        textField.setForeground(new Color(222, 223, 226));
        textField.setCaretColor(textField.getForeground());
        textField.setBorder(BorderFactory.createLineBorder(textField.getForeground(), 2));
        return textField;
    }

    private JButton createButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        button.setBackground(new Color(75, 76, 79));
        button.setForeground(new Color(222, 223, 226));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createMainPanel(JTextArea textArea)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 31, 31));
        panel.setBorder(BorderFactory.createLineBorder(new Color(43, 45, 48), 8));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel(JTextField userInputField)
    {
        JPanel panel = new JPanel(new BorderLayout());

        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(createInputFieldAction());

        panel.add(userInputField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createUpperPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(43, 45, 48));

        JLabel hintLabel = new JLabel("Type in a vector (example format: " + generateRandomVector() + ")");
        hintLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        hintLabel.setForeground(new Color(222, 223, 226));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 0, 0));
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(shuffleTrainFileButton);
        buttonPanel.add(runEpochTrainButton);
        buttonPanel.add(runTestSamplesButton);
        buttonPanel.add(viewPerceptronStatsButton);

        panel.add(hintLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    private void addActionListeners(Perceptron perceptron, List<Iris> irisTrainList, List<Iris> irisTestList)
    {
        runTestSamplesButton.addActionListener(event ->
        {
            String message = Trainer.runTestSamples(perceptron, irisTestList);
            appendMessage(message);
        });

        runEpochTrainButton.addActionListener(event ->
        {
            epochCount++;
            Trainer.runTraining(perceptron, irisTrainList);
            appendMessage("Training complete (" + epochCount + ") - visited " + irisTrainList.size() + " samples.\n");
        });

        shuffleTrainFileButton.addActionListener(event ->
        {
            try
            {
                List<String> lines = Files.readAllLines(Paths.get(args[1]));

                if (lines.isEmpty())
                {System.out.println("File is empty. "); return;}

                String header = lines.get(0);
                List<String> data = lines.subList(1, lines.size());

                Collections.shuffle(data, new Random());

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(args[1])))
                {
                    writer.write(header);
                    writer.newLine();

                    for (String line : data)
                    {writer.write(line); writer.newLine();}
                }

                irisTrainList.clear();

                Trainer.parseFile(args[1], irisTrainList);
            }
            catch (IOException exception)
            {System.err.println("Error reading file: " + exception.getMessage());}

            appendMessage(args[1] + " has been successfully shuffled. \n");
        });

        viewPerceptronStatsButton.addActionListener(event -> appendMessage(perceptron.toString()));
        userInputField.addActionListener(createInputFieldAction());
    }

    private ActionListener createInputFieldAction()
    {
        return event ->
        {
            StringBuilder builder = new StringBuilder(messagesTextArea.getText());
            String[] inputData = userInputField.getText().split(";");
            Double[] attributes = new Double[Trainer.FIXED_VECTOR_SIZE];

            try
            {
                if (inputData.length != Trainer.FIXED_VECTOR_SIZE)
                {throw new ArrayIndexOutOfBoundsException();}

                for (int i = 0; i < Trainer.FIXED_VECTOR_SIZE; i++)
                {attributes[i] = Double.parseDouble(inputData[i]);}

                int y = perceptron.compute(new Iris(Arrays.asList(attributes), ""));
                builder.append(y == 1 ? "Predicted: " + perceptron.getPositiveOutput() + "\n" : "Predicted: Not " + perceptron.getPositiveOutput() + "\n");

            }
            catch (ArrayIndexOutOfBoundsException exception)
            {builder.append("Vector must consists of specifically ").append(Trainer.FIXED_VECTOR_SIZE).append(" arguments. \n");}
            catch (NumberFormatException exception)
            {builder.append("Invalid input. Try again.\n");}

            messagesTextArea.setText(builder.toString());
            userInputField.setText("");
        };
    }

    private void appendMessage(String message)
    {messagesTextArea.setText(messagesTextArea.getText() + message);}

    private String generateRandomVector()
    {
        Random random = new Random();
        StringBuilder exampleVector = new StringBuilder();

        for (int i = 0; i < Trainer.FIXED_VECTOR_SIZE; i++)
        {
            exampleVector.append(Math.round((random.nextDouble() - 5) * 10.0) / 10.0);

            if (i != Trainer.FIXED_VECTOR_SIZE - 1)
            {exampleVector.append(";");}
        }
        return exampleVector.toString();
    }
}
