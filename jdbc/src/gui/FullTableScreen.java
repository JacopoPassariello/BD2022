package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FullTableScreen {
    public FullTableScreen(Connection connection) throws SQLException {
        //Instantiating GUI components
        JFrame frame = new JFrame("Schermata di visualizzazione");
        JPanel panel = new JPanel();
        JPanel topRow = new JPanel();
        JLabel tableLabel = new JLabel("Scegliere la tabella: ");
        JButton enter = new JButton("Mostra");
        JTextArea outputArea = new JTextArea();
        JScrollPane scroller = new JScrollPane(outputArea);
        JComboBox<String> tables = Utils.compileTables(connection);

        //setting up properties for the GUI components
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
                        if(table == null) return;

                        Statement statement = connection.createStatement();
                        ResultSet contents = statement.executeQuery("select * from " + table.replace(' ', '_'));

                        outputArea.setText(Utils.printTable(contents));

                    } catch (SQLException e) {
                        outputArea.setText("C'è stato un errore nell'esecuzione dell'operazione.\n");
                        outputArea.append(e.getMessage());
                    }
                }
        );

        //building the GUI
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
