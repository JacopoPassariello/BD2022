package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionGUI {

    private final JTextArea outputArea;

    private String username;
    private String password;
    private String dbName;
    private Connection connection;

    public ConnectionGUI() {
        JFrame frame = new JFrame("Log into Data Base");
        JPanel panel = new JPanel();
        JPanel inputsPanel = new JPanel();

        JLabel userLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel dbLabel = new JLabel("Data Base name: ");

        final JTextField userField = new JTextField(24);
        final JTextField passwordField = new JTextField(24);
        final JTextField dbField = new JTextField(24);

        outputArea = new JTextArea();

        JButton enter = new JButton("Crea connessione");

        frame.add(panel);

        panel.setLayout(new BorderLayout());
        panel.add(inputsPanel, BorderLayout.NORTH);
        panel.add(outputArea, BorderLayout.CENTER);

        inputsPanel.setLayout(new GridLayout(4, 2));
        inputsPanel.add(userLabel);
        inputsPanel.add(userField);
        inputsPanel.add(passwordLabel);
        inputsPanel.add(passwordField);
        inputsPanel.add(dbLabel);
        inputsPanel.add(dbField);
        inputsPanel.add(enter);

        outputArea.setEditable(false);
        enter.addActionListener(
                actionEvent -> {
                    try {
                        username = userField.getText();
                        password = passwordField.getText();
                        dbName = dbField.getText();
                        connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password);
                        new MainMenuGUI(connection, dbName);
                        frame.dispose();
                    }
                    catch(SQLException e) {
                        outputArea.setText("There was an error with connecting to the database.");
                    }
                }
        );

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
