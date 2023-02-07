package gui;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String printTable(ResultSet contents) throws SQLException {
        String result = "";

        ResultSetMetaData metadata = contents.getMetaData();
        List<String> columns = new ArrayList<>();

        //collect all col names in a list
        int columnNumber = metadata.getColumnCount();
        for(int i = 1; i <= columnNumber; i++) {
            columns.add(metadata.getColumnName(i));
        }

        //print all column names
        for(String column : columns) {
            result += (column.replace('_', ' ') + "\t");
        }
        result += "\n";

        //print all contents from the table
        while(contents.next()) {
            for(String column : columns) {
                result += (contents.getString(column) + "\t");
            }
            result += "\n";
        }

        return result;
    }

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

    //returns a string containing the values to be given the "insert into <table> values" query, correctly formatted
    public static List<String> compileValues(List<Line> lines, Connection connection, String table) throws SQLException {
        List<String> values = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery("select * from " + table.replace(' ', '_'));
        ResultSetMetaData metaData = set.getMetaData();

        int columnCount = metaData.getColumnCount();
        for(int i = 1; i < columnCount; i++) {
            //if the column is a string (char) or a date, the value must be encapsulated by ""
            if(metaData.getColumnTypeName(i).equals("CHAR") || metaData.getColumnTypeName(i).equals("DATE"))
                values.add("\"" + lines.get(i - 1).field().getText() + "\", ");
            else
                values.add(lines.get(i - 1).field().getText() + ", ");
        }
        if(metaData.getColumnTypeName(columnCount).equals("CHAR") || metaData.getColumnTypeName(columnCount).equals("DATE"))
            values.add("\"" + lines.get(columnCount - 1).field().getText() + "\"");
        else
            values.add(lines.get(columnCount - 1).field().getText());

        statement.close();
        set.close();

        return values;
    }
}
