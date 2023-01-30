package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsertionScreen {
    private record Line(JLabel label, JTextField field) {}

    public InsertionScreen(Connection connection) throws SQLException {
        JFrame frame = new JFrame("Schermata di inserimento");
        JPanel panel = new JPanel();
        final JPanel[] topGrid = {new JPanel()};
        JComboBox<String> tables = Utils.compileTables(connection);
        JButton compileFrame = new JButton("OK");
        JButton executeInsertion = new JButton("Inserisci");
        JTextArea outputArea = new JTextArea();
        JScrollPane scroller = new JScrollPane(outputArea);

        outputArea.setEditable(false);
        outputArea.setVisible(true);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        compileFrame.addActionListener(
                actionEvent -> {
                    try {
                        outputArea.setText("");
                        String table = (String) tables.getSelectedItem();
                        List<Line> lines = compileLines(connection, table);
                        panel.remove(topGrid[0]);
                        topGrid[0] = new JPanel();
                        topGrid[0].setLayout(new GridLayout(lines.size() + 1, 1));
                        topGrid[0].add(tables);
                        topGrid[0].add(compileFrame);

                        for(Line l : lines) {
                            topGrid[0].add(l.label());
                            topGrid[0].add(l.field());
                        }
                        topGrid[0].add(executeInsertion);
                        panel.add(topGrid[0], BorderLayout.CENTER);
                        frame.setVisible(false);
                        frame.setVisible(true);
                    } catch(SQLException e) {
                        outputArea.setText("C'è stato un errore:\n" + e.getMessage());
                    }

                }
        );

        executeInsertion.addActionListener(
                actionEvent -> {
                    //TODO: insertion :|
                }
        );

        frame.add(panel);
        panel.setLayout(new BorderLayout());
        panel.add(topGrid[0], BorderLayout.CENTER);
        panel.add(scroller, BorderLayout.SOUTH);
        topGrid[0].setLayout(new GridLayout(1,2));
        topGrid[0].add(tables);
        topGrid[0].add(compileFrame);

        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private List<Line> compileLines(Connection connection, String table) throws SQLException {
        List<Line> lines = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from " + table.replace(' ', '_'));
        ResultSetMetaData metaData = result.getMetaData();

        int columnNumber = metaData.getColumnCount();
        for(int i = 1; i <= columnNumber; i++) {
            lines.add(new Line(new JLabel(metaData.getColumnName(i)), new JTextField()));
        }

        result.close();
        statement.close();
        return lines;
    }
}
