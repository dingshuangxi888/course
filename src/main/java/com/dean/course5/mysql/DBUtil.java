package com.dean.course5.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dean on 15/1/4.
 */
public class DBUtil {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test?user=root&password=root";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
