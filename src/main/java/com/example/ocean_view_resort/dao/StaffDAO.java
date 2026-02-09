package com.example.ocean_view_resort.dao;

import com.example.ocean_view_resort.model.Staff;
import java.util.List;

public interface StaffDAO {
    Staff getStaffByUsername(String username);
    Staff getStaffById(int staffId);
    List<Staff> getAllStaff();
    boolean addStaff(Staff staff);
    boolean updateStaff(Staff staff);
    boolean deleteStaff(int staffId);
}
