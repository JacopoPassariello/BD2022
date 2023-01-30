package gui;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
