package ONE_LAYERED_NEURAL_NETWORK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class UI extends JFrame
{
    private final JTextArea messagesTextArea;
    private final JTextField userInputField;
    private final JButton runEpochTrainButton;
    private final JButton viewPerceptronStatsButton;

    private static int epochCount = 0;

    public UI(List<Perceptron> perceptronList, List<Language> languageTrainList, String[] args, String availableLanguages)
    {
        setTitle("Perceptron test " + Arrays.toString(args));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 920);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        messagesTextArea = createTextArea();
        userInputField = createTextField();
        runEpochTrainButton = createButton("Run Epoch");
        viewPerceptronStatsButton = createButton("Show Stats");

        JPanel mainPanel = createMainPanel(messagesTextArea);
        JPanel bottomPanel = createBottomPanel(userInputField, perceptronList);
        JPanel upperPanel = createUpperPanel(availableLanguages);

        addActionListeners(perceptronList, languageTrainList);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(upperPanel, BorderLayout.NORTH);

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

    private JPanel createBottomPanel(JTextField userInputField, List<Perceptron> perceptronList)
    {
        JPanel panel = new JPanel(new BorderLayout());

        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(createInputFieldAction(perceptronList));

        panel.add(userInputField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createUpperPanel(String availableLanguages)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(43, 45, 48));

        JLabel hintLabel = new JLabel("Type in text in some language: " + availableLanguages);
        hintLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        hintLabel.setForeground(new Color(222, 223, 226));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 0));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 0, 0));
        buttonPanel.setBackground(panel.getBackground());
        buttonPanel.add(runEpochTrainButton);
        buttonPanel.add(viewPerceptronStatsButton);

        panel.add(hintLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    private void addActionListeners(List<Perceptron> perceptronList, List<Language> languageTrainList)
    {
        runEpochTrainButton.addActionListener(event ->
        {
            epochCount++;
            Trainer.runTraining(perceptronList, languageTrainList);
            appendMessage("Training complete (" + epochCount + ") - visited " + languageTrainList.size() + " texts.\n");
        });

        viewPerceptronStatsButton.addActionListener(event -> perceptronList.forEach(perceptron -> appendMessage(perceptron + "\n")));
        userInputField.addActionListener(createInputFieldAction(perceptronList));
    }

    private ActionListener createInputFieldAction(List<Perceptron> perceptronList)
    {
        return event ->
        {
            String text = userInputField.getText();
            Language language = new Language("");

            text = text.toUpperCase();

            text.chars().forEach(c ->
            {
                if ('A' <= c && c <= 'Z')
                {language.raiseAttributeAt(c - 'A');}
            });

            Map<String, Double> computedValues = new HashMap<>();

            appendMessage("\n");
            for (Perceptron perceptron : perceptronList)
            {
                double y = perceptron.compute(language);
                computedValues.put(perceptron.getPositiveOutput(), y);
                appendMessage(perceptron.getPositiveOutput() + " = " + y + "\n");
            }
            appendMessage("\n");

            Map.Entry<String, Double> maxComputedValue = Collections.max(computedValues.entrySet(), Map.Entry.comparingByValue());

            appendMessage("Predicted: " + maxComputedValue.getKey() + " with activated value of " + maxComputedValue.getValue() + "\n");

            userInputField.setText("");
        };
    }

    private void appendMessage(String message)
    {messagesTextArea.setText(messagesTextArea.getText() + message);}
}
