package com.dean.course5.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dean on 15/1/4.
 */
public class UserDao {

    private final int SERVER_SIZE = 3;

    public User queryById(long userId) {
        User result = new User();

        String tableName = "test_" + userId % 3;

        System.out.println("table name: " + tableName);
        String sql = "SELECT * FROM " + tableName + " WHERE userId = ?";

        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.setUserId(rs.getLong("userId"));
                result.setUserName(rs.getString("userName"));
            }
            DBUtil.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public void save(User user) {
        String tableName = "test_" + user.getUserId() % 3;
        System.out.println("table name: " + tableName);
        String sql = "INSERT INTO " + tableName + " (userId, userName) VALUES (?, ?)";
        try {
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.executeUpdate();
            DBUtil.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
