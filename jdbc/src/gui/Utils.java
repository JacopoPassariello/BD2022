package gui;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class Utils {

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

    public static String compileValues(List<InsertionScreen.Line> lines, ResultSetMetaData metaData) throws SQLException {
        String values = "";

        int columnCount = metaData.getColumnCount();
        for(int i = 1; i < columnCount; i++) {
            if(metaData.getColumnTypeName(i).equals("CHAR") || metaData.getColumnTypeName(i).equals("DATE"))
                values += "\"" + lines.get(i - 1).field().getText() + "\", ";
            else
                values += lines.get(i - 1).field().getText() + ", ";
        }
        if(metaData.getColumnTypeName(columnCount).equals("CHAR") || metaData.getColumnTypeName(columnCount).equals("DATE"))
            values += "\"" + lines.get(columnCount - 1).field().getText() + "\"";
        else
            values += lines.get(columnCount - 1).field().getText();

        return values;
    }
}
