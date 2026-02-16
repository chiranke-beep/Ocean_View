package com.example.ocean_view_resort.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for resetting AUTO_INCREMENT counters
 */
public class DatabaseResetUtil {
    
    /**
     * Resets AUTO_INCREMENT counter for a table if it's empty
     */
    public static void resetAutoIncrementIfEmpty(String tableName) {
        String countSql = "SELECT COUNT(*) as count FROM " + tableName;
        String resetSql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(countSql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("count") == 0) {
                    // Table is empty, reset AUTO_INCREMENT
                    try (PreparedStatement resetPs = conn.prepareStatement(resetSql)) {
                        resetPs.executeUpdate();
                        System.out.println("Reset AUTO_INCREMENT for table: " + tableName);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking/resetting AUTO_INCREMENT for " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
