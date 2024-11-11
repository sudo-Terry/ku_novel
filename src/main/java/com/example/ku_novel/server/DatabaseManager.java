package com.example.ku_novel.server;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:ku_novel.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createUsersTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUsersTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id TEXT PRIMARY KEY,
                password TEXT NOT NULL,
                nickname TEXT NOT NULL UNIQUE
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUsernameExists(String id) {
        String query = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNicknameExists(String nickname) {
        String query = "SELECT nickname FROM users WHERE nickname = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String id, String password, String nickname) {
        String insertSQL = "INSERT INTO users (id, password, nickname) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            pstmt.setString(3, nickname);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
