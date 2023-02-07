package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RawQueryScreen {

    public RawQueryScreen(Connection connection) throws SQLException {
        //Instantiating the GUI components
        JFrame frame = new JFrame("Query diretta");
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel bottomPanel = new JPanel();
        JTextArea inputArea = new JTextArea();
        JTextArea outputArea = new JTextArea();
        JButton enter = new JButton("Esegui Query");
        JScrollPane inputScroller = new JScrollPane(inputArea);
        JScrollPane outputScroller = new JScrollPane(outputArea);

        //setting up properties for the GUI components
        inputArea.setEditable(true);
        inputArea.setVisible(true);
        inputScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        outputArea.setEditable(false);
        outputArea.setVisible(true);
        outputScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        outputScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        enter.addActionListener(
                actionEvent -> {
                    try {
                        String query = inputArea.getText();
                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery(query);

                        outputArea.setText(Utils.printTable(result));

                    } catch (SQLException e) {
                        outputArea.setText("C'è stato un errore:\n" + e.getMessage());
                    }
                }
        );

        frame.add(splitPane);
        splitPane.add(inputScroller);
        splitPane.add(bottomPanel);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(outputScroller, BorderLayout.CENTER);
        bottomPanel.add(enter, BorderLayout.SOUTH);

        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
