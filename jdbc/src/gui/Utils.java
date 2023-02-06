package gui;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    //returns a jcombobox containing all the table names from a given connection to a database
    public static JComboBox<String> compileTables(Connection connection) throws SQLException {
        JComboBox<String> tables = new JComboBox<>();
        Statement statement = connection.createStatement();
        ResultSet tablesSet = statement.executeQuery("show tables");

        while(tablesSet.next()) {
            tables.addItem(tablesSet.getString(1).replace('_', ' '));
        }
        tablesSet.close();
        statement.close();

        return tables;
    }

    //builds the line list based on what columns are in the given table
    public static List<Line> compileLines(Connection connection, String table) throws SQLException {
        List<Line> lines = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from " + table.replace(' ', '_'));
        ResultSetMetaData metaData = result.getMetaData();

        int columnNumber = metaData.getColumnCount();
        for(int i = 1; i <= columnNumber; i++) {
            lines.add(new Line(new JLabel(metaData.getColumnName(i).replace('_', ' ')), new JTextField()));
        }

        result.close();
        statement.close();
        return lines;
    }
}
