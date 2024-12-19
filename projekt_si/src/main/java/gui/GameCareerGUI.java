package gui;

import models.Answer;
import models.PossibleAnswers;
import models.Question;
import rules.DecisionEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameCareerGUI {
    private JFrame frame;
    private JLabel questionLabel;
    private DecisionEngine decisionEngine;
    private Question currentQuestion;
    private JComboBox<String> comboBox;

    public GameCareerGUI() {
        decisionEngine = new DecisionEngine(); // Inicjalizacja silnika regu³
        setupUI();
        loadFirstQuestion();
    }

    private void setupUI() {
        frame = new JFrame("Game Career Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        questionLabel = new JLabel("Welcome! Let's find your career path in games.", SwingConstants.CENTER);
        frame.add(questionLabel, BorderLayout.CENTER);

        JPanel comboBoxPanel = new JPanel();
        comboBox = new JComboBox<>(new String[]{"Select an option", "Yes", "No"});
        comboBoxPanel.add(comboBox);

        JButton submitButton = new JButton("Submit");
        comboBoxPanel.add(submitButton);

        frame.add(comboBoxPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> handleAnswer());

        frame.setVisible(true);
    }

    private void loadFirstQuestion() {
        currentQuestion = decisionEngine.getFirstQuestion();
        questionLabel.setText(currentQuestion.getText());
        comboBox.removeAllItems();
        comboBox.addItem("Yes");
        comboBox.addItem("No");
    }


    private void handleAnswer() {
        String selectedOption = (String) comboBox.getSelectedItem();
        if (selectedOption != null) {
            Answer userAnswer = new Answer(currentQuestion, selectedOption);
            System.out.println("User answered: " + selectedOption); 

            currentQuestion = decisionEngine.getNextQuestion(userAnswer);
            PossibleAnswers answers = new PossibleAnswers();
            answers = decisionEngine.getPossibleAnswers(userAnswer);

            if (currentQuestion != null) {
                questionLabel.setText(currentQuestion.getText());
                comboBox.removeAllItems();
                for(String answer : answers.answers) {
                	comboBox.addItem(answer);
                }
                System.out.println("Next question: " + currentQuestion.getText()); // Log next question
            } else {
            	questionLabel.setText(answers.answers[0]);
                comboBox.setEnabled(false);
                System.out.println("No more questions, career path found."); // Log end of questions
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select an option.");
        }
    }
}
