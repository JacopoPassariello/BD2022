package gui;

import javax.swing.*;
import java.sql.*;
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
}
