package com.example.ocean_view_resort.service.impl;

import com.example.ocean_view_resort.dao.StaffDAO;
import com.example.ocean_view_resort.dao.impl.StaffDAOImpl;
import com.example.ocean_view_resort.model.Staff;
import com.example.ocean_view_resort.service.StaffService;
import com.example.ocean_view_resort.utils.PasswordUtil;

import java.util.List;

public class StaffServiceImpl implements StaffService {
    private StaffDAO staffDAO = new StaffDAOImpl();

    @Override
    public Staff authenticateStaff(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        
        Staff staff = staffDAO.getStaffByUsername(username);
        if (staff != null) {
            String hashedPassword = PasswordUtil.hash(password);
            if (staff.getPassword().equals(hashedPassword)) {
                return staff;
            }
        }
        return null;
    }

    @Override
    public Staff getStaffById(int staffId) {
        return staffDAO.getStaffById(staffId);
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    @Override
    public boolean addStaff(String name, String username, String password, String contactNumber, String role) {
        if (name == null || name.isEmpty() || username == null || username.isEmpty() || 
            password == null || password.isEmpty() || role == null || role.isEmpty()) {
            return false;
        }
        
        // Check if username already exists
        if (staffDAO.getStaffByUsername(username) != null) {
            return false;
        }
        
        String hashedPassword = PasswordUtil.hash(password);
        Staff staff = new Staff();
        staff.setName(name);
        staff.setUsername(username);
        staff.setPassword(hashedPassword);
        staff.setContactNumber(contactNumber);
        staff.setRole(role);
        
        return staffDAO.addStaff(staff);
    }

    @Override
    public boolean updateStaff(int staffId, String name, String password, String contactNumber, String role) {
        if (name == null || name.isEmpty() || role == null || role.isEmpty()) {
            return false;
        }
        
        Staff staff = staffDAO.getStaffById(staffId);
        if (staff == null) {
            return false;
        }
        
        staff.setName(name);
        staff.setContactNumber(contactNumber);
        staff.setRole(role);
        
        // Only update password if provided
        if (password != null && !password.isEmpty()) {
            staff.setPassword(PasswordUtil.hash(password));
        }
        
        return staffDAO.updateStaff(staff);
    }

    @Override
    public boolean editStaff(int staffId, String name, String contactNumber, String role) {
        if (name == null || name.isEmpty() || role == null || role.isEmpty()) {
            return false;
        }
        
        Staff staff = staffDAO.getStaffById(staffId);
        if (staff == null) {
            return false;
        }
        
        staff.setName(name);
        staff.setContactNumber(contactNumber);
        staff.setRole(role);
        // Password is NOT modified during edit operation
        
        return staffDAO.updateStaff(staff);
    }

    @Override
    public boolean deleteStaff(int staffId) {
        return staffDAO.deleteStaff(staffId);
    }
}
