package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsertionScreen {
    public record Line(JLabel label, JTextField field) {}

    public InsertionScreen(Connection connection) throws SQLException {
        JFrame frame = new JFrame("Schermata di inserimento");
        JSplitPane panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        final JPanel[] topGrid = {new JPanel()};
        JComboBox<String> tables = Utils.compileTables(connection);
        JButton compileFrame = new JButton("OK");
        JButton executeInsertion = new JButton("Inserisci");
        JTextArea outputArea = new JTextArea();
        JScrollPane scroller = new JScrollPane(outputArea);
        final List<Line>[] lines = new List[]{new ArrayList<Line>()};

        outputArea.setEditable(false);
        outputArea.setVisible(true);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        compileFrame.addActionListener(
                actionEvent -> {
                    try {
                        outputArea.setText("");
                        String table = (String) tables.getSelectedItem();
                        lines[0] = compileLines(connection, table);
                        panel.remove(topGrid[0]);
                        topGrid[0] = new JPanel();
                        topGrid[0].setLayout(new GridLayout(lines[0].size() + 2, 1));
                        topGrid[0].add(tables);
                        topGrid[0].add(compileFrame);

                        for(Line l : lines[0]) {
                            topGrid[0].add(l.label());
                            topGrid[0].add(l.field());
                        }
                        topGrid[0].add(executeInsertion);
                        panel.add(topGrid[0]);
                        frame.setVisible(false);
                        frame.setVisible(true);
                    } catch(SQLException e) {
                        outputArea.setText("C'è stato un errore:\n" + e.getMessage());
                    }

                }
        );

        executeInsertion.addActionListener(
                actionEvent -> {
                    try {
                        String table = (String) tables.getSelectedItem();
                        Statement statement = connection.createStatement();
                        ResultSet set = statement.executeQuery("select * from " + table.replace(' ', '_'));
                        ResultSetMetaData metaData = set.getMetaData();
                        //input the stuff
                        String values =  Utils.compileValues(lines[0], metaData);
                        statement.executeUpdate("insert into " + table.replace(' ', '_') + " values (" + values + ")");
                        statement.close();
                        set.close();
                        outputArea.setText("Inserimento riuscito. Valori inseriti:\n" + values);
                    } catch(SQLException e) {
                        outputArea.setText("C'è stato un errore:\n" + e.getMessage());
                    }
                }
        );

        frame.add(panel);
        panel.add(topGrid[0]);
        panel.add(scroller);
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
