package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainMenuGUI {

    private final Connection connection;

    public MainMenuGUI(Connection connection, String dbName) {
        this.connection = connection;

        JFrame frame = new JFrame("Interfaccia database \"" + dbName + "\"");

        JPanel topPanel = new JPanel();
        JPanel panel = new JPanel();
        JLabel opLabel = new JLabel("Selezionare operazione: ");
        JComboBox<String> opCombo = new JComboBox<>(new String[]{"Inserimento", "Visualizza tabella", "Errore"});
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

                    switch(choice) {
                        case 0 -> {
                            insertScreen();
                        }
                        case 1 -> {
                            try {
                                fullTableScreen();
                            } catch(SQLException e) {
                                outputArea.setText("C'è stato un errore nell'esecuzione dell'operazione.");
                            }
                        }
                        default -> {
                            outputArea.setText("C'è stato un errore nella selezione dell'operazione da eseguire. Riprova.");
                        }
                    }
                }
        );

        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void insertScreen() {

    }

    private void fullTableScreen() throws SQLException {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JPanel topRow = new JPanel();
        JLabel tableLabel = new JLabel("Scegliere la tabella: ");
        JButton enter = new JButton("Mostra");
        JTextArea outputArea = new JTextArea();
        JScrollPane scroller = new JScrollPane(outputArea);
        JComboBox<String> tables = new JComboBox<>();

        //populate the tables combo box
        Statement statement = connection.createStatement();
        ResultSet tablesSet = statement.executeQuery("show tables");

        while(tablesSet.next()) {
            tables.addItem(tablesSet.getString(1).replace('_', ' '));
        }

        outputArea.setEditable(false);
        outputArea.setVisible(true);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.setLayout(new BorderLayout());

        enter.addActionListener(
                actionEvent -> {
                    try {
                        outputArea.setText("");

                        String table = (String) tables.getSelectedItem();
                        ResultSet contents = statement.executeQuery("select * from " + table);
                        ResultSetMetaData metadata = contents.getMetaData();

                        int columnNumber = metadata.getColumnCount();
                        for(int i = 1; i <= columnNumber; i++) {
                            outputArea.setText(outputArea.getText() + metadata.getColumnName(i).replace('_', ' ') + "\t");
                        }
                        outputArea.setText(outputArea.getText() + "\n");
                        //TODO: stampare tutti i valori da ogni colonna

                    } catch (SQLException e) {
                        outputArea.setText("C'è stato un errore nell'esecuzione dell'operazione.");
                    }
                }
        );

        frame.add(panel);
        panel.add(topRow, BorderLayout.NORTH);
        panel.add(scroller, BorderLayout.CENTER);
        topRow.add(tableLabel);
        topRow.add(tables);
        topRow.add(enter);

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
