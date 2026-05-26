package com.example.hotel_management_system.service;

import com.example.hotel_management_system.model.Staff;
import java.util.List;

public interface StaffService {
    Staff createStaff(Staff staff);
    Staff getStaffById(Long id);
    Staff getStaffByEmail(String email);
    List<Staff> getAllStaff();
    Staff updateStaff(Long id, Staff staffDetails);
    void deactivateStaff(Long id);
}
