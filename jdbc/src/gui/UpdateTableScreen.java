package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UpdateTableScreen {

    private static final String errMsg = "C'è stato un errore:\n";
    public UpdateTableScreen(Connection connection) throws SQLException {
        //instantiating GUI components
        JFrame frame = new JFrame("Aggiorna un record in Dipendente");
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JPanel leftPanel = new JPanel();
        JPanel topLeftPanel = new JPanel();
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel rightPanel = new JPanel();
        JPanel linesPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        JTextArea keysArea = new JTextArea();
        JTextArea outputArea = new JTextArea();
        JButton retrieveRecord = new JButton("Recupera");
        JButton updateRecord = new JButton("Aggiorna");
        JButton removeRecord = new JButton("Rimuovi");
        JScrollPane keysScrollPane = new JScrollPane(keysArea);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        List<Line> lines = Utils.compileLines(connection, "dipendente");

        //setting up properties for the GUI components
        keysArea.setVisible(true);
        keysArea.setEditable(false);
        keysScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        keysScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        try {
            keysArea.setText(getKeys(connection));
        } catch (SQLException e) {
            keysArea.setText("C'è stato un errore nel recupero delle chiavi per la tabella:\n" + e.getMessage());
        }

        outputArea.setVisible(true);
        outputArea.setEditable(false);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        retrieveRecord.addActionListener(
                actionEvent -> {
                    try {
                        String key = lines.get(0).field().getText();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("select * from dipendente where cf = \"" + key + "\"");

                        int i = 1;
                        resultSet.next();
                        for(Line l : lines) {
                            l.field().setText(resultSet.getString(i));
                            i++;
                        }

                        statement.close();
                        resultSet.close();

                    } catch (SQLException e) {
                        outputArea.setText(errMsg + e.getMessage());
                    }
                }
        );

        updateRecord.addActionListener(
                actionEvent -> {
                    try {
                        List<String> values = Utils.compileValues(lines, connection, "dipendente");
                        String key = lines.get(0).field().getText();
                        String setStatement = "";

                        int i = 0;
                        for(Line l : lines) {
                            setStatement += l.label().getText().replace(' ', '_') + " = " + values.get(i);
                            i++;
                        }

                        Statement statement = connection.createStatement();
                        statement.executeUpdate("update dipendente\nset " + setStatement + "\nwhere cf = \"" + key + "\"");

                        outputArea.setText("Aggiornamento riuscito. Nuovi valori:\n" + setStatement);
                        keysArea.setText(getKeys(connection));

                        statement.close();
                    } catch(SQLException e) {
                        outputArea.setText(errMsg + e.getMessage());
                    }
                }
        );

        removeRecord.addActionListener(
                actionEvent -> {
                    try {
                        String key = lines.get(0).field().getText();

                        Statement statement = connection.createStatement();
                        statement.executeUpdate("delete from dipendente where cf = \"" + key + "\"");

                        outputArea.setText("Record con chiave \"" + key + "\" eliminato con successo.");
                        keysArea.setText(getKeys(connection));

                        statement.close();
                    } catch(SQLException e) {
                        outputArea.setText(errMsg + e.getMessage());
                    }
                }
        );

        //building the GUI
        frame.add(splitPane);

        splitPane.add(keysScrollPane);
        splitPane.add(rightSplitPane);

        rightSplitPane.add(rightPanel);

        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(linesPanel, BorderLayout.NORTH);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        linesPanel.setLayout(new GridLayout(lines.size() + 1, 2));
        for(Line l : lines) {
            linesPanel.add(l.label());
            linesPanel.add(l.field());
        }

        buttonsPanel.add(retrieveRecord);
        buttonsPanel.add(updateRecord);
        buttonsPanel.add(removeRecord);

        rightSplitPane.add(outputScrollPane);

        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private String getKeys(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        return Utils.printTable(statement.executeQuery("select cf from dipendente"));
    }
}
