package com.example.ocean_view_resort.service;

import com.example.ocean_view_resort.model.Staff;
import java.util.List;

/**
 * Service Layer for Staff Business Logic
 * Encapsulates business rules, validation, and complex operations
 * Acts as intermediary between Controller and DAO
 */
public interface StaffService {
    Staff authenticateStaff(String username, String password);
    Staff getStaffById(int staffId);
    List<Staff> getAllStaff();
    boolean addStaff(String name, String username, String password, String contactNumber, String role);
    boolean updateStaff(int staffId, String name, String password, String contactNumber, String role);
    boolean deleteStaff(int staffId);
}
