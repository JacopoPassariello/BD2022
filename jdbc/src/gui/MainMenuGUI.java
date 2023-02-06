package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainMenuGUI {

    public MainMenuGUI(Connection connection, String dbName) {

        final String[] supportedOperations = new String[]{"Inserimento", "Visualizza tabella", "Aggiornamento", "Query diretta"};

        JFrame frame = new JFrame("Interfaccia database \"" + dbName + "\"");

        JPanel topPanel = new JPanel();
        JPanel panel = new JPanel();
        JLabel opLabel = new JLabel("Selezionare operazione: ");
        JComboBox<String> opCombo = new JComboBox<>(supportedOperations);
        JButton enter = new JButton("OK");
        JTextArea outputArea = new JTextArea();

        frame.add(panel);
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(outputArea, BorderLayout.CENTER);
        topPanel.add(opLabel);
        topPanel.add(opCombo);
        topPanel.add(enter);

        outputArea.setEditable(false);
        enter.addActionListener(
                actionEvent -> {
                    int choice = opCombo.getSelectedIndex();

                    try {
                        switch (choice) {
                            case 0 -> {
                                new InsertionScreen(connection);
                            }
                            case 1 -> {
                                new FullTableScreen(connection);
                            }
                            case 2 -> {
                                new UpdateTableScreen(connection);
                            }
                            case 3 -> {
                                new RawQueryScreen(connection);
                            }
                            default -> {
                                outputArea.setText("C'è stato un errore nella selezione dell'operazione da eseguire. Riprova.");
                            }
                        }
                    } catch(SQLException e) {
                        outputArea.setText("C'è stato un errore nell'accedere al database.");
                    }
                }
        );

        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
